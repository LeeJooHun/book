package com.example.oauth.service;

import com.example.oauth.dto.CustomOAuth2User;
import com.example.oauth.entity.Image;
import com.example.oauth.entity.UserEntity;
import com.example.oauth.repository.ImageRepository;
import com.example.oauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserRepository userRepository;

    public Image saveImage(MultipartFile file) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 현재 인증된 사용자의 정보를 가져오는 예시
        Object principal = authentication.getPrincipal();
        CustomOAuth2User userDetails = (CustomOAuth2User)principal;
        String username = userDetails.getUsername();
        System.out.println(username);
        UserEntity user = userRepository.findByUsername(username);

        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setData(file.getBytes());
        user.setProfileImage(image);

        userRepository.save(user);
        return image;
    }

    public Image getImage(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        return imageRepository.findById(user.getProfileImage().getId()).orElse(null);
    }
}