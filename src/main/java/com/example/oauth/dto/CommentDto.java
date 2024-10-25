package com.example.oauth.dto;

import com.example.oauth.entity.Book;
import com.example.oauth.entity.Comment;
import com.example.oauth.entity.Review;
import com.example.oauth.entity.UserEntity;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class CommentDto {
    private Long id;
    private String content;
    private Long reviewId;
}
