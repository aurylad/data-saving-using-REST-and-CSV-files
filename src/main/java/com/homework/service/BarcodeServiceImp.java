package com.homework.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.homework.dao.Barcode;
import com.homework.dao.CsvReader;
import com.homework.dao.CsvWriter;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

@Service
public class BarcodeServiceImp implements BarcodeService {

	CsvWriter csvWriter;
	CsvReader csvReader;

	@Override
	public void createBarcode(String barcode, String type) {
		final Map<String, Barcode> map = retrieveBarcodes();
		
		final Barcode bar = new Barcode();
		bar.setBarcode(barcode);
		bar.setType(type);
		
		map.put(barcode, bar);
		writeBarcodes(map);
	}

	@Override
	public Map<String, Barcode> getBarcodes() {
		return retrieveBarcodes();
	}

	@Override
	public Barcode getBarcode(String barcode) {
		return retrieveBarcodes().get(barcode);
	}

	@Override
	public void deleteBarcode(String barcode) {
		final Map<String, Barcode> barcodeRepo = retrieveBarcodes();
		barcodeRepo.remove(barcode);
		writeBarcodes(barcodeRepo);	
	}
	
	// Write to csv
	private void writeBarcodes(Map<String, Barcode> barcodesToSave) {
		csvWriter = new CsvWriter();
		try {
			csvWriter.writeToCsvBarcodes(barcodesToSave);
		} catch (CsvDataTypeMismatchException e) {
			e.printStackTrace();
		} catch (CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Retrieve from csc
	private Map<String, Barcode> retrieveBarcodes() {
		csvReader = new CsvReader();
		Map<String, Barcode> bookRepo = new HashMap<>();
		try {
			bookRepo = csvReader.readBarcodesCsvFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bookRepo;
	}


}
