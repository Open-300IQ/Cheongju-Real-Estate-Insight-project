package com.example.iq300.service;

import com.example.iq300.domain.User;
import com.example.iq300.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional; // ======== [ 'Optional' 빨간 줄 해결 ] ========

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        
        // ======== [ isVerified 값을 true로 설정 (DB 오류 해결) ] ========
        user.setVerified(true); // 기본값 true로 설정
        
        this.userRepository.save(user);
        return user;
    }

    /**
     * 사용자 이름으로 사용자를 조회합니다.
     */
    public Optional<User> findUser(String username) {
        return this.userRepository.findByUsername(username);
    }
}