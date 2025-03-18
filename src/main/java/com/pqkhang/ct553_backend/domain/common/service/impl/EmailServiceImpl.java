package com.pqkhang.ct553_backend.domain.common.service.impl;

import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrder;
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
                .subject("📢 Thông báo đơn hàng #" + transaction.getSellingOrder().getSellingOrderId() + " của bạn")
                .templateFileName("success-transaction")
                .context(context)
                .build();

        emailProducer.sendEmailMessage(emailObject);
    }

    @Override
    public void sendSellingOrderStatusEmail(SellingOrder sellingOrder) {
        String CLIENT_URL = environment.getProperty("CLIENT_URL");

        String orderStatus = "";
        String orderStatusText = "";
        String orderMessage = "";

        switch (sellingOrder.getOrderStatus()) {
            case CONFIRMED:
                orderStatus = "CONFIRMED";
                orderStatusText = "🎉 Đơn hàng đã được xác nhận";
                orderMessage = "Chúng tôi sẽ sớm giao hàng cho bạn. Cảm ơn bạn đã mua hàng tại K-Seafood!";
                break;
            case PREPARING:
                orderStatus = "PREPARING";
                orderStatusText = "⏳ Đơn hàng đang được chuẩn bị";
                orderMessage = "Chúng tôi đang chuẩn bị đơn hàng của bạn. Vui lòng chờ trong giây lát!";
                break;
            case DELIVERING:
                orderStatus = "DELIVERING";
                orderStatusText = "🚚 Đơn hàng đang được giao";
                orderMessage = "Đơn hàng của bạn đang trên đường đến. Hãy chuẩn bị nhận hàng!";
                break;
            case DELIVERED:
                orderStatus = "DELIVERED";
                orderStatusText = "✅ Đơn hàng đã giao thành công";
                orderMessage = "Cảm ơn bạn đã mua hàng tại K-Seafood! Hy vọng bạn hài lòng với sản phẩm.";
                break;
            case COMPLETED:
                orderStatus = "COMPLETED";
                orderStatusText = "🎊 Đơn hàng đã hoàn thành";
                orderMessage = "Cảm ơn bạn đã mua hàng tại K-Seafood! Đơn hàng đã hoàn tất và được ghi nhận.";
                break;
            case CANCELLED:
                orderStatus = "CANCELLED";
                orderStatusText = "😟 Đơn hàng đã bị hủy!!";
                orderMessage = "Rất tiếc, đơn hàng của bạn đã bị hủy. Nếu có thắc mắc, vui lòng liên hệ với chúng tôi.";
                break;
            default:
                orderStatus = "UNKNOWN";
                orderStatusText = "❓ Trạng thái đơn hàng không xác định";
                orderMessage = "Chúng tôi không thể xác định trạng thái đơn hàng của bạn. Vui lòng liên hệ hỗ trợ.";
                break;
        }

        Map<String, Object> context = Map.of(
                "orderStatus", orderStatus,
                "orderStatusText", orderStatusText,
                "orderMessage", orderMessage,
                "sellingOrderId", sellingOrder.getSellingOrderId(),
                "sellingOrderDetailURL", CLIENT_URL + "/selling-order/" + sellingOrder.getSellingOrderId()
        );

        EmailObject emailObject = EmailObject.builder()
                .receiverEmail(sellingOrder.getEmail())
                .subject("📢 Thông báo đơn hàng #" + sellingOrder.getSellingOrderId() + " của bạn")
                .templateFileName("selling-order-status")
                .context(context)
                .build();

        emailProducer.sendEmailMessage(emailObject);
    }

}
