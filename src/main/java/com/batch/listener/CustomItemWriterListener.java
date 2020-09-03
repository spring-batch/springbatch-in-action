package com.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

@Slf4j
public class CustomItemWriterListener<S> implements ItemWriteListener<S> {
    int preSize = 0;
    int postSize = 0;
    @Override
    public void beforeWrite(List<? extends S> items) {
        preSize = items.size();
        log.info("[LOG] [Writer] [BEFORE] [{}]", items.size());
    }

    @Override
    public void afterWrite(List<? extends S> items) {
        postSize = items.size();
        log.info("[LOG] [Writer] [AFTER] [{}]", items.size());

        if(preSize != postSize) throw new RuntimeException("갯수가 맞지 않음");
    }

    @Override
    public void onWriteError(Exception exception, List<? extends S> items) {
        log.info("[LOG] [Writer] [{}]", exception.getLocalizedMessage());
    }
}
