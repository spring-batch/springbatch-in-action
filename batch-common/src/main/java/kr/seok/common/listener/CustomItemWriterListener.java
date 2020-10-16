package kr.seok.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 데이터 쓰기 시 ItemProcessor 에서 넘어온 값과 실제 입력 값 비교를 위한 클래스
 *
 * @param <S>
 */
@Slf4j
@Component
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

        if (preSize != postSize) onWriteError(new RuntimeException("데이터 입력 값이 같지 않음"), items);
    }

    @Override
    public void onWriteError(Exception exception, List<? extends S> items) {
        log.info("[LOG] [Writer] [{}]", exception.getLocalizedMessage());
    }
}
