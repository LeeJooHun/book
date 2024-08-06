package com.example.oauth.controller;

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

import java.util.List;

@RequiredArgsConstructor
@Controller
public class  MainController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final BookService bookService;

    @GetMapping("/main")
    public String mainPage(Model model) {
        List<Review> reviewList = reviewService.findAllReverse();
        model.addAttribute("reviewList", reviewList);

        List<Book> bookList = bookService.findBooksByAverageDesc();
        model.addAttribute("bookList", bookList);

        UserEntity user = userService.getUser();
        model.addAttribute("user", user);

        return "main";
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
