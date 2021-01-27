package com.homework.rest;

import static com.homework.rest.RestUtils.toResponseEntity;

import com.homework.dao.Book;
import com.homework.dao.Journal;
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
public class JournalsController {

	@Autowired
    BooksService journalServiceImpl;

	@RequestMapping(value = "/journal", method = RequestMethod.POST)
	public ResponseEntity<Object> createJournal(@Valid @RequestBody Journal journal) {
		return journalServiceImpl.createBook(journal);
	}

	@RequestMapping(value = "/journals", method = RequestMethod.GET)
	public ResponseEntity<Collection<Book>> getJournals() {
		return  toResponseEntity(journalServiceImpl.getBooks());
	}

	@RequestMapping(value = "/journal/{barcode}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateJournal(@PathVariable("barcode") String barcode, @Valid @RequestBody Journal journal) {
		return journalServiceImpl.updateBook(barcode, journal);
	}

	@RequestMapping(value = "/journal/{barcode}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteJournal(@PathVariable("barcode") String barcode) {
		return journalServiceImpl.deleteBook(barcode);
	}
	
}
