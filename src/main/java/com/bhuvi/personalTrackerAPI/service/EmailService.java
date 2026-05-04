package com.bhuvi.personalTrackerAPI.service;

import com.bhuvi.personalTrackerAPI.constant.MailTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    private final RestClient restClient = RestClient.create();

    public void sendEmail(String toEmail, String subject, String otp) {
        Map<String, Object> requestBody = Map.of(
                "sender", Map.of("name", "Personal Tracker", "email", senderEmail),
                "to", List.of(Map.of("email", toEmail)),
                "subject", subject,
                "htmlContent", MailTemplate.signupVerificationTemplate.formatted(otp)
        );

        restClient.post()
                .uri("https://api.brevo.com/v3/smtp/email")
                .header("api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .toBodilessEntity();
    }

}