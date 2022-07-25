package com.amp.catalogsservice.service;

import com.amp.catalogsservice.jpa.CatalogEntity;

public interface CatalogService {
	Iterable<CatalogEntity> getAllCatalogs();
}
