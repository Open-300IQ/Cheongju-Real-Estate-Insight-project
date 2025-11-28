package com.example.iq300.service;

import com.example.iq300.domain.Notification;
import com.example.iq300.domain.User;
import com.example.iq300.repository.NotificationRepository;
import com.example.iq300.exception.DataNotFoundException; // 예외처리를 위해 필요
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 트랜잭션 추가

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void create(User user, String message, String url) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setUrl(url);
        notification.setChecked(false);
        notification.setCreateDate(LocalDateTime.now());
        this.notificationRepository.save(notification);
    }
    
    public List<Notification> getUncheckedNotifications(User user) {
        return notificationRepository.findByUserAndCheckedFalseOrderByIdDesc(user);
    }

    // [추가된 메서드] 알림 읽음 처리 및 URL 반환
    @Transactional
    public String readNotification(Long id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        if(notification.isPresent()) {
            Notification noti = notification.get();
            noti.setChecked(true); // 읽음 처리
            notificationRepository.save(noti);
            return noti.getUrl(); // 이동할 URL 반환
        }
        return "/"; // 알림이 없으면 메인으로
    }
}