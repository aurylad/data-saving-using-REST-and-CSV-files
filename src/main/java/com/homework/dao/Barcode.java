package com.homework.dao;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Barcode {

	@CsvBindByName
	@NotNull
	@Size(min=1)
	private String barcode;
	
	@CsvBindByName
	@NotNull
	private String type;
	
}
