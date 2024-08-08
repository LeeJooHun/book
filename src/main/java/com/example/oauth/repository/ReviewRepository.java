package com.example.oauth.repository;

import com.example.oauth.entity.Review;
import com.example.oauth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Override
    ArrayList<Review> findAll();

    ArrayList<Review> findByUser(UserEntity user);
}
