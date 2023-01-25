package com.library.controller;

import com.library.model.BookDetails;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookDetailsModelAssembler implements RepresentationModelAssembler<BookDetails, EntityModel<BookDetails>> {

    @Override
    public EntityModel<BookDetails> toModel(BookDetails bookDetails) {
        return EntityModel.of(bookDetails, linkTo(methodOn(BookDetailsController.class)
                        .getBookById(bookDetails.getId()))
                        .withSelfRel(),
                linkTo(methodOn(BookDetailsController.class)
                        .getAllBooks())
                        .withRel("books"));
    }
}

