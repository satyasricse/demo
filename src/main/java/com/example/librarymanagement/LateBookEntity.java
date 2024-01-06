package com.example.librarymanagement;

import java.math.BigInteger;

public class LateBookEntity {
    private String isbn;
    private String cardId;
    private int loanId;
    private BigInteger dateDiff;

    public LateBookEntity(Object[] obj) {
        this.isbn = (String) obj[0];
        this.cardId = (String) obj[1];
        this.loanId = (int) obj[2];
        this.dateDiff = (BigInteger) obj[3];
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public BigInteger getDateDiff() {
        return dateDiff;
    }

    public void setDateDiff(BigInteger dateDiff) {
        this.dateDiff = dateDiff;
    }
}
