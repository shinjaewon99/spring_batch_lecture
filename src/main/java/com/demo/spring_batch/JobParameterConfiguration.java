package com.demo.spring_batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
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

import java.util.Date;
import java.util.Map;

@Configuration
public class JobParameterConfiguration {

    @Bean
    public Job JobParam(JobRepository jobRepository,
                        Step step1, Step step2) {
        return new JobBuilder("JobParam", jobRepository)
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
                        JobParameters jobParameters = contribution.getStepExecution().getJobParameters();
                        String name = jobParameters.getString("name");
                        Long seq = jobParameters.getLong("seq");
                        Date date = jobParameters.getDate("date");

                        System.out.println("========================");
                        System.out.println("name = " + name);
                        System.out.println("seq = " + seq);
                        System.out.println("date = " + date);
                        System.out.println("========================");

                        Map<String, Object> jobParameters2 = chunkContext.getStepContext().getJobParameters();
                        String name2 = (String) jobParameters2.get("name");
                        long seq2 = (long) jobParameters2.get("seq");

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
                        System.out.println("step2 has executed");

                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }
}
