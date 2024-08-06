package com.example.oauth.service;

import com.example.oauth.dto.ReviewDto;
import com.example.oauth.entity.Book;
import com.example.oauth.entity.Review;
import com.example.oauth.repository.BookRepository;
import com.example.oauth.vo.BookVO;
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

    @Transactional
    public void save(Review review){
        Book book = bookRepository.findByIsbn(review.getIsbn());
        if(book != null){
            double sum = book.getAverage() * book.getRatingCount() + review.getRating();
            book.setRatingCount(book.getRatingCount() + 1);
            book.setAverage(sum / book.getRatingCount());
            bookRepository.save(book);
        }
        else{
            book = new Book(null, review.getIsbn(), review.getTitle(), review.getImage(), review.getRating(), 1);
            bookRepository.save(book);
        }
    }

    @Transactional
    public void update(Review target, Review review){
        Book book = bookRepository.findByIsbn(review.getIsbn());
        double sum = book.getAverage() * book.getRatingCount() - target.getRating() + review.getRating();
        book.setAverage(sum / book.getRatingCount());
        bookRepository.save(book);
    }

    @Transactional
    public void delete(Review review){
        Book book = bookRepository.findByIsbn(review.getIsbn());
        double sum = book.getAverage() * book.getRatingCount() - review.getRating();
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
