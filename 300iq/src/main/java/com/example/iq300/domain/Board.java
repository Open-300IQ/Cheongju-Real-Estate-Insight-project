package com.example.iq300.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity // 이 클래스가 데이터베이스 테이블임을 나타냅니다.
public class Board {

    @Id // 기본 키(PK)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id;

    @Column(length = 200) // Varchar(200)
    private String title;

    @Column(columnDefinition = "TEXT") // TEXT 타입
    private String content;

    private String author; // 작성자 (추후 회원 F1과 연동)
    
    // (날짜 등은 나중에 추가)
}