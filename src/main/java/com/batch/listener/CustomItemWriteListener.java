package com.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CustomItemWriteListener<S> // implements ItemWriteListener<S>
{

    @BeforeWrite
    public void beforeWrite(List<? extends S> items) {

    }

    @AfterWrite
    public void afterWrite(List<? extends S> items) {

    }

    @OnWriteError
    public void onWriteError(Exception exception, List<? extends S> items) {

    }
}
