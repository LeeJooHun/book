package com.example.oauth.controller;

import com.example.oauth.dto.ReviewDto;
import com.example.oauth.entity.Book;
import com.example.oauth.entity.Review;
import com.example.oauth.entity.UserEntity;
import com.example.oauth.service.*;
import com.example.oauth.vo.BookVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ReviewController {

    private final UserService userService;
    private final SearchService searchService;
    private final BookService bookService;
    private final ReviewService reviewService;
    private final BookAndReviewService bookAndReviewService;

    @GetMapping("/review/{isbn}")
    public String reviewPage(@PathVariable String isbn, Model model){

        BookVO searchBook = searchService.getBookByIsbn(isbn);
        model.addAttribute("book", searchBook);

        UserEntity user = userService.getUser();
        model.addAttribute("user", user);

        List<Review> reviewList = bookService.getReviewList(isbn, user);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("size", reviewList.size());

        int ratings[] = reviewService.calculateRatings(reviewList);
        model.addAttribute("rating5", ratings[5]);
        model.addAttribute("rating4", ratings[4]);
        model.addAttribute("rating3", ratings[3]);
        model.addAttribute("rating2", ratings[2]);
        model.addAttribute("rating1", ratings[1]);
        model.addAttribute("rating0", ratings[0]);

        String average = reviewService.calculateAverageRating(reviewList);
        model.addAttribute("average", average);

        return "review";
    }

    @PostMapping("/review/create")
    public String createReview(ReviewDto reviewDto){
        String isbn = bookAndReviewService.save(reviewDto);
        return "redirect:/review/" + isbn;
    }

    @PostMapping("/review/update")
    public String updateReview(ReviewDto reviewDto){
        String isbn = bookAndReviewService.update(reviewDto);
        return "redirect:/review/" + isbn;
    }

    @PostMapping("/review/delete/{isbn}/{id}")
    public String deleteReview(@PathVariable String isbn, @PathVariable Long id){
        bookAndReviewService.delete(isbn, id);
        return "redirect:/review/" + isbn;
    }
}
