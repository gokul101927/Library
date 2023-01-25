package com.library.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Book {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String fileName;

    @Lob
    @Column(name = "bookFile", length = 1000)
    private byte[] bookFile;

    @Lob
    @Column(name = "thumbnailImage", length = 1000)
    private byte[] thumbnailImage;

    public Book(String fileName, byte[] bookFile, byte[] thumbnailImage) {
        this.fileName = fileName;
        this.bookFile = bookFile;
        this.thumbnailImage = thumbnailImage;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", fileName='" + fileName + '\'' +
                ", bookFile=" + Arrays.toString(bookFile) +
                ", thumbnailImage=" + Arrays.toString(thumbnailImage) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (!Objects.equals(id, book.id)) return false;
        if (!Objects.equals(fileName, book.fileName)) return false;
        if (!Arrays.equals(bookFile, book.bookFile)) return false;
        return Arrays.equals(thumbnailImage, book.thumbnailImage);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(bookFile);
        result = 31 * result + Arrays.hashCode(thumbnailImage);
        return result;
    }
}
