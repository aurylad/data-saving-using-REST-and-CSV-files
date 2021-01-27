package com.homework.dao;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import static com.homework.dao.CsvUtils.openCsvToBean;

/*
 * Csv files used in project
 * 	- books.csv
 *	- journals.csv
 *	- antiquebooks.csv
 *	- barcodes.csv
 */

public class CsvReader {
	
	// Book
	public Map<String, Book> readBooksCsvFile() throws IOException {
		// By default opencsv returns ArrayList
		// Converting List to Map (Java 8 solution)
		Map<String, Book> booksMap = openCsvToBean(Book.class, "books.csv").parse().stream()
			      .collect(Collectors.toMap(Book::getBarcode, book -> book));
		
		return booksMap;
	}
	
	// Antique Book
	public Map<String, AntiqueBook> readAntiqueBooksCsvFile() throws IOException {
		Map<String, AntiqueBook> booksMap = openCsvToBean(AntiqueBook.class, "antiquebooks.csv").parse().stream()
			      .collect(Collectors.toMap(AntiqueBook::getBarcode, antiqueBook -> antiqueBook));

		return booksMap;
	}
	
	// Journals
	public Map<String, Journal> readJournalsCsvFile() throws IOException {
		Map<String, Journal> journalsMap = openCsvToBean(Journal.class, "journals.csv").parse().stream()
				.collect(Collectors.toMap(Journal::getBarcode, journal -> journal));
		
		return journalsMap;
	}
	
	// Barcodes
	public Map<String, Barcode> readBarcodesCsvFile() throws IOException {
		Map<String, Barcode> barcodesMap = openCsvToBean(Barcode.class, "barcodes.csv").parse().stream()
			      .collect(Collectors.toMap(Barcode::getBarcode, barcode -> barcode));
		
		return barcodesMap;
	}
	
}
