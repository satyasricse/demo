package com.example.librarymanagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class DataInitialization {

    public static void main(String[] args) {
        Connection connection = getDatabaseConnection();
        loadBooksData(connection);
        loadBorrowersData(connection);

        System.out.println("Data Loaded successfully");

    }

    static Connection getDatabaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/library";
            String username = "root";
            String password = "password";
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {

        }
        return null;
    }

    static void loadBooksData(Connection connection) {
        String fileName = "src/main/resources/books.csv";
        int size = 7;
        try { //ISBN10	ISBN13	Title	Authro	Cover	Publisher	Pages
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
                        bookEntity.setCover(data[4]);
                        bookEntity.setPublisher(data[5]);
                        bookEntity.setPages(Integer.parseInt(data[6]));

                        String createBookSQL = "insert into book (isbn,title,publisher,cover,pages) values (?,?,?,?,?)";

                        PreparedStatement createBookStmt = connection.prepareStatement(createBookSQL);
                        createBookStmt.setString(1, data[0]);
                        createBookStmt.setString(2, data[2]);
                        createBookStmt.setString(3,data[5]);
                        createBookStmt.setString(4,data[4]);
                        createBookStmt.setInt(5, Integer.parseInt(data[6]));

                        System.out.println(createBookStmt.toString());

                        createBookStmt.executeUpdate();
                        createBookStmt.close();

                        //bookRepository.createBook(bookEntity);

                        AuthorEntity author = new AuthorEntity();
                        author.setAuthorId(data[1]);
                        author.setName(data[3]);
                        String createAuthorSQL = "insert into authors (author_id,name) values (?,?)";
                        PreparedStatement authorStmt = connection.prepareStatement(createAuthorSQL);
                        authorStmt.setString(1, data[1]);
                        authorStmt.setString(2, data[3]);

                        System.out.println(authorStmt.toString());

                        authorStmt.executeUpdate();

                        authorStmt.close();

                        //bookRepository.createAuthor(author);

                        BookAuthorEntity bookAuthorEntity = new BookAuthorEntity();
                        bookAuthorEntity.setAuthorId(data[1]);
                        bookAuthorEntity.setIsbn(data[0]);

                        String bookAuthorSQL = "insert into book_authors (isbn,author_id) values (?,?)";
                        PreparedStatement bookAuthorStmt = connection.prepareStatement(bookAuthorSQL);
                        bookAuthorStmt.setString(1, data[0]);
                        bookAuthorStmt.setString(2, data[1]);

                        System.out.println(bookAuthorStmt.toString());

                        bookAuthorStmt.executeUpdate();
                        bookAuthorStmt.close();

                        //bookRepository.createBookAuthor(bookAuthorEntity);

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

    static void loadBorrowersData(Connection connection) {
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

                        String createBrwrSQL = "insert into borrower (card_id,ssn,bName,address,phone) values (?,?,?,?,?)";
                        PreparedStatement createBorrowerStmt = connection.prepareStatement(createBrwrSQL);
                        createBorrowerStmt.setString(1, data[0]);
                        createBorrowerStmt.setString(2, data[1]);
                        createBorrowerStmt.setString(3, data[2] + " " + data[3]);
                        createBorrowerStmt.setString(4, data[5] + " " + data[6] + " " + data[7]);
                        createBorrowerStmt.setString(5, data[8]);

                        System.out.println(createBorrowerStmt);

                        createBorrowerStmt.executeUpdate();
                        createBorrowerStmt.close();

                        // bookRepository.createBorrower(borrower);
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
