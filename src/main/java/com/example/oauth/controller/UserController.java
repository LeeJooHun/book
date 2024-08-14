package com.example.oauth.controller;

import com.example.oauth.dto.ReviewDto;
import com.example.oauth.entity.Review;
import com.example.oauth.entity.UserEntity;
import com.example.oauth.service.ReviewService;
import com.example.oauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;

    @GetMapping("/main/mypage")
    public String check() {
        UserEntity user = userService.getUser();
        if (user == null) {
            return "redirect:/login";
        }
        return "redirect:/mypage/" + user.getId();
    }

    @GetMapping("/mypage/{id}")
    public String myPage(@PathVariable Long id, Model model){
        UserEntity user = userService.findById(id);
        model.addAttribute("user", user);

        boolean chk = userService.check(user);
        model.addAttribute("chk", chk);

        List<ReviewDto> reviewDtoList = reviewService.findByUserReverse(user);
        model.addAttribute("reviewDtoList", reviewDtoList);
        model.addAttribute("size", reviewDtoList.size());

        return "mypage";
    }

}
