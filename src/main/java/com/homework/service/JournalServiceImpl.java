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
public class JournalServiceImpl implements BooksService {

    private CsvWriter csvWriter;
    private CsvReader csvReader;

    @Autowired
    BarcodeService barcodeService;

    @Override
    public ResponseEntity<Object> createBook(Book book) {
        Journal journal = (Journal) book;
        final Map<String, Journal> journalRepo = retrieveJournals();

        Optional<Barcode> barcodeOptional = Optional.ofNullable(barcodeService.getBarcode(book.getBarcode()));

        if (barcodeOptional.isPresent()) {
            return new ResponseEntity<>("The record already exists", HttpStatus.CONFLICT);
        } else if (journal.getBarcode().trim().isEmpty()) {
            return new ResponseEntity<>("The barcode cannot be empty", HttpStatus.BAD_REQUEST);
        }

        // Science Index should be from 1 to 10, it is checked in journal dao
        // using Hibernate validators @Max and @Min in the entity class

        journal.setPricePerUnit(journal.getPricePerUnit().setScale(2, RoundingMode.HALF_EVEN));
        journalRepo.put(journal.getBarcode(), journal);

        barcodeService.createBarcode(journal.getBarcode(), journal.getClass().getSimpleName());
        writeJournals(journalRepo);

        return ResponseEntity.ok().build();
    }

    @Override
    public Collection<Book> getBooks() {
        final List<Book> list = new ArrayList<>();

        for (final Journal src : retrieveJournals().values()) {
            list.add(src);
        }

        return list;
    }

    @Override
    public Journal getByBarcode(String barcode) {

        return retrieveJournals().get(barcode);
    }

    @Override
    public ResponseEntity<Object> updateBook(String barcode, Book book) {
        Journal journal = (Journal) book;
        final Map<String, Journal> journalRepo = retrieveJournals();

        Optional<Barcode> barcodeOptional = Optional.ofNullable(barcodeService.getBarcode(book.getBarcode()));

        if (barcodeOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        journal.setPricePerUnit(journal.getPricePerUnit().setScale(2, RoundingMode.HALF_EVEN));
        journal.setBarcode(barcode);
        journalRepo.replace(barcode, journal);
        writeJournals(journalRepo);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteBook(String barcode) {
        final Map<String, Journal> journalRepo = retrieveJournals();

        Optional<Barcode> barcodeOptional = Optional.ofNullable(barcodeService.getBarcode(barcode));

        if (barcodeOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        journalRepo.remove(barcode);
        barcodeService.deleteBarcode(barcode);
        writeJournals(journalRepo);
        return ResponseEntity.ok().build();
//
    }

    @Override
    public ResponseEntity<BigDecimal> calculatePrice(String barcode) {
        Optional<Journal> journalOptional = Optional.ofNullable(getByBarcode(barcode));

        if (journalOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        BigDecimal unitPrice = journalOptional.get().getPricePerUnit();
        int quantity = journalOptional.get().getQuantity();
        int scienceIndex = journalOptional.get().getScienceIndex();

        // Total Price = Quantity * Price * Science Index.

        BigDecimal totalPrice = unitPrice.multiply(new BigDecimal(quantity)).multiply(new BigDecimal(scienceIndex));

        return ResponseEntity.ok(totalPrice);
    }

    // Retrieve from csv
    private Map<String, Journal> retrieveJournals() {
        csvReader = new CsvReader();
        Map<String, Journal> journalRepo = new HashMap<>();
        try {
            journalRepo = csvReader.readJournalsCsvFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return journalRepo;
    }

    // Write to csv
    private void writeJournals(Map<String, Journal> journalsToSave) {
        csvWriter = new CsvWriter();
        try {
            csvWriter.writeToCsvJournals(journalsToSave);
        } catch (CsvDataTypeMismatchException e) {
            e.printStackTrace();
        } catch (CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
