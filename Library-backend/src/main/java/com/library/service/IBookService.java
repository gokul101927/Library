package com.library.service;

import com.library.model.Book;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IBookService {

    Book saveBook(MultipartFile file);

    List<Book> getAll();

    Book getBookById(String id);

    void deleteBookById(String id);

    void deleteAll();
}
