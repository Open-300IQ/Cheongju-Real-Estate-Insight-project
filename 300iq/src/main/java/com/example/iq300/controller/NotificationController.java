package com.example.iq300.controller;

import com.example.iq300.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class NotificationController {

    private final NotificationService notificationService;

    // 알림 클릭 시 실행: 읽음 처리 후 해당 URL로 리다이렉트
    @GetMapping("/notification/read/{id}")
    public String readNotification(@PathVariable("id") Long id) {
        String url = notificationService.readNotification(id);
        return "redirect:" + url; 
    }
}