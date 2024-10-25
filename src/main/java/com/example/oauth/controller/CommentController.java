package com.example.oauth.controller;

import com.example.oauth.dto.CommentDto;
import com.example.oauth.repository.CommentRepository;
import com.example.oauth.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/create/{isbn}")
    public String createComment(@PathVariable String isbn, CommentDto commentDto) {
        commentService.create(commentDto);
        return "redirect:/review/" + isbn;
    }

    @PostMapping("/comment/update/{isbn}")
    public String updateComment(@PathVariable String isbn, CommentDto commentDto) {
        commentService.update(commentDto);
        return "redirect:/review/" + isbn;
    }

    @PostMapping("/comment/delete/{isbn}/{id}")
    public String deleteComment(@PathVariable String isbn, @PathVariable Long id){
        commentService.delete(id);
        return "redirect:/review/" + isbn;
    }

}
