package com.example.oauth.repository;

import com.example.oauth.entity.Book;
import com.example.oauth.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Override
    ArrayList<Book> findAll();

    Book findByIsbn(String isbn);
}
