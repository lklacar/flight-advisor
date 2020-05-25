package com.htecgroup.flightadvisor.service.impl.batch;

import com.htecgroup.flightadvisor.config.JobCompletionNotificationListener;
import com.htecgroup.flightadvisor.domain.Airport;
import com.htecgroup.flightadvisor.domain.City;
import com.htecgroup.flightadvisor.service.batch.AirportBatchService;
import com.htecgroup.flightadvisor.service.batch.JobService;
import com.htecgroup.flightadvisor.service.dto.ImportResponse;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.Neo4jItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.io.File;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class AirportBatchServiceImpl implements AirportBatchService {
    private static final String AIRPORT_READER_NAME = "AIRPORT_ITEM_READER";
    private static final String IMPORT_AIRPORT_STEP_NAME = "IMPORT_AIRPORTS";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SessionFactory sessionFactory;
    private final JobCompletionNotificationListener listener;
    private final LineTokenizer airportLineTokenizer;
    private final JobService jobService;

    public AirportBatchServiceImpl(
            JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory,
            SessionFactory sessionFactory,
            JobCompletionNotificationListener listener,
            @Qualifier("airportOracleCSVLineTokenizer") AbstractOracleCSVLineTokenizer airportLineTokenizer,
            JobService jobService
    ) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.sessionFactory = sessionFactory;
        this.listener = listener;
        this.airportLineTokenizer = airportLineTokenizer;
        this.jobService = jobService;
    }

    public ImportResponse startImportJob(File file, List<City> cities, List<Airport> airports) {
        var job = createImportAirportsJob(file.getAbsolutePath(), file.getPath(), cities, airports);
        var res = jobService.startJob(job);
        var totalRecords = res.getStepExecutions()
                .stream()
                .findFirst()
                .map(StepExecution::getReadCount)
                .orElse(0);
        var writeCount = res.getStepExecutions()
                .stream()
                .findFirst()
                .map(StepExecution::getWriteCount)
                .orElse(0);
        return ImportResponse.builder()
                .success(res.getStatus() == BatchStatus.COMPLETED)
                .totalRecords(totalRecords)
                .writeCount(writeCount)
                .build();
    }

    public Job createImportAirportsJob(String fileName, String taskName, List<City> cities, List<Airport> airports) {

        return jobBuilderFactory.get(taskName)
                .preventRestart()
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(createImportAirportsStep(fileName, cities, airports))
                .end()
                .build();
    }

    private FlatFileItemReader<Airport> createAirportReader(String filePath) {
        var fieldSetMapper = new BeanWrapperFieldSetMapper<Airport>();
        fieldSetMapper.setTargetType(Airport.class);

        return new FlatFileItemReaderBuilder<Airport>().name(AIRPORT_READER_NAME)
                .resource(new FileSystemResource(filePath))
                .lineTokenizer(airportLineTokenizer)
                .fieldSetMapper(fieldSetMapper)
                .build();
    }

    private ItemWriter<Airport> createAirportWriter() {
        return new Neo4jItemWriterBuilder<Airport>().sessionFactory(sessionFactory)
                .build();
    }

    private TaskExecutor createTaskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    private Step createImportAirportsStep(String fileName, List<City> cities, List<Airport> airports) {
        return stepBuilderFactory.get(IMPORT_AIRPORT_STEP_NAME).<Airport, Airport>chunk(100).reader(createAirportReader(
                fileName))
                .processor(new AirportProcessor(cities, airports))
                .writer(createAirportWriter())
                .taskExecutor(createTaskExecutor())
                .throttleLimit(20)
                .build();
    }
}


