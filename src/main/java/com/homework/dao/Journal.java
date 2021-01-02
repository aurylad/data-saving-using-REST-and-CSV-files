package com.homework.dao;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Journal extends Book {
	
	@CsvBindByName
	@Min(1) @Max(10)
	private Integer scienceIndex;
}
