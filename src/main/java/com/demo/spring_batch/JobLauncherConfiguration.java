package com.demo.spring_batch;

import org.springframework.batch.core.Job;
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
public class JobLauncherConfiguration {

    @Bean
    public Job LauncherJob(JobRepository jobRepository,
                           Step step1, Step step2) {
        return new JobBuilder("LauncherJob", jobRepository)
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

                        // 비동기 처리 확인을 위한 sleep
                        Thread.sleep(3000);
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {

        return new StepBuilder("step1", jobRepository)
                .tasklet(((contribution, chunkContext) -> null)
                        , transactionManager)
                .build();
    }
}
