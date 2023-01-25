package com.library.service;

import com.library.model.BookDetails;

import java.util.List;

public interface IBookDetailsService {

    BookDetails addBook(BookDetails bookDetails);

    List<BookDetails> findAllBook();

    BookDetails findBookById(String id);

    void deleteBookById(String id);
}
