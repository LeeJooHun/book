package com.example.oauth.controller;

import com.example.oauth.dto.HeartDto;
import com.example.oauth.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/heart")
    public ResponseEntity<?> saveOrDeleteHeart(@RequestBody HeartDto heartDto) {
        heartService.saveOrDeleteHeart(heartDto);
        return ResponseEntity.ok("Heart saved successfully");
    }


}
