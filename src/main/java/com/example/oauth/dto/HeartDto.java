package com.example.oauth.dto;

import com.example.oauth.entity.Review;
import com.example.oauth.entity.UserEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class HeartDto {

    private Long reviewId;

    private UserEntity user;
}
