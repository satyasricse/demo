package com.example.librarymanagement;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

//@Component
public class DataInitialization_Spring {

    @Autowired
    BookRepository bookRepository;

    @PostConstruct
    void init() {
        loadBooksData();
        loadBorrowersData();
    }



    void loadBooksData() {
        String fileName = "src/main/resources/books.csv";
        int size = 7;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            if (reader != null) {
                String headers = reader.readLine();
                String nextLine;
                while ((nextLine = reader.readLine()) != null) {
                    String[] data = nextLine.split("\t");
                    if (data.length == size) {
                        BookEntity bookEntity = new BookEntity();
                        bookEntity.setIsbn(data[0]);
                        bookEntity.setTitle(data[2]);

                        bookRepository.createBook(bookEntity);

                        AuthorEntity author = new AuthorEntity();
                        author.setAuthorId(data[1]);
                        author.setName(data[3]);

                        bookRepository.createAuthor(author);

                        BookAuthorEntity bookAuthorEntity = new BookAuthorEntity();
                        bookAuthorEntity.setAuthorId(data[1]);
                        bookAuthorEntity.setIsbn(data[0]);
                        bookRepository.createBookAuthor(bookAuthorEntity);

                    } else {
                        System.out.println("Data doesnt match" + nextLine);
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadBorrowersData() {
        String fileName = "src/main/resources/borrowers.csv";
        int size = 9;
        List<BorrowerEntity> borrowersList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            if (reader != null) {
                String headers = reader.readLine();
                String nextLine;
                while ((nextLine = reader.readLine()) != null) {
                    String[] data = nextLine.split(",");
                    if (data.length == size) {
                        BorrowerEntity borrower = new BorrowerEntity();
                        borrower.setCardId(data[0]);
                        borrower.setSsn(data[1]);
                        borrower.setbName(data[2] + " " + data[3]);
                        borrower.setAddress(data[5] + " " + data[6] + " " + data[7]);
                        borrower.setPhone(data[8]);

                        bookRepository.createBorrower(borrower);
                    } else {
                        System.out.println("Data doesnt match" + nextLine);
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
