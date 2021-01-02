package com.homework.dao;

import java.math.BigDecimal;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

// Using Lombok library annotations for getters, setters, etc.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
	
	//	Using barcode as a unique ID
	@CsvBindByName
	@NotNull
	@Size(min=1)
	private String barcode;

	@CsvBindByName
	@NotNull
	private String name;

	@CsvBindByName
	@NotNull
	private String author;

	@CsvBindByName
	@NotNull
	@PositiveOrZero
	private BigDecimal pricePerUnit; 
	
	@CsvBindByName
	@NotNull
	@PositiveOrZero
	private Integer quantity;
	
}
