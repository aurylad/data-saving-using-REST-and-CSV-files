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

import com.homework.dao.Journal;
import com.homework.service.JournalService;

/*
 * Implemented GET ALL, CREATE, UPDATE and DELETE REST calls.
 * GET BY BARCODE and GET PRICE calls are implemented in GetAnyBookController,
 * these methods not related with specific type, they work independently.
 * */

@RestController
@RequestMapping("/api")
public class JournalsController {

	@Autowired
	JournalService journalService;
	
	@RequestMapping(value = "/journal", method = RequestMethod.POST)
	public ResponseEntity<Object> createJournal(@Valid @RequestBody Journal journal) {
		return journalService.createJournal(journal);
	}
	
	@RequestMapping(value = "/journals", method = RequestMethod.GET)
	public ResponseEntity<Collection<Journal>> getJournals() {
		return  toResponseEntity(journalService.getJournals());
	}

	@RequestMapping(value = "/journal/{barcode}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateJournal(@PathVariable("barcode") String barcode, @Valid @RequestBody Journal journal) {
		return journalService.updateJournal(barcode, journal);
	}
	
	@RequestMapping(value = "/journal/{barcode}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteJournal(@PathVariable("barcode") String barcode) {
		return journalService.deleteJournal(barcode);
	}
	
}
