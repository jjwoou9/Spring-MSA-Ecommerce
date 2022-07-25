package com.amp.catalogsservice.service;

import com.amp.catalogsservice.jpa.CatalogEntity;
import com.amp.catalogsservice.jpa.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CatalogServiceImpl implements CatalogService {
	CatalogRepository catalogRepository;
	
	@Autowired
	public CatalogServiceImpl(CatalogRepository catalogRepository) {
		this.catalogRepository = catalogRepository;
	}

	@Override
	public Iterable<CatalogEntity> getAllCatalogs() {
		return catalogRepository.findAll();
	}

}
