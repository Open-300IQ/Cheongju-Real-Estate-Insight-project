package com.example.iq300.service;

import com.example.iq300.domain.User;
import com.example.iq300.domain.UserRole;
import com.example.iq300.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // SecurityConfig에서 만든 암호화 객체

    /**
     * FR-001 회원가입 기능
     */
    public User createUser(String email, String nickname, String password) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setNickname(nickname);
        
        // 비밀번호는 반드시 암호화해서 저장해야 합니다.
        newUser.setPassword(passwordEncoder.encode(password)); 
        
        
        newUser.setRole(UserRole.ROLE_USER);
        
        userRepository.save(newUser);
        return newUser;
    }
    
 // ▼▼▼▼▼ 4. (FR-018) 인증 신청 메서드 추가 ▼▼▼▼▼
    /**
     * 사용자의 권한을 '인증대기중'으로 변경 (인증 신청)
     */
    public void applyForVerification(String email) {
        Optional<User> _user = userRepository.findByEmail(email);
        if (_user.isEmpty()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        User user = _user.get();
        
        // 이미 신청했거나 인증된 상태가 아니면 '인증대기중'으로 변경
        if (user.getRole() == UserRole.ROLE_USER) {
            user.setRole(UserRole.ROLE_PENDING);
            userRepository.save(user);
        }
    }
}