package com.example.oauth.dto;

import com.example.oauth.entity.Review;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@ToString
public class ReviewDto {

    private Long id;
    private String isbn;
    private String content;
    private int rating;
    private String title;
    private String image;

    public Review toEntity(){
        return new Review(id, isbn, content, rating, title, image, LocalDate.now(), null);
    }
}
