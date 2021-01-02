package com.homework.service;

import java.math.BigDecimal;
import java.util.Collection;

import org.springframework.http.ResponseEntity;

import com.homework.dao.Book;

public interface BookService {

	public abstract ResponseEntity<Object> createBook(Book book);

	public abstract ResponseEntity<Void> updateBook(String barcode, Book book);

	public abstract Collection<Book> getBooks();
	
	public abstract Book getBookByBarcode(String barcode);
	
	public abstract ResponseEntity<Void> deleteBook(String barcode);
	
	public abstract ResponseEntity<BigDecimal> calculatePrice(String barcode);
}
