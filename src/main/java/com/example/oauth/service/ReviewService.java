package com.example.oauth.service;

import com.example.oauth.dto.ReviewDto;
import com.example.oauth.entity.Book;
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

    public List<Review> findAllReverse(){
        List<Review> reviewList = reviewRepository.findAll();
        Collections.reverse(reviewList);
        return reviewList;
    }


    public List<Review> findByUserReverse(UserEntity user){
        List<Review> reviewList = reviewRepository.findByUser(user);
        Collections.reverse(reviewList);
        return reviewList;
    }

    public Review findById(Long id){
        return reviewRepository.findById(id).orElse(null);
    }


    public Review save(ReviewDto reviewDto, Book book){
        Review review = reviewDto.toReview();
        review.setBook(book);
        reviewRepository.save(review);
        return review;
    }

    public Review update(Review target, ReviewDto reviewDto){
        Review review = reviewDto.toReview();
        review.setBook(target.getBook());
        reviewRepository.save(review);
        return review;
    }

    public void delete(Review target){
        reviewRepository.delete(target);
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