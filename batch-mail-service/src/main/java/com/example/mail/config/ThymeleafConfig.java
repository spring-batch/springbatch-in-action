package com.example.mail.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class ThymeleafConfig {

    private static final String CHARACTER_ENCODING_UTF8 = "UTF-8";
    public static final String PREFIX_LOCATION = "classpath:templates/";
    public static final String SUFFIX_HTML = ".html";

    @Bean
    public TemplateEngine htmlTemplateEngine() {
        TemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(springResourceTemplateResolver());
        return templateEngine;
    }

    @Bean
    public SpringResourceTemplateResolver springResourceTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setOrder(1);
        resolver.setPrefix(PREFIX_LOCATION);
        resolver.setSuffix(SUFFIX_HTML);
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(CHARACTER_ENCODING_UTF8);
        resolver.setCacheable(false);
        return resolver;
    }
}
