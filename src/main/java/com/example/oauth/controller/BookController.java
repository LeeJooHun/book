package com.example.oauth.controller;

import com.example.oauth.service.CustomOAuth2UserService;
import com.example.oauth.service.SearchService;
import com.example.oauth.vo.BookVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BookController {

    private final SearchService searchService;

    @GetMapping("/books")
    public String list(String text, Model model) {
        List<BookVO> books = searchService.bookSearch(text);
        model.addAttribute("books", books);
        return "list";
    }

}