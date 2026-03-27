package com.btg.funds_api.infrastructure.adapter;

import com.btg.funds_api.domain.gateway.NotificationGateway;
import com.btg.funds_api.domain.model.Fund;
import com.btg.funds_api.domain.model.NotificationPreference;
import com.btg.funds_api.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationAdapter implements NotificationGateway {

    private final SesClient sesClient;
    private final SnsClient snsClient;

    @Value("${aws.ses.from-email}")
    private String fromEmail;

    @Override
    public void send(User user, Fund fund, String message) {
        if (user.getNotificationPreference() == NotificationPreference.SMS) {
            sendSms(user.getPhone(), message);
        } else {
            sendEmail(user.getEmail(), message);
        }
    }

    private void sendEmail(String to, String body) {
        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .source(fromEmail)
                    .destination(Destination.builder()
                            .toAddresses(to)
                            .build())
                    .message(Message.builder()
                            .subject(Content.builder()
                                    .data("BTG Pactual - Notificación de fondo")
                                    .charset("UTF-8")
                                    .build())
                            .body(Body.builder()
                                    .text(Content.builder()
                                            .data(body)
                                            .charset("UTF-8")
                                            .build())
                                    .build())
                            .build())
                    .build();

            sesClient.sendEmail(request);
            log.info("Email enviado a {}", to);
        } catch (Exception e) {
            log.error("Error enviando email a {}: {}", to, e.getMessage());
        }
    }

    private void sendSms(String phone, String message) {
        try {
            PublishRequest request = PublishRequest.builder()
                    .phoneNumber(phone)
                    .message(message)
                    .build();

            snsClient.publish(request);
            log.info("SMS enviado a {}", phone);
        } catch (Exception e) {
            log.error("Error enviando SMS a {}: {}", phone, e.getMessage());
        }
    }
}
