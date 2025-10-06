package com.demo.spring_batch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobRepositoryListener implements JobExecutionListener {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }


    @Override
    public void afterJob(JobExecution jobExecution) {

        String jobName = jobExecution.getJobInstance().getJobName();
        JobParameters jobParameters = new JobParametersBuilder().addString("requestDate", "20251006").toJobParameters();
        JobExecution lastJobExecution = jobRepository.getLastJobExecution(jobName, jobParameters);

        if (lastJobExecution == null) {
            for (StepExecution stepExecution : lastJobExecution.getStepExecutions()) {
                BatchStatus status = stepExecution.getStatus();

                System.out.println("status.isRunning() = " + status.isRunning());
                System.out.println("status.name() = " + status.name());
            }
        }
    }
}
