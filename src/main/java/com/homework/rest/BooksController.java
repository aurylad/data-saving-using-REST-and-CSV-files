package com.homework.rest;

import static com.homework.rest.RestUtils.toResponseEntity;

import com.homework.dao.Book;
import com.homework.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

/*
 * Implemented GET ALL, CREATE, UPDATE and DELETE REST calls.
 * GET BY BARCODE and GET PRICE calls are implemented in GetAnyBookController,
 * these methods not related with specific type, they work independently.
 * */

@RestController
@RequestMapping("/api")
public class BooksController {

	@Autowired
    BooksService bookServiceImpl;

	@RequestMapping(value = "/book", method = RequestMethod.POST)
	public ResponseEntity<Object> createBook(@Valid @RequestBody Book book) {
		return bookServiceImpl.createBook(book);
	}

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public ResponseEntity<Collection<Book>> getBooks() {
		return  toResponseEntity(bookServiceImpl.getBooks());
	}

	@RequestMapping(value = "/book/{barcode}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateBook(@Valid @PathVariable("barcode") String barcode, @RequestBody Book book) {
		return bookServiceImpl.updateBook(barcode, book);
	}

	@RequestMapping(value = "/book/{barcode}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteBook(@PathVariable("barcode") String barcode) {
		return bookServiceImpl.deleteBook(barcode);
	}
	
}
