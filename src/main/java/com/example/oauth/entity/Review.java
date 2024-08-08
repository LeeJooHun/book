package com.example.oauth.entity;

import com.example.oauth.dto.ReviewDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @Column
    private int rating;

    @Column
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @PrePersist
    protected void onCreate() {
        date = LocalDate.now();
    }

    public ReviewDto toDto(String isbn, String title, String image){
        return new ReviewDto(id, isbn, content, rating, title, image, user);
    }

}
