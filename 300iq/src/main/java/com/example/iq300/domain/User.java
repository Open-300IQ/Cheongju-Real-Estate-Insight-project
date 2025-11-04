package com.example.iq300.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
// User는 데이터베이스 예약어인 경우가 많아, @Table(name="users")로 
// 실제 테이블 이름을 'users'로 지정해주는 것이 안전합니다.
@jakarta.persistence.Table(name = "users") 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false) // 이메일(ID)은 중복되면 안 됨
    private String email;

    @Column(nullable = false)
    private String password; // 실제로는 암호화해서 저장해야 함 (2단계에서 처리)

    @Column(unique = true, nullable = false) // 닉네임도 중복되면 안 됨
    private String nickname;


    
    @Enumerated(EnumType.STRING) // Enum의 이름을 DB에 문자열로 저장
    @Column(nullable = false)
    private UserRole role;
}