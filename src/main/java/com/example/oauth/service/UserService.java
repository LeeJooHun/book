package com.example.oauth.service;

import com.example.oauth.dto.CustomOAuth2User;
import com.example.oauth.entity.UserEntity;
import com.example.oauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserEntity getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            // 인증된 사용자가 없거나 인증이 되지 않은 경우 null 반환
            return null;
        }

        Object principal = authentication.getPrincipal();

        // principal 객체가 CustomOAuth2User 타입인지 확인
        if (!(principal instanceof CustomOAuth2User)) {
            // 예상한 타입이 아닌 경우 null 반환
            return null;
        }

        CustomOAuth2User userDetails = (CustomOAuth2User) principal;
        String username = userDetails.getUsername();
        UserEntity user = userRepository.findByUsername(username);
        return user;
    }

    public UserEntity findById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public boolean check(UserEntity user){
        UserEntity i = getUser();
        boolean chk = true;
        if(i != user)
            chk = false;
        return chk;
    }

}
