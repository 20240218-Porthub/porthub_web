package hello.example.porthub.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "wjdgusdlekd3@yonsei.ac.kr";
    private static int number;

    // 인증 메일 랜덤 전송 번호 생성
    public static void createNumber() {
        number = (int)(Math.random() * (90000)) + 100000;
    }

    public MimeMessage CreateMail(String mail) {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("[Porthub] Vertification Email");
            String body = "";
            body += "<h3>" + "인증 번호: " + "</h3>";
            body += "<h1>" + number + "</h1>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }
    public int sendMail(String mail) {
        MimeMessage message = CreateMail(mail);
        javaMailSender.send(message);

        return number;
    }
}
