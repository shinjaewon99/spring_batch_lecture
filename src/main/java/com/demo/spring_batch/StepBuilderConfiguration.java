package com.demo.spring_batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StepBuilderConfiguration {

    @Bean
    public Job batchJob(JobRepository jobRepository,
                        Step step1, Step step2,
                        Step step3, Step step4) {
        return new JobBuilder("batchJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .next(step2)
                .next(step3) // job 실행
                .next(step4) // flow 실행
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
                .<String, String>chunk(3, transactionManager) // <입력, 출력> in, out 타입 설정
                .reader(() -> null)  // 데이터 읽기
                .writer(list -> {})  // 데이터 쓰기
                .build();
    }

    @Bean
    public Step step3(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      JobLauncher jobLauncher,
                      Job subJob) {
        return new JobStepBuilder(new StepBuilder("step3", jobRepository))
                .job(subJob)
                .launcher(jobLauncher)
                .build();
    }

    @Bean
    public Step step4(JobRepository jobRepository,
                      Flow flow) {
        return new StepBuilder("step4", jobRepository)
                .flow(flow)
                .build();
    }

    @Bean
    public Job subJob(JobRepository jobRepository,
                      Step step1, Step step2) {
        return new JobBuilder("subJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .next(step2)
                .build();
    }

    @Bean
    public Flow flow(Step step2) {
        return new FlowBuilder<Flow>("flow")
                .start(step2)
                .end();
    }
}
