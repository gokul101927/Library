package com.library;

import com.library.model.BookDetails;
import com.library.repository.BookDetailsRepository;
import com.library.service.BookDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(BookDetailsRepository bookDetailsRepository, BookDetailsServiceImpl bookService) {
		return args -> {
			BookDetails bookDetails1 = new BookDetails("PS", "Kalki", 1202, true);
			bookDetailsRepository.save(bookDetails1);
			bookDetailsRepository.save(new BookDetails("PS-2", "Kalki", 1651, false));
			log.info("Sample database initialized");
			log.info(String.valueOf(bookService.findBookById(bookDetails1.getId())));
		};
	}

}
