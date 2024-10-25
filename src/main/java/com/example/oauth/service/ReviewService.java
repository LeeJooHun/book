package com.example.oauth.service;

import com.example.oauth.dto.ReviewDto;
import com.example.oauth.entity.Book;
import com.example.oauth.entity.Review;
import com.example.oauth.entity.UserEntity;
import com.example.oauth.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<ReviewDto> findAllReverse(){
        List<Review> reviewList = reviewRepository.findAll();
        Collections.reverse(reviewList);
        List<ReviewDto> reviewDtoList = new ArrayList<>();
        for(Review review : reviewList){
            reviewDtoList.add(review.toDto(review.getBook().getIsbn(), review.getBook().getTitle(), review.getBook().getImage()));
        }
        return reviewDtoList;
    }

    public Page<ReviewDto> findAllReverse(int page) {
        // 페이지 인덱스를 1부터 시작하도록 변환
        int adjustedPage = page > 0 ? page - 1 : 0;

        List<Review> reviewList = reviewRepository.findAllByOrderByIdDesc();

        // Review를 ReviewDto로 변환
        List<ReviewDto> reviewDtoList = new ArrayList<>();
        for (Review review : reviewList) {
            reviewDtoList.add(review.toDto(review.getBook().getIsbn(), review.getBook().getTitle(), review.getBook().getImage()));
        }

        // 페이지 처리
        Pageable pageable = PageRequest.of(adjustedPage, 8);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reviewDtoList.size());

        return new PageImpl<>(reviewDtoList.subList(start, end), pageable, reviewDtoList.size());
    }


    public List<ReviewDto> findRecentFourReviews() {
        List<Review> reviewList = reviewRepository.findAll();
        Collections.reverse(reviewList);

        List<Review> recentFourReviews = reviewList.stream()
                .limit(4)
                .collect(Collectors.toList());

        List<ReviewDto> reviewDtoList = recentFourReviews.stream()
                .map(review -> review.toDto(review.getBook().getIsbn(), review.getBook().getTitle(), review.getBook().getImage()))
                .collect(Collectors.toList());

        return reviewDtoList;
    }


    public List<ReviewDto> findByUserReverse(UserEntity user){
        List<Review> reviewList = reviewRepository.findByUser(user);
        Collections.reverse(reviewList);

        List<ReviewDto> reviewDtoList = new ArrayList<>();
        for(Review review : reviewList){
            reviewDtoList.add(review.toDto(review.getBook().getIsbn(), review.getBook().getTitle(), review.getBook().getImage()));
        }

        return reviewDtoList;
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

    public String calculateAverageRating(List<Review> reviews) {
        double average = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String formattedRating = decimalFormat.format(average);
        return formattedRating;
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