package com.example.librarymanagement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "book_authors")
public class BookAuthorEntity {

    public BookAuthorEntity() {
    }

    public BookAuthorEntity(String authorId, String isbn) {
        this.authorId = authorId;
        this.isbn = isbn;
    }

    @Id
    @Column(name = "author_id")
    private String authorId;

    private String isbn;


    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
