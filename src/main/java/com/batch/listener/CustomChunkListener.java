package com.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterChunkError;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomChunkListener // implements ChunkListener
{

    @BeforeChunk
    public void beforeChunk(ChunkContext context) {

    }

    @AfterChunk
    public void afterChunk(ChunkContext context) {

    }

    @AfterChunkError
    public void afterChunkError(ChunkContext context) {

    }
}
