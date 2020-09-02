package com.batch.listener;

import com.batch.domain.batch.LibraryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ItemReaderListener implements ItemReadListener<LibraryDTO> {
    @Override
    public void beforeRead() {
        log.info("[LOG] [READER] [BEFORE]");
    }

    @Override
    public void afterRead(LibraryDTO item) {
        log.info("[LOG] [READER] [DATA] [{}]", item);
    }

    @Override
    public void onReadError(Exception ex) {
        log.error("[LOG] [READER] [ERROR]");
    }
}
