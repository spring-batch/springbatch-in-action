package com.batch.writer;


import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * Custom Item Writer
 * @param <T>
 */
public class CustomItemWriter<T> implements ItemWriter<T> {
    @Override
    public void write(List items) throws Exception {

    }
}
