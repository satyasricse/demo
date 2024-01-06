package com.example.librarymanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class BookController {
    @Autowired
    BookRepository booksRepository;

    @GetMapping("/search-book")
    List<SearchBook> searchBook(@RequestParam("searchText") String searchText) {
        if(searchText != null && searchText.length() >0){
            List<Object[]> objects = booksRepository.searchBook(searchText.toLowerCase());
            if (objects != null && objects.size() > 0) {
                return objects.stream().map(obj -> new SearchBook(obj)).collect(Collectors.toList());
            }
        }
        return null;
    }

    @PostMapping("/create-book")
    void createBook(@RequestBody CreateBookEntity createBook) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setIsbn(createBook.getIsbn());
        bookEntity.setTitle(createBook.getTitle());
        bookEntity.setCover(createBook.getCover());
        bookEntity.setPublisher(createBook.getPublisher());
        bookEntity.setPages(createBook.getPages());

        booksRepository.createBook(bookEntity);

        AuthorEntity author = new AuthorEntity();
        author.setName(createBook.getName());

        booksRepository.createAuthor(author);
    }

    @PostMapping("/checkout-book")
    Map<String, Object> checkOutBook(@RequestBody BookLoansEntity bookLoansEntity) {
        Map<String, Object> dataMap = new HashMap<>();
        LocalDate dateOut = LocalDate.now();
        LocalDate dueDate = dateOut.plusDays(14);
        bookLoansEntity.setDateOut(Date.valueOf(dateOut));
        bookLoansEntity.setDueDate(Date.valueOf(dueDate));
        try {
            validateIfBookIsCheckedOut(bookLoansEntity);

            validateMaxAllowedBooks(bookLoansEntity);

            validateIfPaymentIsDue(bookLoansEntity);

            booksRepository.checkoutBook(bookLoansEntity);
        } catch (Exception e) {
            dataMap.put("error", e.getMessage());
            e.printStackTrace();
        }
        return dataMap;

    }

    void validateIfBookIsCheckedOut(BookLoansEntity bookLoansEntity) {
        List<String> users = booksRepository.findIfBookCheckedout(bookLoansEntity.getIsbn());
        if (users != null && users.size() > 0) {
            throw new RuntimeException("BOOK already checkout by " + users.get(0));
        }
    }

    void validateMaxAllowedBooks(BookLoansEntity bookLoans) {
        List<String> checkedOutBooksByUser = booksRepository.findTotalBooksCheckedOutByUser(bookLoans.getCardId());
        if (checkedOutBooksByUser != null && checkedOutBooksByUser.size() >= 3) {
            throw new RuntimeException("User already checked out maximum books - " + checkedOutBooksByUser.size());
        }
    }

    void validateIfPaymentIsDue(BookLoansEntity bookLoans){
        if(isPaymentDue(bookLoans.getCardId())){
            throw new RuntimeException("Need to clear fines before checking out books");
        }
    }

    @GetMapping("/checkedout-books")
    List<CheckedInBook> getCheckedOutBooks(@RequestParam String searchText) {
        if(searchText != null && searchText.length() > 0){
            List<Object[]> checkedInBooks = booksRepository.getCheckedOutBooks(searchText.toLowerCase());
            if (checkedInBooks != null && checkedInBooks.size() > 0) {
                return checkedInBooks.stream().map(objects -> new CheckedInBook(objects)).collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }

    @PostMapping("/checkin-book")
    Map<String, Object> checkInBook(@RequestBody BookLoansEntity book) {
        HashMap<String, Object> dataMap = new HashMap<>();
        book.setDateIn(Date.valueOf(LocalDate.now()));
        if(isPaymentDue(book.getCardId())){
            dataMap.put("error", "Need to clear fines before checking in books");
            return dataMap;
        }
        int result = booksRepository.checkInBook(book);
        return dataMap;
    }

    boolean isPaymentDue(String cardId){
        Object[] paymentAmountList = booksRepository.getPaymentAmount(cardId);
        if (paymentAmountList != null && paymentAmountList.length > 0) {
            Object[] obj = (Object[]) paymentAmountList[0];
            if (obj != null && obj.length > 0 && (Double) obj[1] > 0) {
                return true;
            }
        }
        return false;
    }

    @PostMapping("/create-borrower")
    Map<String, Object> createBorrower(@RequestBody BorrowerEntity borrower) {
        Map<String, Object> dataMap = new HashMap<>();
        Object borrowerBySsn = booksRepository.findBorrowerBySsn(borrower.getSsn());
        //BorrowerEntity borrowerEntity = new BorrowerEntity(borrowerBySsn);
        if (borrowerBySsn != null  ) {
            dataMap.put("error", "Borrower with SSN already exists");
            return dataMap;
        }
        String maxId = booksRepository.getLastBorrowerId();
        Integer id = Integer.valueOf(maxId.replace("ID", ""));
        String nextId = "ID00" + (id + 1);
        borrower.setCardId(nextId);
        booksRepository.createBorrower(borrower);

        return dataMap;
    }

    @GetMapping("/refresh-fines")
    List<LoanFineEntity> refreshFines() {
        List<Object[]> lateBooks = booksRepository.getRefreshFinesQuery();
        List<LateBookEntity> collect = lateBooks.stream().map(objects -> new LateBookEntity(objects)).collect(Collectors.toList());
        for (LateBookEntity lateBookEntity : collect) {
            BigInteger dateDiff = lateBookEntity.getDateDiff();
            Double fine = dateDiff.intValue() * 0.25;
            Object[] finesRecord = booksRepository.getFinesRecord(lateBookEntity.getLoanId());
            if (finesRecord == null || finesRecord.length == 0) {
                FineEntity fineEntity = new FineEntity();
                fineEntity.setLoanId(lateBookEntity.getLoanId());
                fineEntity.setFineAmount(fine);
                fineEntity.setPaid(false);
                booksRepository.insertFineRecord(fineEntity);
            } else {
                FineEntity fineEntity = new FineEntity();
                fineEntity.setLoanId(lateBookEntity.getLoanId());
                fineEntity.setFineAmount(fine);
                booksRepository.updateFineRecord(fineEntity);
            }
        }
        return booksRepository.getAllFines().stream().map(objects -> {

            LoanFineEntity fine = new LoanFineEntity();
            fine.setIsbn((String) objects[0]);
            fine.setCardId((String) objects[1]);
            fine.setLoanId((Integer) objects[2]);
            fine.setFineAmount((Double) objects[3]);
            fine.setPaid(objects[4] != null ? (Boolean) objects[4] : false);
            return fine;
        }).collect(Collectors.toList());
    }

    @GetMapping("/get-fine-amount")
    Map<String, Object> getFineAmount(@RequestParam("cardId") String cardId) {
        Map<String, Object> dataMap = new HashMap<>();
        Object[] paymentAmountList = booksRepository.getPaymentAmount(cardId);
        if (paymentAmountList != null && paymentAmountList.length > 0) {
            Object[] paymentAmount = (Object[]) paymentAmountList[0];
            if (paymentAmount != null && paymentAmount.length > 0) {
                dataMap.put("cardId", paymentAmount[0]);
                dataMap.put("paymentAmount", paymentAmount[1]);
            }
        }
        return dataMap;
    }

    @PutMapping("/make-payment")
    void makePayment(@RequestParam("cardId") String cardId) {
        int i = booksRepository.makePayment(cardId);
        System.out.println(i);
    }
}
