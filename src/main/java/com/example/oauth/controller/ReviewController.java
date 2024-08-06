package com.example.oauth.controller;

import com.example.oauth.dto.ReviewDto;
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
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ReviewController {

    private final UserService userService;

    private final SearchService searchService;

    private final ReviewService reviewService;

    @GetMapping("/review")
    public String reviewP(){
        return "review";
    }

    @GetMapping("/review/{isbn}")
    public String reviewPage(@PathVariable String isbn, Model model){
        List<Review> reviews = reviewService.findByIsbn(isbn);
        int ratings[] = reviewService.calculateRatings(reviews);
        double averageRating = reviewService.calculateAverageRating(reviews);

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String formattedRating = decimalFormat.format(averageRating);

        BookVO book = searchService.getBookByIsbn(isbn);

        model.addAttribute("book", book);
        model.addAttribute("reviews", reviews);
        model.addAttribute("size", reviews.size());
        model.addAttribute("average", formattedRating);
        model.addAttribute("rating5", ratings[5]);
        model.addAttribute("rating4", ratings[4]);
        model.addAttribute("rating3", ratings[3]);
        model.addAttribute("rating2", ratings[2]);
        model.addAttribute("rating1", ratings[1]);
        model.addAttribute("rating0", ratings[0]);

        UserEntity user = userService.getUser();
        model.addAttribute("user", user);

        return "review";
    }

    @PostMapping("/review/create")
    public String createReview(ReviewDto reviewDto){
        Review review = reviewService.save(reviewDto);
        return "redirect:/review/" + review.getIsbn();
    }

    @PostMapping("/review/update")
    public String updateReview(ReviewDto reviewDto){
        Review review = reviewService.update(reviewDto);
        return "redirect:/review/" + review.getIsbn();
    }

    @PostMapping("/review/delete/{id}")
    public String deleteReview(@PathVariable Long id){
        Review review = reviewService.delete(id);
        return "redirect:/review/" + review.getIsbn();
    }
}
