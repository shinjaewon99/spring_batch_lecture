package com.demo.spring_batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
public class JobBuilderConfiguration {

    @Bean
    public Job batchJob1(
            JobRepository jobRepository,
            Step step1, Step step2) {
        return new JobBuilder("batchJob1", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .next(step2)
                .build();
    }

    @Bean
    public Job batchJob2(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            Step step1, Step step2) {
        return new JobBuilder("batchJob1", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(flow(jobRepository, transactionManager)) // flow 메서드에 있는 step 3,4가 먼저 실행후 step2 실행
                .next(step2)
                .end() // flow API 호출시 end를 필수로 작성
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
    public Flow flow(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager) {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow");
        flowBuilder
                .start(step3(jobRepository, transactionManager))
                .next(step4(jobRepository, transactionManager))
                .end();
        return flowBuilder.build();
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

    @Bean
    public Step step4(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step4", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step4 실행");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
