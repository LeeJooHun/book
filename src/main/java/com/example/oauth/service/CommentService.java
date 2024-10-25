package com.example.oauth.service;

import com.example.oauth.dto.CommentDto;
import com.example.oauth.entity.Comment;
import com.example.oauth.entity.Review;
import com.example.oauth.entity.UserEntity;
import com.example.oauth.repository.CommentRepository;
import com.example.oauth.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ReviewRepository reviewRepository;

    public void create(CommentDto commentDto) {
        
    }



    public void delete(Long id){
        Comment comment = commentRepository.findById(id).orElse(null);
        commentRepository.delete(comment);
    }


    public void update(CommentDto commentDto) {
    }
}
