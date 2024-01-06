package com.example.librarymanagement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "borrower")
public class BorrowerEntity {

    public BorrowerEntity(Object objects){
        Object[] object = (Object[])objects ;
        System.out.println("AAA");
        this.cardId = (String) object[0];
        this.ssn = (String) object[1];
        this.bName = (String) object[2];
        this.address = (String) object[3];
        this.phone = (String) object[4];
    }
    public BorrowerEntity(String cardId, String ssn, String bName, String address, String phone) {
        this.cardId = cardId;
        this.ssn = ssn;
        this.bName = bName;
        this.address = address;
        this.phone = phone;
    }

    public BorrowerEntity() {
    }

    @Id
    @Column(name = "card_id")
   private String cardId;
    private String ssn;
    @Column(name = "Bname")
    private String bName;
    private String address;
    private String phone;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
