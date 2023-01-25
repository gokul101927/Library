package com.library.converter;

import com.library.model.BookDetails;
import com.library.model.BookDetailsDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDetailsConverter {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public BookDetailsDto entityToDto(BookDetails bookDetails) {
        return modelMapper().map(bookDetails, BookDetailsDto.class);
    }

    public BookDetails DtoToEntity(BookDetailsDto bookDetailsDto) {
        return modelMapper().map(bookDetailsDto, BookDetails.class);
    }

    public List<BookDetailsDto> entityToDto(List<BookDetails> bookDetails) {
        return bookDetails.stream().map(this::entityToDto).toList();
    }

    public List<BookDetails> DtoToEntity(List<BookDetailsDto> bookDetailsDtos) {
        return bookDetailsDtos.stream().map(this::DtoToEntity).toList();
    }
}
