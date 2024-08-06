package com.example.oauth.service;

import com.example.oauth.dto.ReviewDto;
import com.example.oauth.entity.Review;
import com.example.oauth.entity.UserEntity;
import com.example.oauth.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserService userService;

    private final BookService bookService;

    public List<Review> findAllReverse(){
        List<Review> reviewList = reviewRepository.findAll();
        Collections.reverse(reviewList);
        return reviewList;
    }

    public List<Review> findByIsbn(String isbn){
        List<Review> reviewList = reviewRepository.findByIsbn(isbn);
        return reviewList;
    }

    public List<Review> findByUserReverse(UserEntity user){
        List<Review> reviewList = reviewRepository.findByUser(user);
        Collections.reverse(reviewList);
        return reviewList;
    }


    public Review save(ReviewDto reviewDto){
        Review review = reviewDto.toEntity();
        UserEntity user = userService.getUser();
        if(user != null) {
            review.setUser(user);
            reviewRepository.save(review);
            bookService.save(review);
        }
        return review;
    }

    public Review update(ReviewDto reviewDto){
        Review review = reviewDto.toEntity();
        Review target = reviewRepository.findById(review.getId()).orElse(null);
        UserEntity user = userService.getUser();
        if(target.getUser().equals(user)) {
            review.setUser(user);
            reviewRepository.save(review);
            bookService.update(target, review);
        }
        return review;
    }

    public Review delete(Long id){
        Review review = reviewRepository.findById(id).orElse(null);
        UserEntity user = userService.getUser();
        if(review.getUser().equals(user)) {
            reviewRepository.delete(review);
            bookService.delete(review);
        }
        return review;
    }

    public Map<Integer, Long> calculateRatingDistribution(List<Review> reviews) {
        return reviews.stream()
                .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));
    }

    public double calculateAverageRating(List<Review> reviews) {
        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public int[] calculateRatings(List<Review> reviews) {
        int[] ratings = new int[6];
        for (Review review : reviews) {
            if (review.getRating() >= 0 && review.getRating() < ratings.length) {
                ratings[review.getRating()]++;
            }
        }
        return ratings;
    }
}