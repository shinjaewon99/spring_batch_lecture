package com.demo.spring_batch;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

// .validator() 메소드 체인을 커스텀(오버라이딩)
public class CustomJobParametersValidator implements JobParametersValidator {
    @Override
    public void validate(final JobParameters parameters) throws JobParametersInvalidException {

        if (parameters.getString("name") == null) {
            throw new JobParametersInvalidException("name parameter is not found");
        }
    }
}
