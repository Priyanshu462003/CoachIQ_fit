package com.fitness.coachiq.Progress.Tracking.Service;


import org.springframework.mail.javamail.JavaMailSender;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendMonthlyReport(String email,
                                  byte[] pdf) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message,true);

            helper.setTo(email);

            helper.setSubject("CoachIQ Monthly Progress Report");

            helper.setText("""
Hello,

Your monthly AI fitness report is attached.

Stay consistent.

Team CoachIQ
""");

            helper.addAttachment(
                    "CoachIQ_Report.pdf",
                    new ByteArrayResource(pdf)
            );

            mailSender.send(message);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }
}