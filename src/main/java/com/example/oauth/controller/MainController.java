package com.example.oauth.controller;

import com.example.oauth.dto.PageDto;
import com.example.oauth.dto.ReviewDto;
import com.example.oauth.entity.Book;
import com.example.oauth.entity.Review;
import com.example.oauth.entity.UserEntity;
import com.example.oauth.service.*;
import com.example.oauth.vo.BookVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Controller
public class  MainController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final BookService bookService;
    private final PageService pageService;

    @GetMapping("/main")
    public String mainPage(Model model) {
        List<ReviewDto> reviewDtoList = reviewService.findRecentFourReviews();
        model.addAttribute("reviewDtoList", reviewDtoList);

        List<Book> bookList = bookService.findFourBooksByAverageDesc();
        model.addAttribute("bookList", bookList);

        List<Book> clubBookList = bookService.findFourClubBooks();
        model.addAttribute("clubBookList", clubBookList);

        UserEntity user = userService.getUser();
        model.addAttribute("user", user);

        return "main";
    }

    @GetMapping("/recent-reviews")
    public String recentReviewPage(Model model, @RequestParam(value="page", defaultValue="1") int page) {
        Page<ReviewDto> reviewDtoList = reviewService.findAllReverse(page);
        model.addAttribute("reviewDtoList", reviewDtoList);

        List<PageDto> pageDtoList= pageService.getPageDtoList(reviewDtoList.getTotalPages(), page);
        model.addAttribute("pageDtoList", pageDtoList);
        model.addAttribute("lastPage", reviewDtoList.getTotalPages());

        UserEntity user = userService.getUser();
        model.addAttribute("user", user);

        return "recentReviews";
    }

    @GetMapping("/high-reviews")
    public String highReviewPage(Model model, @RequestParam(value="page", defaultValue="1") int page){
        Page<Book> bookList = bookService.findBooksByAverageDesc(page);
        model.addAttribute("bookList", bookList);

        List<PageDto> pageDtoList= pageService.getPageDtoList(bookList.getTotalPages(), page);
        model.addAttribute("pageDtoList", pageDtoList);
        model.addAttribute("lastPage", bookList.getTotalPages());

        UserEntity user = userService.getUser();
        model.addAttribute("user", user);

        return "highReviews";
    }

    @GetMapping("/bookClub")
    public String bookClubPage(Model model, @RequestParam(value="page", defaultValue="1") int page){
        Page<Book> bookList = bookService.findBooksByBookClub(page);
        model.addAttribute("bookList", bookList);

        List<PageDto> pageDtoList= pageService.getPageDtoList(bookList.getTotalPages(), page);
        model.addAttribute("pageDtoList", pageDtoList);
        model.addAttribute("lastPage", bookList.getTotalPages());

        UserEntity user = userService.getUser();
        model.addAttribute("user", user);

        return "bookClub";
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
