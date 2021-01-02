package com.homework.service;

import java.math.BigDecimal;
import java.util.Collection;

import org.springframework.http.ResponseEntity;

import com.homework.dao.AntiqueBook;

public interface AntiqueBookService {

	public abstract ResponseEntity<Object> createAntiqueBook(AntiqueBook antiqueBook);
	
	public abstract ResponseEntity<Object> updateAntiqueBook(String barcode, AntiqueBook antiqueBook);
	
	public abstract Collection<AntiqueBook> getAntiqueBooks();
	
	public abstract AntiqueBook getAntiqueBookByBarcode(String barcode);
	
	public abstract ResponseEntity<Void> deleteAntiqueBook(String barcode);
	
	public abstract ResponseEntity<BigDecimal> calculatePrice(String barcode);
}
