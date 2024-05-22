package com.nucleus.picpay.services;

import com.nucleus.picpay.domain.user.User;
import com.nucleus.picpay.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${picpay.notificationURL}")
    private String notificationURL;

    public void sendNotification(User user, String message) throws Exception {
        NotificationDTO notificationRequest = new NotificationDTO(user.getEmail(), message);

        ResponseEntity<String> notificationResponse = restTemplate.postForEntity(this.notificationURL, notificationRequest, String.class);

        if(!(notificationResponse.getStatusCode() == HttpStatus.OK)) throw new Exception("Notification service not working");
    }
}
