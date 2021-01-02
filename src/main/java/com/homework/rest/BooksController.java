package com.homework.rest;

import static com.homework.rest.RestUtils.toResponseEntity;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.homework.dao.Book;
import com.homework.service.BookService;

/*
 * Implemented GET ALL, CREATE, UPDATE and DELETE REST calls.
 * GET BY BARCODE and GET PRICE calls are implemented in GetAnyBookController,
 * these methods not related with specific type, they work independently.
 * */

@RestController
@RequestMapping("/api")
public class BooksController {

	@Autowired
	BookService bookService;
	
	@RequestMapping(value = "/book", method = RequestMethod.POST)
	public ResponseEntity<Object> createBook(@Valid @RequestBody Book book) {	
		return bookService.createBook(book);
	}
	
	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public ResponseEntity<Collection<Book>> getBooks() {
		return  toResponseEntity(bookService.getBooks());
	}

	@RequestMapping(value = "/book/{barcode}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateBook(@Valid @PathVariable("barcode") String barcode, @RequestBody Book book) {
		return bookService.updateBook(barcode, book);
	}
	
	@RequestMapping(value = "/book/{barcode}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteBook(@PathVariable("barcode") String barcode) {
		return bookService.deleteBook(barcode);
	}
	
}
