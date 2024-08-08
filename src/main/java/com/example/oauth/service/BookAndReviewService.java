package com.example.oauth.service;

import com.example.oauth.dto.ReviewDto;
import com.example.oauth.entity.Book;
import com.example.oauth.entity.Review;
import com.example.oauth.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookAndReviewService {

    private final BookService bookService;

    private final ReviewService reviewService;

    private final UserService userService;

    public String save(ReviewDto reviewDto) {
        UserEntity user = userService.getUser();
        if(user != null) {
            Book book = bookService.save(reviewDto);
            reviewDto.setUser(user);
            reviewService.save(reviewDto, book);
        }
        return reviewDto.getIsbn();
    }

    public String update(ReviewDto reviewDto){
        UserEntity user = userService.getUser();
        Review target = reviewService.findById(reviewDto.getId());
        if(user.equals(target.getUser())){
            bookService.update(target, reviewDto);
            reviewDto.setUser(user);
            reviewService.update(target, reviewDto);
        }
        return reviewDto.getIsbn();
    }

    public void delete(String isbn, Long id){
        UserEntity user = userService.getUser();
        Review target = reviewService.findById(id);
        if(user.equals(target.getUser())){
            reviewService.delete(target);
            bookService.delete(isbn, target);
        }
    }

}
