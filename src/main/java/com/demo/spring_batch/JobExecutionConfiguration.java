package com.demo.spring_batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
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
public class JobExecutionConfiguration {

    @Bean
    public Job ExecutionJob(JobRepository jobRepository,
                            Step step1, Step step2) {
        return new JobBuilder("ExecutionJob", jobRepository)
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
                        JobExecution jobExecution = contribution.getStepExecution().getJobExecution();
                        System.out.println("jobExecution = " + jobExecution);

                        System.out.println("step1 has executed");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {

                        /**
                         * 단일 step에서 예외가 발생하면 동이한 Job name이여도 재실행이 가능하다.
                         * throw new RuntimeException("JobExecution has failed");
                         */

                        System.out.println("step2 has executed");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }
}
