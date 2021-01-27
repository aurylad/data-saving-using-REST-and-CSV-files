package com.homework.service;

import java.math.BigDecimal;
import java.util.Collection;

import com.homework.dao.Book;
import org.springframework.http.ResponseEntity;

import com.homework.dao.AntiqueBook;

public interface BooksService {

	public abstract ResponseEntity<Object> createBook(Book book);

	public abstract Collection<Book> getBooks();

	public abstract Book getByBarcode(String barcode);

	public abstract ResponseEntity<Object> updateBook(String barcode, Book book);

	public abstract ResponseEntity<Void> deleteBook(String barcode);
	
	public abstract ResponseEntity<BigDecimal> calculatePrice(String barcode);
}
