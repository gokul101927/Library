package com.library.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BookDetails {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String title;

    private String author;
    private int pages;

    @OneToOne
    @JoinColumn(name = "id")
    private Book book;

    public BookDetails(String title, String author, int pages) {
        this.title = title;
        this.author = author;
        this.pages = pages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookDetails bookDetails))
            return false;
        return Objects.equals(this.id, bookDetails.id)
                && Objects.equals(this.title, bookDetails.title)
                && Objects.equals(this.author, bookDetails.author)
                && Objects.equals(this.pages, bookDetails.pages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title, this.author, this.pages);
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", pages=" + pages +
                '}';
    }
}
