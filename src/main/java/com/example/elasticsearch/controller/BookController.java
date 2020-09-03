package com.example.elasticsearch.controller;

import com.example.elasticsearch.model.request.BookSaveRequest;
import com.example.elasticsearch.model.response.BookResponse;
import com.example.elasticsearch.service.BookService;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BookController {

    private final BookService bookService;
    Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public void generateBooks() {
        int index = 0;
        while (index < 100) {
            BookSaveRequest bookSaveRequest = new BookSaveRequest();
            bookSaveRequest.setAuthor(RandomStringUtils.randomAlphabetic(1, 25));
            bookSaveRequest.setTitle(RandomStringUtils.randomAlphabetic(2, 25));
            bookSaveRequest.setId(Integer.toString(index));
            bookSaveRequest.setReleaseDate(Long.toString(DateTimeUtils.currentTimeMillis()));
            index++;
            bookService.save(bookSaveRequest);
            LOGGER.info("index {}, title : {}", index, bookSaveRequest.getTitle());
        }
    }

    @GetMapping("find-book")
    public BookResponse findBook(@RequestParam("title") String title) {
        return bookService.findBooksByTitle(title);
    }
}
