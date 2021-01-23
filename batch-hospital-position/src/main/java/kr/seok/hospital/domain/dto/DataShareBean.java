package kr.seok.hospital.domain.dto;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class DataShareBean<T> {

    private Map<String, T> shareDataMap;

    public DataShareBean () {
        this.shareDataMap = Maps.newConcurrentMap();
    }

    public void putData(String key, T data) {
        if (shareDataMap ==  null) {
            log.error("Map is not initialize");
            return;
        }

        shareDataMap.put(key, data);
    }

    public T getData (String key) {

        if (shareDataMap == null) {
            return null;
        }

        return shareDataMap.get(key);
    }

    public int getSize () {
        if (this.shareDataMap == null) {
            log.error("Map is not initialize");
            return 0;
        }

        return shareDataMap.size();
    }

}
