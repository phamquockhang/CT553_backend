package com.pqkhang.ct553_backend.domain.common.service.impl;

import com.pqkhang.ct553_backend.domain.common.service.EmailService;
import com.pqkhang.ct553_backend.domain.transaction.entity.Transaction;
import com.pqkhang.ct553_backend.infrastructure.kafka.email.EmailObject;
import com.pqkhang.ct553_backend.infrastructure.kafka.email.EmailProducer;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {

    SpringTemplateEngine springTemplateEngine;
    JavaMailSender javaMailSender;
    Environment environment;
    EmailProducer emailProducer;

    private void sendEmail(EmailObject emailObject) throws MessagingException, UnsupportedEncodingException {
        String receiverEmail = emailObject.getReceiverEmail();
        String subject = emailObject.getSubject();
        String templateName = emailObject.getTemplateFileName();
        Map<String, Object> context = emailObject.getContext();

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(context);
        String content = springTemplateEngine.process(templateName, thymeleafContext);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);

        String senderEmail = environment.getProperty("EMAIL_USER");
        String senderName = "K-Seafood";

        assert senderEmail != null;
        mimeMessageHelper.setFrom(senderEmail, senderName);
        mimeMessageHelper.setTo(receiverEmail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true);

        javaMailSender.send(message);
    }

    public void sendEmailAsync(EmailObject emailObject) {
        CompletableFuture.runAsync(() -> {
            try {
                sendEmail(emailObject);
            } catch (MessagingException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void sendSuccessfullyTransactionEmail(Transaction transaction) {
        String CLIENT_URL = environment.getProperty("CLIENT_URL");

        Map<String, Object> context = Map.of(
                "transactionId", transaction.getTransactionId(),
                "sellingOrderId", transaction.getSellingOrder().getSellingOrderId(),
                "sellingOrderDetailURL", CLIENT_URL + "/selling-order/" + transaction.getSellingOrder().getSellingOrderId()
        );

        EmailObject emailObject = EmailObject.builder()
                .receiverEmail(transaction.getSellingOrder().getCustomer().getEmail())
                .subject("K-Seafood - Thông báo đơn hàng #" + transaction.getSellingOrder().getSellingOrderId() + " của bạn")
                .templateFileName("success-transaction")
                .context(context)
                .build();

        emailProducer.sendEmailMessage(emailObject);
    }
}
