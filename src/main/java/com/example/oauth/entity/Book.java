package com.example.oauth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String isbn;

    @Column
    private String title;

    @Column
    private String image;

    @Column
    private double average;

    @Column
    private int ratingCount;

    @OneToMany(mappedBy = "book")
    private List<Review> reviews;
}
