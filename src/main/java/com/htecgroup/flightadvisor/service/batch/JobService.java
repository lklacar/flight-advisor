package com.htecgroup.flightadvisor.service.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;

public interface JobService {

    /**
     * Starts a {@link Job}
     *
     * @param job Job that needs to be started
     * @return JobExecution for the given job
     */
    JobExecution startJob(Job job);
}
