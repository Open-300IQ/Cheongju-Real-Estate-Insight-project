package com.example.iq300.service;

import com.example.iq300.domain.User;
import com.example.iq300.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;


import java.util.ArrayList; // ArrayList 임포트
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // "Username"으로 이메일을 사용합니다.
        Optional<User> _user = userRepository.findByEmail(email);
        if (_user.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        User user = _user.get();
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        
        

        // Spring Security가 사용하는 UserDetails 객체로 변환하여 반환
        // (여기서는 간단하게 사용자 이메일, 비밀번호, 권한(빈 리스트)만 반환)
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(), 
            user.getPassword(), 
            authorities
        );
    }
}