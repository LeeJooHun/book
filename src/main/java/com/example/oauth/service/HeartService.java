package com.example.oauth.service;

import com.example.oauth.dto.HeartDto;
import com.example.oauth.entity.Heart;
import com.example.oauth.entity.Review;
import com.example.oauth.repository.HeartRepository;
import com.example.oauth.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final ReviewRepository reviewRepository;
    private final UserService userService;

    public void saveOrDeleteHeart(HeartDto heartDto){
        Heart heart = heartRepository.findByUser(userService.getUser());
        if(heart == null)
            saveHeart(heartDto);
        else
            deleteHeart();
    }

    public void saveHeart(HeartDto heartDto) {
        Heart heart = new Heart();
        Review review = reviewRepository.findById(heartDto.getReviewId()).orElse(null);
        heart.setReview(review);
        heart.setUser(userService.getUser());
        heartRepository.save(heart);
    }

    public void deleteHeart() {
        Heart heart = heartRepository.findByUser(userService.getUser());
        heartRepository.delete(heart);
    }
}
