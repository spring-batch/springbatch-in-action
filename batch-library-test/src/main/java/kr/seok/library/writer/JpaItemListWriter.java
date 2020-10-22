package kr.seok.library.writer;

import org.springframework.batch.item.database.JpaItemWriter;

import java.util.ArrayList;
import java.util.List;

public class JpaItemListWriter<T> extends JpaItemWriter<List<T>> {

    private JpaItemWriter<T> jpaItemWriter;

    public JpaItemListWriter(JpaItemWriter<T> jpaItemWriter) {
        this.jpaItemWriter = jpaItemWriter;
    }

    @Override
    public void write(List<? extends List<T>> items) {
        List<T> totalList = new ArrayList<>();
        /* processor에서 CityEntity를 제네릭으로하는 List 타입의 데이터를 넘김 */
        totalList.addAll(items.get(items.size() - 1));
        jpaItemWriter.write(totalList);
    }
}
