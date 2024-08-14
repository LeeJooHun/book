package com.example.oauth.service;

import com.example.oauth.dto.ReviewDto;
import com.example.oauth.entity.Book;
import com.example.oauth.entity.Review;
import com.example.oauth.entity.UserEntity;
import com.example.oauth.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    public Book findByIsbn(String isbn){
        return bookRepository.findByIsbn(isbn);
    }

    public List<Review> getReviewList(String isbn, UserEntity user){
        Book book = findByIsbn(isbn);
        List<Review> reviewList = new ArrayList<>();
        if(book != null)
            reviewList = book.getReviews();

        for(Review review : reviewList){
            if(user == null)
                review.setOwner(false);
            else
                review.setOwner(user.getId() == review.getUser().getId());
        }
        return reviewList;
    }

    @Transactional
    public Book save(ReviewDto reviewDto){
        Book book = bookRepository.findByIsbn(reviewDto.getIsbn());
        if(book != null){
            double sum = book.getAverage() * book.getRatingCount() + reviewDto.getRating();
            book.setRatingCount(book.getRatingCount() + 1);
            book.setAverage(sum / book.getRatingCount());
            bookRepository.save(book);
        }
        else{
            book = reviewDto.toBook();
            bookRepository.save(book);
        }
        return book;
    }

    @Transactional
    public void update(Review target, ReviewDto reviewDto){
        Book book = bookRepository.findByIsbn(reviewDto.getIsbn());
        double sum = book.getAverage() * book.getRatingCount() - target.getRating() + reviewDto.getRating();
        book.setAverage(sum / book.getRatingCount());
        bookRepository.save(book);
    }

    @Transactional
    public void delete(String isbn, Review target){
        Book book = bookRepository.findByIsbn(isbn);
        double sum = book.getAverage() * book.getRatingCount() - target.getRating();
        book.setRatingCount(book.getRatingCount() - 1);

        if(book.getRatingCount() == 0){
            bookRepository.delete(book);
        }
        else {
            book.setAverage(sum / book.getRatingCount());
        }
    }

    public List<Book> findBooksByAverageDesc() {
        List<Book> bookList = bookRepository.findAll();
        Collections.sort(bookList, Comparator.comparingDouble(Book::getAverage).reversed());
        return bookList;
    }


}
