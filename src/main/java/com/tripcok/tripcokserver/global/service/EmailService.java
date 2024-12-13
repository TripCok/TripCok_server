package com.tripcok.tripcokserver.global.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    @Value(value = "${mail.username}")
    private String SENDER_EMAIL;

    private final JavaMailSender mailSender;

    private static final long CODE_EXPIRATION_TIME = 300_000; // 5분

    public ResponseEntity<?> sendVerificationEmail(String email, HttpSession session) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        String code = generateVerificationCode();

        message.addRecipients(MimeMessage.RecipientType.TO, email);

        try {
            message.setSubject("TripCok 인증 코드 안내");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        String body = "<div>"
                + "<h1> 안녕하세요. Tripcok 입니다</h1>"
                + "<br>"
                + "<p>안내 인증 코드에 따라 인증 번호를 입력해주세요.<p>"
                + "<p>인증 코드 : " + code + "</p>"
                + "</div>";

        message.setText(body, "utf-8", "html");
        message.setFrom(new InternetAddress(SENDER_EMAIL, "TripCok"));
        mailSender.send(message);

        session.setAttribute(email, code);
        session.setMaxInactiveInterval((int) (CODE_EXPIRATION_TIME / 1000)); // 세션 만료 시간 설정


        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> verifyCode(String email, String code, HttpSession session) {
        log.info("verify code {}", code);
        log.info("verify email {}", email);
        String storedCode = (String) session.getAttribute(email);
        log.info("verify storedCode {}", storedCode);

        if (storedCode != null && storedCode.equals(code)) {
            session.removeAttribute(email);
            return ResponseEntity.status(HttpStatus.OK).body("인증에 성공하였습니다. 기존 코드는 삭제 됩니다.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증에 실패하였습니다.");
    }

    private String generateVerificationCode() {
        return String.format("%04d", new Random().nextInt(9999));
    }
}
