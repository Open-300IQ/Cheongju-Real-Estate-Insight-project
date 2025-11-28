package com.example.iq300.repository;

import com.example.iq300.domain.Notification;
import com.example.iq300.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // 특정 사용자의 읽지 않은 알림만 조회
    List<Notification> findByUserAndCheckedFalseOrderByIdDesc(User user);
    
    // 특정 사용자의 모든 알림 조회
    List<Notification> findByUserOrderByIdDesc(User user);
}