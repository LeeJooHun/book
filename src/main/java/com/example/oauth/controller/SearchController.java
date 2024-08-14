package com.example.oauth.controller;

import com.example.oauth.entity.UserEntity;
import com.example.oauth.service.SearchService;
import com.example.oauth.service.UserService;
import com.example.oauth.vo.BookVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class SearchController {

    private final SearchService searchService;
    private final UserService userService;

    @GetMapping("/books")
    public String list(String text, Model model) {
        List<BookVO> bookList = searchService.bookSearch(text);
        model.addAttribute("bookList", bookList);

        UserEntity user = userService.getUser();
        model.addAttribute("user", user);

        return "list";
    }

}