package com.example.iq300.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String subject; // 질문 제목

    @Column(columnDefinition = "TEXT")
    private String content; // 질문 내용

    private LocalDateTime createDate; // 생성일시

    @ManyToOne
    private User author; // 질문자

    // (중요) 질문 1개에 답변 N개가 달림
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
}