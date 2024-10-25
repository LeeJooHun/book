package com.example.oauth.service;

import com.example.oauth.dto.ReviewDto;
import com.example.oauth.entity.*;
import com.example.oauth.repository.BookRepository;
import com.example.oauth.repository.HeartRepository;
import com.example.oauth.vo.BookVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final HeartRepository heartRepository;

    public Book findByIsbn(String isbn){
        return bookRepository.findByIsbn(isbn);
    }

    public List<Review> getReviewList(String isbn, UserEntity user){
        Book book = findByIsbn(isbn);
        List<Review> reviewList = new ArrayList<>();
        if(book != null)
            reviewList = book.getReviews();

        for(Review review : reviewList){
            Heart heart = heartRepository.findByUserAndReview(user, review);
            if(heart != null)
                review.setHeartClicked(true);
            review.setHeartSize(review.getHearts().size());

            if(user == null)
                review.setOwner(false);
            else {
                review.setOwner(user.getId() == review.getUser().getId());
                List<Comment> comments = review.getComments();
                for(Comment comment : comments){
                    if(user.getId() == comment.getUser().getId())
                        comment.setOwner(true);
                }
                review.setComments(comments);
            }
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

    public List<Book> findFourBooksByAverageDesc() {
        List<Book> bookList = bookRepository.findAll();
        Collections.sort(bookList, Comparator.comparingDouble(Book::getAverage).reversed());

        List<Book> recentFourReviews = bookList.stream()
                .limit(4)
                .collect(Collectors.toList());

        return recentFourReviews;
    }

    public List<Book> findFourClubBooks() {
        List<Book> clubBookList = bookRepository.findByBookClubTrue();
        Collections.reverse(clubBookList);

        List<Book> recentFourBooks = clubBookList.stream()
                .limit(4)
                .collect(Collectors.toList());
        return recentFourBooks;
    }

    public Page<Book> findBooksByAverageDesc(int page) {

        int adjustedPage = page > 0 ? page - 1 : 0;

        List<Book> bookList = bookRepository.findAll();
        Collections.sort(bookList, Comparator.comparingDouble(Book::getAverage).reversed());

        // 페이지 처리
        Pageable pageable = PageRequest.of(adjustedPage, 8);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), bookList.size());

        return new PageImpl<>(bookList.subList(start, end), pageable, bookList.size());
    }

    public Page<Book> findBooksByBookClub(int page) {

        int adjustedPage = page > 0 ? page - 1 : 0;

        List<Book> bookList = bookRepository.findByBookClubTrue();
        Collections.reverse(bookList);

        // 페이지 처리
        Pageable pageable = PageRequest.of(adjustedPage, 8);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), bookList.size());

        return new PageImpl<>(bookList.subList(start, end), pageable, bookList.size());
    }

}
