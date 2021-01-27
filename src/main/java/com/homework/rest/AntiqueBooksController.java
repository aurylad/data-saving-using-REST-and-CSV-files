package com.homework.rest;

import static com.homework.rest.RestUtils.toResponseEntity;

import java.util.Collection;

import javax.validation.Valid;

import com.homework.dao.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.homework.dao.AntiqueBook;
import com.homework.service.BooksService;

/*
 * Implemented GET ALL, CREATE, UPDATE and DELETE REST calls.
 * GET BY BARCODE and GET PRICE calls are implemented in GetAnyBookController,
 * these methods not related with specific type, they work independently.
 * */

@RestController
@RequestMapping("/api")
public class AntiqueBooksController {

	@Autowired
    BooksService antiqueBookServiceImpl;
	
	@RequestMapping(value = "/antiquebook", method = RequestMethod.POST)
	public ResponseEntity<Object> createBook(@Valid @RequestBody AntiqueBook antiqueBook) {
		return antiqueBookServiceImpl.createBook(antiqueBook);
	}
	
	@RequestMapping(value = "/antiquebooks", method = RequestMethod.GET)
	public ResponseEntity<Collection<Book>> getAntiqueBooks() {
		return  toResponseEntity(antiqueBookServiceImpl.getBooks());
	}

	@RequestMapping(value = "/antiquebook/{barcode}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateAntiqueBook(@PathVariable("barcode") String barcode, @Valid @RequestBody AntiqueBook antiqueBook) {
		return antiqueBookServiceImpl.updateBook(barcode, antiqueBook);
	}
	
	@RequestMapping(value = "/antiquebook/{barcode}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteAntiqueBook(@PathVariable("barcode") String barcode) {
		return antiqueBookServiceImpl.deleteBook(barcode);
	}
	
}
