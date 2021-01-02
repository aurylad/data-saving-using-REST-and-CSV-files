package com.homework.dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

/*
 * Using OpenCsv parser library
 */

public class CsvUtils {

	static private Writer writer;

	static <T> CsvToBean<T> openCsvToBean(Class<T> type, String path) throws IOException {
		Reader reader = new InputStreamReader(new FileInputStream(path), "UTF-8");
		CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader).withType(type).withSeparator(';').build();
		return csvToBean;
	}

	static <T> StatefulBeanToCsv<T> beanToOpenCsv(Class<T> type, String path) throws IOException {
		writer = new OutputStreamWriter(new FileOutputStream(path));
		StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer).withSeparator(';').build();
		return beanToCsv;
	}

	static void closeWriter() throws IOException {
		if (writer != null) {
			writer.close();
		}
	}

}
