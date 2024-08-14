package com.example.oauth.controller;

import com.example.oauth.dto.ReviewDto;
import com.example.oauth.entity.Book;
import com.example.oauth.entity.Review;
import com.example.oauth.entity.UserEntity;
import com.example.oauth.service.BookService;
import com.example.oauth.service.ReviewService;
import com.example.oauth.service.SearchService;
import com.example.oauth.service.UserService;
import com.example.oauth.vo.BookVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class  MainController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final BookService bookService;

    @GetMapping("/main")
    public String mainPage(Model model) {
        List<ReviewDto> reviewDtoList = reviewService.findRecentFourReviews();
        model.addAttribute("reviewDtoList", reviewDtoList);

        List<Book> bookList = bookService.findBooksByAverageDesc();
        model.addAttribute("bookList", bookList);

        UserEntity user = userService.getUser();
        model.addAttribute("user", user);

        return "main";
    }

    @GetMapping("/recent-reviews")
    public String recentReviewPage(Model model){
        List<ReviewDto> reviewDtoList = reviewService.findAllReverse();
        model.addAttribute("reviewDtoList", reviewDtoList);

        UserEntity user = userService.getUser();
        model.addAttribute("user", user);

        return "recentReviews";
    }

    @GetMapping("/")
    public String redirect() {
        return "redirect:/main";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
