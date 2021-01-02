package com.homework.service;

import java.math.BigDecimal;
import java.util.Collection;

import org.springframework.http.ResponseEntity;

import com.homework.dao.Journal;

public interface JournalService {

	public abstract ResponseEntity<Object> createJournal(Journal journal);

	public abstract ResponseEntity<Void> updateJournal(String barcode, Journal journal);

	public abstract Collection<Journal> getJournals();

	public abstract Journal getJournalByBarcode(String barcode);

	public abstract ResponseEntity<Void> deleteJournal(String barcode);
	
	public abstract ResponseEntity<BigDecimal> calculatePrice(String barcode);
}
