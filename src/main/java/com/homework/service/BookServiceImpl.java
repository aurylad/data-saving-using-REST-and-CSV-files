package com.homework.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import com.homework.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

@Service
public class BookServiceImpl implements BooksService {

    private CsvWriter csvWriter;
    private CsvReader csvReader;

    @Autowired
    BarcodeService barcodeService;

    @Override
    public ResponseEntity<Object> createBook(Book book) {
        final Map<String, Book> bookRepo = retrieveBooks();

        Optional<Barcode> barcodeOptional = Optional.ofNullable(barcodeService.getBarcode(book.getBarcode()));

        if (barcodeOptional.isPresent()) {
            return new ResponseEntity<>("The record already exists", HttpStatus.CONFLICT);
        } else if (book.getBarcode().trim().isEmpty()) {
            return new ResponseEntity<>("The barcode cannot be empty", HttpStatus.BAD_REQUEST);
        }

        book.setPricePerUnit(book.getPricePerUnit().setScale(2, RoundingMode.HALF_EVEN));
        bookRepo.put(book.getBarcode(), book);

        barcodeService.createBarcode(book.getBarcode(), book.getClass().getSimpleName());
        writeBooks(bookRepo);

        return ResponseEntity.ok().build();
    }

    @Override
    public Collection<Book> getBooks() {
        return retrieveBooks().values();
    }

    @Override
    public Book getByBarcode(String barcode) {
        return retrieveBooks().get(barcode);
    }

    @Override
    public ResponseEntity<Object> updateBook(String barcode, Book book) {
        final Map<String, Book> bookRepo = retrieveBooks();

        if (bookRepo.get(barcode) == null) {
            return ResponseEntity.badRequest().build();
        }

        book.setPricePerUnit(book.getPricePerUnit().setScale(2, RoundingMode.HALF_EVEN));

        book.setBarcode(barcode);
        bookRepo.replace(barcode, book);
        writeBooks(bookRepo);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteBook(String barcode) {
        final Map<String, Book> bookRepo = retrieveBooks();

        if (bookRepo.get(barcode) == null) {
            return ResponseEntity.badRequest().build();
        }

        bookRepo.remove(barcode);
        barcodeService.deleteBarcode(barcode);
        writeBooks(bookRepo);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<BigDecimal> calculatePrice(String barcode) {
//        Book book = getAntiqueBookByBarcode(barcode);
		Optional<Book> bookOptional = Optional.ofNullable(getByBarcode(barcode));

        if (bookOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        BigDecimal unitPrice = bookOptional.get().getPricePerUnit();
        int quantity = bookOptional.get().getQuantity();

        // Total price = Quantity * Price
        BigDecimal totalPrice = unitPrice.multiply(new BigDecimal(quantity));

        return ResponseEntity.ok(totalPrice);
    }

    // Retrieve from csv
    private Map<String, Book> retrieveBooks() {
        csvReader = new CsvReader();
        Map<String, Book> bookRepo = new HashMap<>();
        try {
            bookRepo = csvReader.readBooksCsvFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookRepo;
    }

    // Write to csv
    private void writeBooks(Map<String, Book> booksToSave) {
        csvWriter = new CsvWriter();
        try {
            csvWriter.writeToCsv(booksToSave);
        } catch (CsvDataTypeMismatchException e) {
            e.printStackTrace();
        } catch (CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
