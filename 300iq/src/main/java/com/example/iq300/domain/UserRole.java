package com.example.iq300.domain;

import lombok.Getter;

@Getter
public enum UserRole {
    // "KEY"는 Spring Security가 인식하는 이름, "value"는 화면에 표시할 이름
    ROLE_USER("일반사용자"),
    ROLE_PENDING("인증대기중"), // (FR-018: 인증 신청 상태)
    ROLE_BUSINESS("인증사업자"), // (FR-021: 인증 완료 상태)
    ROLE_ADMIN("관리자");

    private String value;

    UserRole(String value) {
        this.value = value;
    }
}