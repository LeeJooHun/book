package com.example.oauth.controller;

import com.example.oauth.entity.Image;
import com.example.oauth.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);
    private final ImageService imageService;

    @PostMapping("/upload")
    public RedirectView uploadImage(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        try {
            imageService.saveImage(file);
            attributes.addFlashAttribute("message", "Image uploaded successfully");
            return new RedirectView("/main/mypage", true);
        } catch (IOException e) {
            logger.error("Failed to upload image", e);
            attributes.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
            return new RedirectView("/uploadPage", true);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Image image = imageService.getImage(id);
        if (image == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        MediaType mediaType = MediaType.IMAGE_JPEG;
        if (image.getName().toLowerCase().endsWith(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (image.getName().toLowerCase().endsWith(".gif")) {
            mediaType = MediaType.IMAGE_GIF;
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                .contentType(mediaType)
                .body(image.getData());
    }
}
