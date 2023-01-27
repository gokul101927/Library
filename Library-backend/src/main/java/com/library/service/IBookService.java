package com.library.service;

import com.library.model.Book;
import com.library.model.BookDTO;
import com.library.model.BookDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IBookService {

    BookDTO saveBook(MultipartFile file, BookDetails bookDetails);

    BookDTO getBookById(Long id);
    List<BookDTO> getAll();

    void deleteBookById(Long id);

    void deleteAll();

}
