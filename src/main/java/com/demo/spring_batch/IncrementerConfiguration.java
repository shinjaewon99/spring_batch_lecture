package com.demo.spring_batch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class IncrementerConfiguration {

    @Bean
    public Job batchJob(JobRepository jobRepository,
                        Step step1, Step step2, Step step3) {
        return new JobBuilder("batchJob", jobRepository)
//                .incrementer(new RunIdIncrementer())
                .incrementer(new CustomJobParametersIncrementer())
                .start(step1)
                .next(step2)
                .next(step3)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step1 실행");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step2 실행");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step3(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step3", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step3 실행");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
