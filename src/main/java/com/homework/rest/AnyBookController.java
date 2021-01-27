package com.homework.rest;

import com.homework.dao.Barcode;
import com.homework.dao.Book;
import com.homework.service.BarcodeService;
import com.homework.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static com.homework.rest.RestUtils.toResponseEntity;
import static com.homework.service.Constans.*;

/*
 * GET BY BARCODE, GET PRICE
 * These REST calls not require to determine type, when barcode
 * parameter is given, methods self decide which file type to read.
 * */

@RestController
@RequestMapping("/api")
public class AnyBookController {

    /*
    *For disambiguate which beans to inject, was used Autowired by name. (Not @Qualifiers)
    * */

    @Autowired
    BooksService bookServiceImpl;

    @Autowired
    BooksService antiqueBookServiceImpl;

    @Autowired
    BooksService journalServiceImpl;

    @Autowired
    BarcodeService barcodeService;

    String type;

    @RequestMapping(value = "/{barcode}", method = RequestMethod.GET)
    public ResponseEntity<Book> getAnyBook(@PathVariable("barcode") String barcode) {
        if (!isBarcodeValid(barcode)) {
            return ResponseEntity.noContent().build();
        }

        switch (type) {
            case SIMPLE_BOOK:
                return toResponseEntity(bookServiceImpl.getByBarcode(barcode));
            case ANTIQUE_BOOK:
                return toResponseEntity(antiqueBookServiceImpl.getByBarcode(barcode));
            case JOURNAL:
                return toResponseEntity(journalServiceImpl.getByBarcode(barcode));
            default:
                return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/price/{barcode}", method = RequestMethod.GET)
    public ResponseEntity<BigDecimal> getPrice(@PathVariable("barcode") String barcode) {
        if (!isBarcodeValid(barcode)) {
            return ResponseEntity.noContent().build();
        }

        switch (type) {
            case SIMPLE_BOOK:
                return bookServiceImpl.calculatePrice(barcode);
            case ANTIQUE_BOOK:
                return antiqueBookServiceImpl.calculatePrice(barcode);
            case JOURNAL:
                return journalServiceImpl.calculatePrice(barcode);
            default:
                return ResponseEntity.badRequest().build();
        }
    }

    public boolean isBarcodeValid(String barcode) {
        Barcode bar = barcodeService.getBarcodes().get(barcode);
        type = null;

        if (bar == null) {
            return false;
        }
        type = bar.getType();

        return true;
    }
}
