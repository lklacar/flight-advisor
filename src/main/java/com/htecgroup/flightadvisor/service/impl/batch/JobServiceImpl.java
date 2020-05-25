package com.htecgroup.flightadvisor.service.impl.batch;

import com.htecgroup.flightadvisor.exception.StartJobException;
import com.htecgroup.flightadvisor.service.batch.JobService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImpl implements JobService {
    private final JobLauncher jobLauncher;

    public JobServiceImpl(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    @Override
    public JobExecution startJob(Job job) {
        try {
            return jobLauncher.run(job, new JobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            throw new StartJobException(e);
        }
    }
}
