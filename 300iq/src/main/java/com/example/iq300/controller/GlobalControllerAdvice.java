package com.example.iq300.controller;

import com.example.iq300.domain.Notification;
import com.example.iq300.domain.User;
import com.example.iq300.service.NotificationService;
import com.example.iq300.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;
    private final NotificationService notificationService;


    @ModelAttribute
    public void handleNotification(Model model, Principal principal) {
        if (principal != null) {
            User user = userService.getUser(principal.getName());
            List<Notification> notifications = notificationService.getUncheckedNotifications(user);
            model.addAttribute("uncheckedNoti", notifications);
        }
    }
}