package com.demo.spring_batch;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.text.SimpleDateFormat;
import java.util.Date;

// mysql의 autoIncrement와 유사
// custom 해서 사용 가능
public class CustomJobParametersIncrementer implements JobParametersIncrementer {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-hhmmss");

    @Override
    public JobParameters getNext(final JobParameters parameters) {
        String id = format.format(new Date());

        return new JobParametersBuilder().addString("run.id", id).toJobParameters();
    }
}
