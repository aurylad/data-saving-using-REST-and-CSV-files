package com.homework.rest;

import java.util.Collection;

import org.springframework.http.ResponseEntity;

public final class RestUtils {

	static <T> ResponseEntity<Collection<T>> toResponseEntity(Collection<T> collection) {
		if (collection == null || collection.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(collection);
	}

	static <T> ResponseEntity <T> toResponseEntity(T object) {
		if (object == null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(object);
	}

}
