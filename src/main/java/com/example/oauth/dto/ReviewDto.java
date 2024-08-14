package com.example.oauth.dto;

import com.example.oauth.entity.Book;
import com.example.oauth.entity.Review;
import com.example.oauth.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class ReviewDto {

    private Long id;
    private String isbn;
    private String content;
    private int rating;
    private String title;
    private String image;
    private UserEntity user;

    public Review toReview(){
        return new Review(id, content, rating, LocalDate.now(), user, null, false);
    }

    public Book toBook(){
        return new Book(null, isbn, title, image, rating, 1, null);
    }
}
