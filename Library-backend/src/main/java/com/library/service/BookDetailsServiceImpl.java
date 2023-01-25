package com.library.service;

import com.library.exception.BookNotFoundException;
import com.library.model.BookDetails;
import com.library.repository.BookDetailsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookDetailsServiceImpl implements IBookDetailsService {

    private final BookDetailsRepository bookDetailsRepository;

    public BookDetailsServiceImpl(BookDetailsRepository bookDetailsRepository) {
        this.bookDetailsRepository = bookDetailsRepository;
    }

    @Override
    public BookDetails addBook(BookDetails bookDetails) {
        return bookDetailsRepository.save(bookDetails);
    }

    @Override
    public List<BookDetails> findAllBook() {
        return bookDetailsRepository.findAll();
    }

    @Override
    public BookDetails findBookById(String id) {
        Optional<BookDetails> optionalBook = bookDetailsRepository.findById(id);

        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException(id);
        }

        return optionalBook.get();
    }

    @Override
    public void deleteBookById(String id) {
        if (bookDetailsRepository.existsById(id)) {
            bookDetailsRepository.deleteById(id);
        } else {
            throw new BookNotFoundException(id);
        }

    }
}
