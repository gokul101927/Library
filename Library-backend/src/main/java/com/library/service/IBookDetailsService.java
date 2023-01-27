package com.library.service;

import com.library.model.BookDetails;

import java.util.List;

public interface IBookDetailsService {

    BookDetails addBook(BookDetails bookDetails);

    List<BookDetails> findAllBook();

    BookDetails findBookById(Long id);

    void deleteBookById(Long id);
}
