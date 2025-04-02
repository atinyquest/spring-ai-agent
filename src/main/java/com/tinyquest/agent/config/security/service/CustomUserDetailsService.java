package com.tinyquest.agent.config.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // DB에서 사용자 조회 (테스트용으로 하드코딩)
        if ("admin".equals(username)) {
            return User.withUsername("admin")
                    .password("{noop}password") // {noop}은 암호화 안 된 비밀번호 의미
                    .roles("ADMIN")
                    .build();
        }

        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
    }
}
