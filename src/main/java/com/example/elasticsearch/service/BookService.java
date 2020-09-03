package com.example.elasticsearch.service;

import com.example.elasticsearch.domain.Book;
import com.example.elasticsearch.model.request.BookSaveRequest;
import com.example.elasticsearch.model.response.BookResponse;
import com.example.elasticsearch.repository.BookRepository;
import org.elasticsearch.common.unit.Fuzziness;
import org.joda.time.DateTimeUtils;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.fuzzyQuery;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final ElasticsearchOperations elasticsearchTemplate;


    public BookService(BookRepository bookRepository,
                       ElasticsearchOperations elasticsearchTemplate) {
        this.bookRepository = bookRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public BookResponse findBooksByTitle(String title) {
        List<Book> byTitle = bookRepository.findByTitle(title);

        Optional<BookResponse> bookResponseOptional = byTitle.stream()
                .map(this::convert)
                .findFirst();

        return bookResponseOptional.isPresent() ? bookResponseOptional.get() : new BookResponse();

    }

    BookResponse convert(Book book) {
        BookResponse bookResponse = new BookResponse();
        bookResponse.setAuthor(book.getAuthor());
        bookResponse.setTitle(book.getTitle());
        return bookResponse;
    }

    public void save(BookSaveRequest request) {
        Book book = new Book();
        book.setAuthor(request.getAuthor());
        book.setTitle(request.getTitle());
        book.setId(request.getId());
        book.setReleaseDate(Long.toString(DateTimeUtils.currentTimeMillis()));
        bookRepository.save(book);
    }

    public boolean search(String title) {
        Query query = new NativeSearchQueryBuilder()
                .withQuery(fuzzyQuery("title", title).fuzziness(Fuzziness.AUTO))
                .build();
        SearchHits<Book> search = elasticsearchTemplate.search(query, Book.class);

        return search.get().findFirst().isPresent();
    }
}
