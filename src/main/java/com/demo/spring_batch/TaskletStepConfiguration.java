package com.demo.spring_batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

@Configuration
public class TaskletStepConfiguration {

    @Bean
    public Job batchJob(JobRepository jobRepository,
                        Step taskStep, Step chunkStep) {
        return new JobBuilder("batchJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(taskStep)
                .next(chunkStep)
                .build();
    }

    @Bean
    public Step taskStep(JobRepository jobRepository,
                         PlatformTransactionManager transactionManager) {
        return new StepBuilder("taskStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> taskStep 실행");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step chunkStep(JobRepository jobRepository,
                          PlatformTransactionManager transactionManager) {
        return new StepBuilder("chunkStep", jobRepository)
                .<String, String>chunk(3, transactionManager)
                .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3")))
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(final String item) throws Exception {
                        return item.toUpperCase(); // 대문자로 바꾸는작업
                    }
                })
                .writer(list -> {
                    list.forEach(item -> System.out.println(item));
                })
                .build();
    }
}
