package com.library.controller;

import com.library.model.Book;
import com.library.model.BookResponse;
import com.library.service.BookServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path = "/api/file")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookServiceImpl bookService;

    @PostMapping("/upload")
    public ResponseEntity<BookResponse> uploadBook(@RequestParam("file") MultipartFile file) {
        Book book = bookService.saveBook(file);
        String bookUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(book.getId()).toUriString();
        return new ResponseEntity<>(new BookResponse(book.getFileName(), bookUri), HttpStatus.CREATED);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downLoadBook(@PathVariable String id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok().
                header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + book.getFileName() + "\"").
                body(new ByteArrayResource(book.getBookFile()));
    }

}
