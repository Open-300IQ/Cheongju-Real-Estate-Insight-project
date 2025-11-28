package com.example.iq300.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user; // 알림을 받는 사람 (글 작성자)

    private String message; // 알림 내용 (예: "새로운 답변이 달렸습니다.")

    private String url; // 클릭 시 이동할 링크 (예: /question/detail/1)

    private boolean checked; // 알림 확인 여부 (읽었으면 true)

    private LocalDateTime createDate;
}