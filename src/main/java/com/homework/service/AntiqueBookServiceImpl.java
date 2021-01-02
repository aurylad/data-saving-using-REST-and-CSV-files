package com.homework.service;

import static com.homework.service.Constans.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.homework.dao.AntiqueBook;
import com.homework.dao.CsvReader;
import com.homework.dao.CsvWriter;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

@Service
public class AntiqueBookServiceImpl implements AntiqueBookService {

	private CsvWriter csvWriter;
	private CsvReader csvReader;

	@Autowired
	BarcodeService barcodeService;

	@Override
	public ResponseEntity<Object> createAntiqueBook(AntiqueBook antiqueBook) {
		final Map<String, AntiqueBook> antiqueRepo = retrieveAntiqueBooks();

		if (barcodeService.ifBarcodeExist(antiqueBook.getBarcode())) {
			return new ResponseEntity<>("The record already exists", HttpStatus.CONFLICT);
		} else if (antiqueBook.getBarcode().trim().isEmpty()) {
			return new ResponseEntity<>("The barcode cannot be empty", HttpStatus.BAD_REQUEST);
		} else if (getYearFromDate(antiqueBook.getReleaseYear()) > RELEASE_YEAR_LIMIT) {
			return new ResponseEntity<>("No more recent than 1900", HttpStatus.BAD_REQUEST);
		}

		antiqueBook.setPricePerUnit(antiqueBook.getPricePerUnit().setScale(2, RoundingMode.HALF_EVEN));
		antiqueRepo.put(antiqueBook.getBarcode(), antiqueBook);

		barcodeService.createBarcode(antiqueBook.getBarcode(), antiqueBook.getClass().getSimpleName());
		writeAntiqueBooks(antiqueRepo);

		return ResponseEntity.ok().build();
	}

	@Override
	public Collection<AntiqueBook> getAntiqueBooks() {
		return retrieveAntiqueBooks().values();
	}

	@Override
	public AntiqueBook getAntiqueBookByBarcode(String barcode) {
		return retrieveAntiqueBooks().get(barcode);
	}

	@Override
	public ResponseEntity<Object> updateAntiqueBook(String barcode, AntiqueBook antiqueBook) {
		final Map<String, AntiqueBook> antiqueRepo = retrieveAntiqueBooks();

		if (antiqueRepo.get(barcode) == null) {
			return ResponseEntity.badRequest().build();
		} else if (getYearFromDate(antiqueBook.getReleaseYear()) > RELEASE_YEAR_LIMIT) {
			return new ResponseEntity<>("No more recent than 1900", HttpStatus.BAD_REQUEST);
		}

		antiqueBook.setPricePerUnit(antiqueBook.getPricePerUnit().setScale(2, RoundingMode.HALF_EVEN));

		antiqueBook.setBarcode(barcode);
		antiqueRepo.replace(barcode, antiqueBook);
		writeAntiqueBooks(antiqueRepo);

		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<Void> deleteAntiqueBook(String barcode) {
		final Map<String, AntiqueBook> antiqueRepo = retrieveAntiqueBooks();

		if (antiqueRepo.get(barcode) == null) {
			return ResponseEntity.badRequest().build();
		}

		antiqueRepo.remove(barcode);
		barcodeService.deleteBarcode(barcode);
		writeAntiqueBooks(antiqueRepo);

		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<BigDecimal> calculatePrice(String barcode) {
		AntiqueBook antiqueBook = getAntiqueBookByBarcode(barcode);

		if (antiqueBook == null) {
			return ResponseEntity.badRequest().build();
		}

		BigDecimal unitPrice = antiqueBook.getPricePerUnit();
		int quantity = antiqueBook.getQuantity();
		int releaseYear = getYearFromDate(antiqueBook.getReleaseYear());
		int currentYear = getYearFromDate(new Date());

		// Total Price = Quantity * Price * (Current Year – Release Year) / 10
		BigDecimal totalPrice = unitPrice.multiply(new BigDecimal(quantity))
				.multiply(new BigDecimal(currentYear - releaseYear)).divide(new BigDecimal(10));

		return ResponseEntity.ok(totalPrice);
	}

	private int getYearFromDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		return year;
	}

	// Retrieve from csv
	private Map<String, AntiqueBook> retrieveAntiqueBooks() {
		csvReader = new CsvReader();
		Map<String, AntiqueBook> antiqueBookRepo = new HashMap<>();
		try {
			antiqueBookRepo = csvReader.readAntiqueBooksCsvFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return antiqueBookRepo;
	}

	// Write to csv
	private void writeAntiqueBooks(Map<String, AntiqueBook> booksToSave) {
		csvWriter = new CsvWriter();
		try {
			csvWriter.writeToCsvAntiqueBooks(booksToSave);
		} catch (CsvDataTypeMismatchException e) {
			e.printStackTrace();
		} catch (CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
