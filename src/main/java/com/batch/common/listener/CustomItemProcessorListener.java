package com.batch.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomItemProcessorListener<T, S> implements ItemProcessListener<T, S> {

    @Override
    public void beforeProcess(T item) {
        /* log.info("[LOG] [PROCESSOR] [BEFORE] [{}]", item); */
    }

    @Override
    public void afterProcess(T item, S result) {
        log.info("[LOG] [PROCESSOR] [AFTER] [ITEM] [{}]", item);
        log.info("[LOG] [PROCESSOR] [AFTER] [RESULT] [{}]", result);
    }

    @Override
    public void onProcessError(T item, Exception e) {
        log.info("[LOG] [PROCESSOR] [ERROR] [{}] [{}]", item, e.getStackTrace());
    }
}
