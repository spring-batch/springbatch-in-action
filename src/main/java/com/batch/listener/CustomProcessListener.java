package com.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.batch.core.annotation.AfterProcess;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomProcessListener<S> // implements ItemProcessListener<T, S>
{

    @BeforeProcess
    public void beforeProcess(T item) {

    }

    @AfterProcess
    public void afterProcess(T item, S result) {

    }

    @OnProcessError
    public void onProcessError(T item, Exception e) {

    }
}
