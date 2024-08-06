package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.EmailDto;
import com.Duo960118.fitow.config.EmailConfig;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.Random;

@RequiredArgsConstructor
// final이 붙어있거나 @nonnull인 경우 생성자에 필요 arguments 지정
@Service
public class EmailSendService {
    private static final Logger log = LoggerFactory.getLogger(EmailSendService.class);
    private final EmailConfig emailConfig;
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    // final를 사용하여 필드 선언 시 생성자에서 초기화 필수
    // 최종값이므로 생성자에서 초기화가 없으면 null 값으로
    // 초기화하지 않으면 컴파일 에러 발생

    // 이메일 인증번호 생성기
    public String authNumberGenerator() {
        Random random = new Random();
        StringBuilder randomNumberBuilder = new StringBuilder();
        // 스트링빌더 공부하기
        int authNumLength = 6;
        for (int i = 0; i < authNumLength; i++) {
            randomNumberBuilder.append(random.nextInt(10));
        }
        return randomNumberBuilder.toString();
    }

    // todo: 메일 전송 속도 개선, smtp 대체 할 다른 걸 쓰거나, 답답함을 해소할 프로그레스바 같은것 프론트에 표시
    // 이메일 발송 정보
    public void sendEmail(String from, String to, String subject, String text) {
        // todo: mime message 이용 html 이메일 발송 admin
        // MimeMessage message = new MimeMessage();
        // message.set
        SimpleMailMessage message = new SimpleMailMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        // 예외: MailException
        mailSender.send(message);

//        try {
//
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
    }

    // mail 수발신 및 인증번호 html form
    public boolean sendAuthEmail(EmailDto.SendAuthEmailRequestDto sendAuthEmailRequest) {
        String authNum = authNumberGenerator();
        String from = emailConfig.getServiceEmail();
        String to = sendAuthEmailRequest.getEmail();
        String subject = "회원 가입 인증 이메일 입니다.";
        String text =
                "FITOW를 방문해주셔서 감사합니다.\n" +
                        "인증번호는 " + authNum + " 입니다.\n" +
                        "인증번호를 제대로 입력해주세요";
        sendEmail(from, to, subject, text);
        redisUtil.setDataExp(to, authNum, Duration.ofSeconds(60 * 4L)); //인증번호 4분 후 만료

//        try {
//
//
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return false;
//        }
        return true;
    }

    // 임시 비밀번호 발급 method
    public void sendTempPasswd(String receiverEmail, String passwd) {
        String from = emailConfig.getServiceEmail();
        String subject = "임시 비밀번호 발급 이메일 입니다.";
        String text =
                "FITOW를 방문해주셔서 감사합니다.\n" +
                        "임시 비밀번호는 " + passwd + " 입니다.";
        sendEmail(from, receiverEmail, subject, text);
    }

    // 인증번호 인증
    public boolean verifyEmail(EmailDto.VerifyEmailRequestDto verifyEmailRequest) {
        // null 예외발생을 방지하기 위해 equals > object.equals로 변경
        // null을 포함하여 비교할 수 있다.
        String storedAuthNum = redisUtil.getData(verifyEmailRequest.getEmail()).orElseThrow();
        return Objects.equals(verifyEmailRequest.getAuthNum(), storedAuthNum);

//        try {
//
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return false;
//        }
    }
}