package com.homework.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.homework.dao.CsvReader;
import com.homework.dao.CsvWriter;
import com.homework.dao.Journal;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

@Service
public class JournalServiceImpl implements JournalService {

	private CsvWriter csvWriter;
	private CsvReader csvReader;

	@Autowired
	BarcodeService barcodeService;

	@Override
	public ResponseEntity<Object> createJournal(Journal journal) {
		final Map<String, Journal> journalRepo = retrieveJournals();

		if (barcodeService.ifBarcodeExist(journal.getBarcode())) {
			return new ResponseEntity<>("The record already exists", HttpStatus.CONFLICT);
		} else if (journal.getBarcode().trim().isEmpty()) {
			return new ResponseEntity<>("The barcode cannot be empty", HttpStatus.BAD_REQUEST);
		}

		// Science Index should be from 1 to 10, it is checked in journal dao
		// using Hibernate validators @Max and @Min
		
		journal.setPricePerUnit(journal.getPricePerUnit().setScale(2, RoundingMode.HALF_EVEN));
		journalRepo.put(journal.getBarcode(), journal);

		barcodeService.createBarcode(journal.getBarcode(), journal.getClass().getSimpleName());
		writeJournals(journalRepo);

		return ResponseEntity.ok().build();
	}

	@Override
	public Collection<Journal> getJournals() {
		return retrieveJournals().values();
	}

	@Override
	public Journal getJournalByBarcode(String barcode) {
		return retrieveJournals().get(barcode);
	}

	@Override
	public ResponseEntity<Void> updateJournal(String barcode, Journal journal) {
		final Map<String, Journal> journalRepo = retrieveJournals();

		if (journalRepo.get(barcode) == null) {
			return ResponseEntity.badRequest().build();
		}

		journal.setPricePerUnit(journal.getPricePerUnit().setScale(2, RoundingMode.HALF_EVEN));
		journal.setBarcode(barcode);
		journalRepo.replace(barcode, journal);
		writeJournals(journalRepo);

		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<Void> deleteJournal(String barcode) {
		final Map<String, Journal> journalRepo = retrieveJournals();

		if (journalRepo.get(barcode) == null) {
			return ResponseEntity.badRequest().build();
		}

		journalRepo.remove(barcode);
		barcodeService.deleteBarcode(barcode);
		writeJournals(journalRepo);

		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<BigDecimal> calculatePrice(String barcode) {
		Journal journal = getJournalByBarcode(barcode);

		if (journal == null) {
			return ResponseEntity.badRequest().build();
		}

		BigDecimal unitPrice = journal.getPricePerUnit();
		int quantity = journal.getQuantity();
		int scienceIndex = journal.getScienceIndex();

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
