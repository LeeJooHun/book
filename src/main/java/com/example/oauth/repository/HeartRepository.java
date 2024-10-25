package com.example.oauth.repository;

import com.example.oauth.entity.Heart;
import com.example.oauth.entity.Review;
import com.example.oauth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    ArrayList<Heart> findByReview(Review review);

    Heart findByUser(UserEntity user);

    Heart findByUserAndReview(UserEntity user, Review review);
}
