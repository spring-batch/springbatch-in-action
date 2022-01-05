package com.example.mail.template;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MailTemplateBuilder {

    public static final String TEMPLATE_PATH = "mail/";
    private final TemplateEngine templateEngine;

    public String makeTemplate(String templateName, List<? extends Map<String, Object>> users) {
        Context context = new Context(Locale.getDefault());
        context.setVariable("list", users);
        return templateEngine.process(TEMPLATE_PATH + templateName, context);
    }
}
