package com.homework.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.homework.dao.CsvUtils.beanToOpenCsv;
import static com.homework.dao.CsvUtils.closeWriter;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

/*
 * Csv files used in project
 * 	- books.csv
 *	- journals.csv
 *	- antiquebooks.csv
 *	- barcodes.csv
 */

public class CsvWriter {
	
	// Csv file headers are created by dao objects attributes annotated with @CsvBindByName
	
	// Books	
	public void writeToCsv(Map<String, Book> bookRepo) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		List<Book> list = new ArrayList<Book>(bookRepo.values());
		beanToOpenCsv(Book.class, "books.csv").write(list);
		closeWriter();
	}
	
	// Antique Books
	public void writeToCsvAntiqueBooks(Map<String, AntiqueBook> bookRepo) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		List<AntiqueBook> list = new ArrayList<AntiqueBook>(bookRepo.values());
		beanToOpenCsv(AntiqueBook.class, "antiquebooks.csv").write(list);
		closeWriter();
	}
	
	// Journals
	public void writeToCsvJournals(Map<String, Journal> journalRepo) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		List<Journal> list = new ArrayList<Journal>(journalRepo.values());
		beanToOpenCsv(Journal.class, "journals.csv").write(list);
		closeWriter();
	}
	
	// Barcodes
	public void writeToCsvBarcodes(Map<String, Barcode> barcodeRepo) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		List<Barcode> list = new ArrayList<Barcode>(barcodeRepo.values());
		beanToOpenCsv(Barcode.class, "barcodes.csv").write(list);
		closeWriter();
	}
	
}
