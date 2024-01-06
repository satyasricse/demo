package com.example.librarymanagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity,String> {

    @Query(value = "Select b.isbn,b.title,a.name,bl.date_in,bl.date_out from  book b join book_authors ba on b.isbn = ba.isbn " +
            " join authors a on a.author_id = ba.author_id " +
            " left join book_loans bl on bl.isbn = b.isbn where lower(b.isbn) like %:searchText% or lower(b.title) like %:searchText% or lower(a.name) like %:searchText% ",
            nativeQuery = true)
    List<Object[]> searchBook(String searchText);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "insert into book (isbn,title,publisher,cover,pages) values (:#{#books.isbn}, :#{#books.title},:#{#books.publisher},:#{#books.cover},:#{#books.pages})", nativeQuery = true)
    void createBook(BookEntity books);


    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "insert into book_authors (isbn,author_id) values (:#{#bookAuthor.isbn}, :#{#bookAuthor.authorId})", nativeQuery = true)
    void createBookAuthor(BookAuthorEntity bookAuthor);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "insert into authors (author_id,name) values (:#{#author.authorId}, :#{#author.name})", nativeQuery = true)
    void createAuthor(AuthorEntity author);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "insert into borrower (card_id,ssn,bName,address,phone) values (:#{#borrower.cardId}, :#{#borrower.ssn},:#{#borrower.bName}, :#{#borrower.address}, :#{#borrower.phone})", nativeQuery = true)
    void createBorrower(BorrowerEntity borrower);

    @Query(value = "select max(card_id) from borrower", nativeQuery = true)
    String getLastBorrowerId();


    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "insert into book_loans (isbn,card_id,date_out,due_date) values (:#{#bookLoans.isbn},:#{#bookLoans.cardId},:#{#bookLoans.dateOut},:#{#bookLoans.dueDate})", nativeQuery = true)
    void checkoutBook(BookLoansEntity bookLoans);

    @Query(value = "select card_id from book_loans where isbn = :isbn and date_in is null", nativeQuery = true)
    List<String> findIfBookCheckedout(String isbn);

    @Query(value = "select isbn from book_loans where card_id = :cardNo and date_in is null", nativeQuery = true)
    List<String> findTotalBooksCheckedOutByUser(String cardNo);

    @Query(value = "select bl.isbn,b.bname,bl.card_id,bl.date_in,f.fine_amt,f.paid from book_loans bl join borrower b on bl.card_id = b.card_id left join fines f on f.loan_id = bl.loan_id where bl.date_in is null and (bl.isbn like %:searchText% or bl.card_id like %:searchText% or b.bname like %:searchText%)", nativeQuery = true)
    List<Object[]> getCheckedOutBooks(String searchText);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update book_loans set date_in=:#{#book.dateIn} where card_id=:#{#book.cardId} and isbn=:#{#book.isbn} and date_in is null", nativeQuery = true)
    int checkInBook(BookLoansEntity book);

    @Query(value = "select card_id,ssn,bname,address,phone from borrower where ssn=:ssn", nativeQuery = true)
    Object findBorrowerBySsn(String ssn);

    @Query(value = "select bl.isbn, bl.card_id, bl.loan_id,datediff(current_timestamp,due_date ) from book_loans bl where bl.due_date < curdate() and bl.date_in is null", nativeQuery = true)
    List<Object[]> getRefreshFinesQuery();

    @Query(value = "select loan_id,paid from fines where loan_id=:loanId", nativeQuery = true)
    Object[] getFinesRecord(int loanId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "insert into fines (loan_id,fine_amt,paid) values (:#{#fine.loanId},:#{#fine.fineAmount},:#{#fine.paid})", nativeQuery = true)
    int insertFineRecord(FineEntity fine);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update fines set fine_amt =:#{#fine.fineAmount} where loan_id = :#{#fine.loanId} ", nativeQuery = true)
    int updateFineRecord(FineEntity fine);

    @Query(value = "select bl.isbn, bl.card_id,bl.loan_id,f.fine_amt,f.paid from fines f join book_loans bl on f.loan_id = bl.loan_id ", nativeQuery = true)
    List<Object[]> getAllFines();

    @Query(value = "select bl.card_id,sum(fine_amt) from book_loans bl join fines f on f.loan_id = bl.loan_id where bl.card_id=:cardId and f.paid = false group by bl.card_id ", nativeQuery = true)
    Object[] getPaymentAmount(String cardId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update fines f join book_loans bl on f.loan_id = bl.loan_id set paid = true  where bl.card_id = :cardId ",nativeQuery = true)
    int makePayment(String cardId);
}
