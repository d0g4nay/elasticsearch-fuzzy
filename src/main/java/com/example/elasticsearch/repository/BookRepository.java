package com.example.elasticsearch.repository;

import com.example.elasticsearch.domain.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookRepository extends ElasticsearchRepository<Book, String> {

    List<Book> findByTitle(String title);

}
