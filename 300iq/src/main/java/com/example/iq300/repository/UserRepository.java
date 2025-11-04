package com.example.iq300.repository;

import com.example.iq300.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Optional 임포트

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Spring Data JPA가 메서드 이름을 보고 쿼리를 자동 생성합니다.
    // 1. 이메일(로그인 ID)로 사용자를 찾는 메서드 (로그인 시 필요)
    Optional<User> findByEmail(String email);

    // 2. 닉네임으로 사용자를 찾는 메서드 (닉네임 중복 체크 시 필요)
    Optional<User> findByNickname(String nickname);
}