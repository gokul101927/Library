package com.library.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.converter.BookDetailsConverter;
import com.library.model.*;
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

import java.util.List;

@RestController
@RequestMapping(path = "/api/file")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookServiceImpl bookService;

    private final BookDetailsConverter bookDetailsConverter;

    private final ObjectMapper objectMapper;

    @PostMapping("/upload")
    public ResponseEntity<BookResponse> uploadBook(@RequestParam("file") MultipartFile file, @RequestParam("bookDetails") String bookDetailsDto) {
        BookDetails bookDetails = null;
        try {
            bookDetails = bookDetailsConverter.DtoToEntity(objectMapper.readValue(bookDetailsDto, BookDetailsDto.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        BookDTO book = bookService.saveBook(file, bookDetails);
        String bookUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(String.valueOf(book.book().getId())).toUriString();
        String thumbnailUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/thumbnail/").path(String.valueOf(book.book().getId())).toUriString();
        return new ResponseEntity<>(new BookResponse(book.book().getId(), book.bookDetails().getId(), book.book().getFileName(), bookUri, thumbnailUri), HttpStatus.CREATED);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downLoadBook(@PathVariable Long id) {
        BookDTO book = bookService.getBookById(id);
        return ResponseEntity.ok().
                header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + book.book().getFileName() + "\"").
                body(new ByteArrayResource(book.book().getBookFile()));
    }

    @GetMapping("/download/thumbnail/{id}")
    public ResponseEntity<Resource> downLoadThumbnail(@PathVariable Long id) {
        BookDTO book = bookService.getBookById(id);
        return ResponseEntity.ok().
                header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + book.book().getFileName().replace("pdf", "png") + "\"").
                body(new ByteArrayResource(book.book().getThumbnailImage()));
    }

    @GetMapping("/download")
    public ResponseEntity<List<BookDTO>> downloadAllBook() {
        List<BookDTO> bookDTOS = bookService.getAll();
        return new ResponseEntity<>(bookDTOS, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteBookById(@PathVariable("id") Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }

}
