package com.batch.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * Custom Item reader
 * @param <T>
 */
public class CustomItemReader<T> implements ItemReader<T> {
    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }
}
