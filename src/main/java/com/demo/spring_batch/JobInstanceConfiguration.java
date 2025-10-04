package com.demo.spring_batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobInstanceConfiguration {

    @Bean
    public Job BatchJob(JobRepository jobRepository,
                        Step step1, Step step2) {
        return new JobBuilder("batchJob", jobRepository)
                .start(step1)
                .next(step2)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
                        JobInstance jobInstance = contribution.getStepExecution().getJobExecution().getJobInstance();
                        System.out.println("jobInstance.getId() = " + jobInstance.getId());
                        System.out.println("jobInstance.getInstanceId() = " + jobInstance.getInstanceId());
                        System.out.println("jobInstance.getJobName() = " + jobInstance.getJobName());
                        System.out.println("jobInstance.getVersion() = " + jobInstance.getVersion());
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }


    @Bean
    public Step step2(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2 has executed");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
