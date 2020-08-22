package com.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.BeforeRead;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomItemReadListener //implements ItemReadListener<T>
{

    @BeforeRead
    public void beforeRead() {

    }

    @AfterRead
    public void afterRead(T item) {

    }

    @OnReadError
    public void onReadError(Exception ex) {

    }
}
