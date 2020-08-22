package com.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomSkipListener<S> // implements SkipListener<T, S>
{
    @OnSkipInRead
    public void onSkipInRead(Throwable t) {

    }

    @OnSkipInWrite
    public void onSkipInWrite(S item, Throwable t) {

    }

    @OnSkipInProcess
    public void onSkipInProcess(T item, Throwable t) {

    }
}
