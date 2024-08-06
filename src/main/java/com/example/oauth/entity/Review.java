package com.example.oauth.entity;

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
    private String isbn;

    @Column
    private String content;

    @Column
    private int rating;

    @Column
    private String title;

    @Column
    private String image;

    @Column
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @PrePersist
    protected void onCreate() {
        date = LocalDate.now();
    }

}
