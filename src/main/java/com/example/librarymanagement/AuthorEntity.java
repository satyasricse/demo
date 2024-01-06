package com.example.librarymanagement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authors")
public class AuthorEntity {

    public AuthorEntity() {
    }

    public AuthorEntity(String authorId, String name) {
        this.authorId = authorId;
        this.name = name;
    }

    @Id
    @Column(name = "author_id")
    private String authorId;

    private String name;

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
