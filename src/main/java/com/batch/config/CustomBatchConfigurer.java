package com.batch.config;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.stereotype.Component;

/**
 * DefaultBatchConfigurer 를 상속받은 클래스를 Bean으로 등록하여 기존의 DefaultBatchConfigurer가 적용되지 않도록 설정
 */
@Component
public class CustomBatchConfigurer extends DefaultBatchConfigurer {
}
