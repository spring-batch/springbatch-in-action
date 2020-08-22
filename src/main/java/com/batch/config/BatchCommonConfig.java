package com.batch.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@ComponentScan(basePackageClasses = CustomBatchConfigurer.class)
@Configuration
public class BatchCommonConfig {
}
