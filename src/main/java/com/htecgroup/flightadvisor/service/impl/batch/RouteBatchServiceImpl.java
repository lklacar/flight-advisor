package com.htecgroup.flightadvisor.service.impl.batch;

import com.htecgroup.flightadvisor.config.JobCompletionNotificationListener;
import com.htecgroup.flightadvisor.domain.Airport;
import com.htecgroup.flightadvisor.domain.Route;
import com.htecgroup.flightadvisor.service.batch.JobService;
import com.htecgroup.flightadvisor.service.batch.RouteBatchService;
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
public class RouteBatchServiceImpl implements RouteBatchService {
    private static final String ROUTE_READER_NAME = "ROUTE_ITEM_READER";
    private static final String IMPORT_ROUTE_STEP_NAME = "IMPORT_ROUTES";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SessionFactory sessionFactory;
    private final JobCompletionNotificationListener listener;
    private final LineTokenizer routeLineTokenizer;
    private final JobService jobService;

    public RouteBatchServiceImpl(
            JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory,
            SessionFactory sessionFactory,
            JobCompletionNotificationListener listener,
            @Qualifier("routeOracleCSVLineTokenizer") LineTokenizer routeLineTokenizer,
            JobService jobService
    ) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.sessionFactory = sessionFactory;
        this.listener = listener;
        this.routeLineTokenizer = routeLineTokenizer;
        this.jobService = jobService;
    }

    public Job createImportRoutesJob(String fileName, String taskName, List<Airport> airports) {

        return jobBuilderFactory.get(taskName)
                .preventRestart()
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(createImportRoutesStep(fileName, airports))
                .end()
                .build();
    }

    private FlatFileItemReader<Route> createRouteReader(String filePath) {
        var fieldSetMapper = new BeanWrapperFieldSetMapper<Route>();
        fieldSetMapper.setTargetType(Route.class);
        return new FlatFileItemReaderBuilder<Route>().name(ROUTE_READER_NAME)
                .resource(new FileSystemResource(filePath))
                .lineTokenizer(routeLineTokenizer)
                .fieldSetMapper(fieldSetMapper)
                .build();
    }

    private ItemWriter<Route> createRouteWriter() {
        return new Neo4jItemWriterBuilder<Route>().sessionFactory(sessionFactory)
                .build();
    }

    private TaskExecutor createTaskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    private Step createImportRoutesStep(String fileName, List<Airport> airports) {
        RouteProcessor routeItemProcessor = new RouteProcessor(airports);

        return stepBuilderFactory.get(IMPORT_ROUTE_STEP_NAME).<Route, Route>chunk(1000).reader(createRouteReader(
                fileName))
                .processor(routeItemProcessor)
                .writer(createRouteWriter())
                .taskExecutor(createTaskExecutor())
                .throttleLimit(20)
                .build();
    }

    @Override
    public ImportResponse startImportJob(File file, List<Airport> airports) {
        var job = createImportRoutesJob(file.getAbsolutePath(), file.getPath(), airports);
        var res = jobService.startJob(job);

        return ImportResponse.builder()
                .success(res.getStatus() == BatchStatus.COMPLETED)
                .totalRecords(res.getStepExecutions()
                        .stream()
                        .findFirst()
                        .map(StepExecution::getReadCount)
                        .orElse(0))
                .writeCount(res.getStepExecutions()
                        .stream()
                        .findFirst()
                        .map(StepExecution::getWriteCount)
                        .orElse(0))
                .build();
    }

}
