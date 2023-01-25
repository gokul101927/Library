package com.library.controller;

import com.library.converter.BookDetailsConverter;
import com.library.model.BookDetails;
import com.library.model.BookDetailsDto;
import com.library.service.BookDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/api", produces = "application/json")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class BookDetailsController {

    private final BookDetailsServiceImpl bookService;
    private final BookDetailsModelAssembler modelAssembler;
    private final BookDetailsConverter bookDetailsConverter;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<BookDetails>> getAllBooks() {
        List<EntityModel<BookDetails>> books = bookService.findAllBook().stream().map(modelAssembler::toModel).toList();
        return CollectionModel.of(books, linkTo(methodOn(BookDetailsController.class).getAllBooks()).withSelfRel());
    }

    @GetMapping("/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<BookDetails> getBookById(@PathVariable("id") Long id) {
        BookDetails bookDetails = bookService.findBookById(id);
        return modelAssembler.toModel(bookDetails);
    }

    @PostMapping(path = "/add", consumes = "application/json")
    public ResponseEntity<EntityModel<BookDetails>> addBook(@RequestBody BookDetailsDto bookDetailsDto) {
        BookDetails newBookDetails = bookDetailsConverter.DtoToEntity(bookDetailsDto);
        EntityModel<BookDetails> bookEntityModel = modelAssembler.toModel(bookService.addBook(newBookDetails));
        return ResponseEntity.created(bookEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(bookEntityModel);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteBookById(@PathVariable("id") Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }

}
