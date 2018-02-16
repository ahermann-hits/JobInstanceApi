package de.hits.jobinstance.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.hits.jobinstance.data.CatalogJson;
import de.hits.jobinstance.domain.Catalog;
import de.hits.jobinstance.repository.CatalogRepository;
import de.hits.jobinstance.service.CatalogService;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
@Service
public class CatalogServiceImpl implements CatalogService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private CatalogRepository catalogRepo;

	@Autowired
	public CatalogServiceImpl(CatalogRepository catalogRepo) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(getClass().getSimpleName() + "ManageCatalogServiceImpl()");
		}

		this.catalogRepo = catalogRepo;
	}

	@Override
	public List<Catalog> listAllCatalogs() {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#listAllCatalogs()");
		}

		List<Catalog> catalogs = new ArrayList<>();
		CollectionUtils.addAll(catalogs, this.catalogRepo.findAll().iterator());
		return catalogs;
	}

	@Override
	public Catalog getCatalogById(int id) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#getCatalogById()");
		}

		return this.catalogRepo.findByCatalogId(id);
	}

	@Override
	public Catalog getCatalogBySysname(String sysname) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#getCatalogBySysname()");
		}

		return this.catalogRepo.findBySysname(sysname);
	}

	@Override
	public List<Catalog> listByCatalogType(int catalogTypeId) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#listByCatalogType()");
		}

		return this.catalogRepo.findByCatalogTypeIdOrderBySortorderAsc(catalogTypeId);
	}

	@Override
	public Catalog getCatalogByTypeAndSysname(int catalogTypeId, String sysname) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#getCatalogByTypeAndSysname()");
		}

		return this.catalogRepo.findByCatalogTypeIdAndSysname(catalogTypeId, sysname);
	}

	@Override
	public CatalogJson convertCatalogToJson(Catalog catalog) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#convertCatalogToJson()");
		}

		CatalogJson response = null;

		if (catalog != null) {
			response = new CatalogJson(catalog.getCatalogId(), catalog.getSysname(), catalog.getName(),
					catalog.getDescription(), catalog.getParentId(), catalog.getSortorder(),
					catalog.getCatalogTypeId());
		}

		return response;
	}
}