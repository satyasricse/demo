package com.example.librarymanagement;

import java.sql.Date;

public class CheckedInBook {
    private  Double fineAmt;
    private String isbn;
    private String bname;
    private String cardId;
    private Date dateIn;
private boolean paid;


    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public CheckedInBook() {
    }

    public CheckedInBook(Object[] obj) {
        this.isbn = (String)obj[0];
        this.bname = (String)obj[1];
        this.cardId = (String)obj[2];
        this.dateIn = (Date) obj[3];
        this.fineAmt = (Double) obj[4];
        this.paid = obj[5] != null ? (boolean) obj[5] : false;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Double getFineAmt() {
        return fineAmt;
    }

    public void setFineAmt(Double fineAmt) {
        this.fineAmt = fineAmt;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
