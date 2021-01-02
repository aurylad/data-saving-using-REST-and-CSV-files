package com.homework.rest;

import static com.homework.service.Constans.*;

import java.math.BigDecimal;

import static com.homework.rest.RestUtils.toResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.homework.dao.Barcode;
import com.homework.dao.Book;
import com.homework.service.AntiqueBookService;
import com.homework.service.BarcodeService;
import com.homework.service.BookService;
import com.homework.service.JournalService;

/*
 * GET BY BARCODE, GET PRICE
 * These REST calls not require to determine type, when barcode 
 * parameter is given, methods self decide which file type to read.
 * */

@RestController
@RequestMapping("/api")
public class AnyBookController {

	@Autowired
	BookService bookService;

	@Autowired
	AntiqueBookService antiqueBookService;

	@Autowired
	JournalService journalsService;

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
			return toResponseEntity(bookService.getBookByBarcode(barcode));
		case ANTIQUE_BOOK:
			return toResponseEntity(antiqueBookService.getAntiqueBookByBarcode(barcode));
		case JOURNAL:
			return toResponseEntity(journalsService.getJournalByBarcode(barcode));
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
			return bookService.calculatePrice(barcode);
		case ANTIQUE_BOOK:
			return antiqueBookService.calculatePrice(barcode);
		case JOURNAL:
			return journalsService.calculatePrice(barcode);
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
