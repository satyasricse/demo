package com.example.librarymanagement;

import java.sql.Date;

public class SearchBook {
    private String isbn;
    private String title;
    private String author;
    private boolean availableStatus;

    public SearchBook(Object[] obj) {
        this.isbn = obj[0] != null? (String)obj[0] : null;
        this.title = obj[1] != null? (String)obj[1] : null;;
        this.author = obj[2] != null? (String)obj[2] : null;;
        Date dateIn = obj[3] != null ? (Date) obj[3]:null;
        Date dateOut = obj[4] != null ? (Date) obj[4]:null;
        if(dateIn == null || dateOut != null){
            this.availableStatus = true;
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isAvailableStatus() {
        return availableStatus;
    }

    public void setAvailableStatus(boolean availableStatus) {
        this.availableStatus = availableStatus;
    }
}
