package com.homework.service;

import java.util.Map;

import com.homework.dao.Barcode;

public interface BarcodeService {

	public abstract void createBarcode(String barcode, String type);
	
	public abstract Map<String, Barcode> getBarcodes();
	
	public abstract void deleteBarcode(String barcode);
	
	public abstract boolean ifBarcodeExist(String barcode);
	
}
