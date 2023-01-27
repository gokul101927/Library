package com.library.service;

import com.library.exception.BookNotFoundException;
import com.library.exception.UnableToSaveBookException;
import com.library.model.Book;
import com.library.model.BookDTO;
import com.library.model.BookDetails;
import com.library.repository.BookDetailsRepository;
import com.library.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

import java.io.*;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements IBookService {
    public static final String FILE_NOT_STORED = "Book cannot be stored ";
    private final BookRepository bookRepository;
    private final BookDetailsRepository bookDetailsRepository;

    @Override
    @Transactional
    public BookDTO saveBook(MultipartFile file, BookDetails bookDetails) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (filename.contains("...")) {
                throw new UnableToSaveBookException(FILE_NOT_STORED + filename);
            }
            log.info("Book saving started");
            byte[] image = getThumbnailFromPdf(file);
            log.info("Image converted");
            Book book = bookRepository.save(new Book(filename, file.getBytes(), image));
            log.info("Book saved");
            BookDetails newBookDetails = bookDetailsRepository.save(bookDetails);
            log.info("BookDetails saved");
            return new BookDTO(book, newBookDetails);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnableToSaveBookException(FILE_NOT_STORED + filename);
        }
    }

    @Override
    @Transactional
    public List<BookDTO> getAll() {
        return bookRepository.findAll().stream()
                .flatMap(book -> bookDetailsRepository.findAll().stream()
                        .filter(bookDetails -> book.getId().equals(bookDetails.getId()))
                        .map(bookDetails -> new BookDTO(book, bookDetails)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        BookDetails bookDetails = bookDetailsRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        return new BookDTO(book, bookDetails);
    }

    @Override
    @Transactional
    public void deleteBookById(Long id) {
        if (bookRepository.existsById(id) && bookDetailsRepository.existsById(id)) {
            bookRepository.deleteById(id);
            bookDetailsRepository.deleteById(id);
        } else {
            throw new BookNotFoundException(id);
        }
    }

    @Override
    @Transactional
    public void deleteAll() {
        bookRepository.deleteAll();
        bookDetailsRepository.deleteAll();
    }

    public byte[] getThumbnailFromPdf(MultipartFile file) {
        log.info("image converting started");
        try {
            PDDocument document = PDDocument.load(convertToFile(file));
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage image = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
            document.close();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnableToSaveBookException(FILE_NOT_STORED + file.getName());
        }
    }

    public File convertToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (OutputStream os = new FileOutputStream(convFile)) {
            os.write(file.getBytes());
        }
        return convFile;
    }
}
