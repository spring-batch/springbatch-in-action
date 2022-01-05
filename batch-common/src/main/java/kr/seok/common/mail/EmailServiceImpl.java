package kr.seok.common.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    public static final String ENCODING_UTF8 = "UTF-8";
    private final JavaMailSender javaMailSender;

    @Value(value = "${spring.mail.username}")
    private String from;

    public void sendSimpleMessage(String subject, String template, String... to) {
        MimeMessagePreparator mimeMessagePreparator = makeMassage(subject, template, to);
        try {
            javaMailSender.send(mimeMessagePreparator);
            log.info("[LOG] [SEND] EXECUTE");
        } catch(MailException e) {
            log.debug("[LOG] [MailException] : {}", e.getMessage());
        }
    }

    private MimeMessagePreparator makeMassage(final String subject, final String template, final String[] to) {
        return mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, ENCODING_UTF8);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setTo(to);
        };
    }
}
