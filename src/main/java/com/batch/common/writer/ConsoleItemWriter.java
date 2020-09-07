package com.batch.common.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * 데이터 조회 후 임의 데이터 출력을 위한 ItemWriter 구현 클래스
 *
 * @param <T> 임의 타입
 */
@Slf4j
public class ConsoleItemWriter<T> implements ItemWriter<T> {
    @Override
    public void write(List<? extends T> items) {
        for (T item : items) {
            log.info("[LOG] Console Writer : {}", item);
        }
    }
}
