package com.batch.common.writer;

import org.springframework.batch.item.database.JpaItemWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * JpaItemWriter 를 상속받아 JpaItemListWriter로 재정의한 클래스
 *  [참고](https://jojoldu.tistory.com/140?category=902551)
 *
 * @param <T> 임의 타입클래스
 */
public class JpaItemListWriter<T> extends JpaItemWriter<List<T>> {
    private JpaItemWriter<T> jpaItemWriter;

    public JpaItemListWriter(JpaItemWriter<T> jpaItemWriter) {
        this.jpaItemWriter = jpaItemWriter;
    }

    @Override
    public void write(List<? extends List<T>> items) {
        List<T> totalList = new ArrayList<>();

        for (List<T> list : items) {
            totalList.addAll(list);
        }
        jpaItemWriter.write(totalList);
    }
}
