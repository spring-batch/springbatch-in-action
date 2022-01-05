package kr.seok.common.mail;

public interface EmailService {
    void sendSimpleMessage(String subject, String template, String... to);
}
