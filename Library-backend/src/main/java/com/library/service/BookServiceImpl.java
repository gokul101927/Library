package com.library.service;

import com.library.exception.BookNotFoundException;
import com.library.exception.UnableToSaveBookException;
import com.library.model.Book;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements IBookService {
    public static final String FILE_NOT_STORED = "Book cannot be stored.";
    private final BookRepository bookRepository;

    @Override
    public Book saveBook(MultipartFile file) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (filename.contains("...")) {
                throw new UnableToSaveBookException(FILE_NOT_STORED + filename);
            }
            return bookRepository.save(new Book(filename, file.getBytes(), getThumbnailFromPdf(file)));
        } catch (Exception e) {
            throw new UnableToSaveBookException(FILE_NOT_STORED + filename);
        }

    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(String id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public void deleteBookById(String id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new BookNotFoundException(id);
        }
    }

    @Override
    public void deleteAll() {
        bookRepository.deleteAll();
    }

    public byte[] getThumbnailFromPdf(MultipartFile file) {
        try {
            PDDocument document = PDDocument.load(convertToFile(file));
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage image = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
            document.close();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new UnableToSaveBookException(FILE_NOT_STORED + file.getName());
        }
    }

    public File convertToFile(MultipartFile file) {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            file.transferTo(convFile);
        } catch (Exception e) {
            throw new UnableToSaveBookException(FILE_NOT_STORED + file.getName());
        }
        return convFile;
    }
}
