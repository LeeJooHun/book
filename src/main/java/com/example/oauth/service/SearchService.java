package com.example.oauth.service;

import com.example.oauth.entity.Book;
import com.example.oauth.repository.BookRepository;
import com.example.oauth.vo.BookVO;
import com.example.oauth.vo.NaverResultVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final BookRepository bookRepository;

    public List<BookVO> bookSearch(String text){
        // 네이버 검색 API 요청
        String clientId = "cQqIhvcu4psbNYozwHT3";
        String clientSecret = "73Qj8bU7lB";

        //String apiURL = "https://openapi.naver.com/v1/search/blog?query=" + text;    // JSON 결과
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/book.json")
                .queryParam("query", text)
                .queryParam("display", 10)
                .queryParam("start", 1)
                .queryParam("sort", "sim")
                .encode()
                .build()
                .toUri();

        // Spring 요청 제공 클래스
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .build();
        // Spring 제공 restTemplate
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.exchange(req, String.class);

        // JSON 파싱 (Json 문자열을 객체로 만듦, 문서화)
        ObjectMapper om = new ObjectMapper();
        NaverResultVO resultVO = null;

        try {
            resultVO = om.readValue(resp.getBody(), NaverResultVO.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return resultVO.getItems();	// books를 list.html에 출력 -> model 선언
    }

    public BookVO getBookByIsbn(String isbn){
        List<BookVO> books = bookSearch(isbn);
        BookVO book = books.get(0);
        String bookDescription = book.getDescription().replace("\n", "<br>");
        book.setDescription(bookDescription);
        return book;
    }

    @Transactional
    public boolean setBookAsBookClub(String isbn) {
        Book book = bookRepository.findByIsbn(isbn);
        if(book == null) {
            BookVO searchBook = getBookByIsbn(isbn);
            book = new Book(null, isbn, searchBook.getTitle(), searchBook.getImage(), 0, 0, true, null);
        }
        book.setBookClub(true);
        bookRepository.save(book);

        return true;
    }



}