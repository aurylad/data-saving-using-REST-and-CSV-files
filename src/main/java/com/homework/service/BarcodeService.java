package com.homework.service;

import java.util.Map;

import com.homework.dao.Barcode;

public interface BarcodeService {

	public abstract void createBarcode(String barcode, String type);
	
	public abstract Map<String, Barcode> getBarcodes();

	public  abstract  Barcode getBarcode(String barcode);
	
	public abstract void deleteBarcode(String barcode);

}
