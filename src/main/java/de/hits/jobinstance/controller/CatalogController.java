package de.hits.jobinstance.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.hits.jobinstance.data.CatalogJson;
import de.hits.jobinstance.service.CatalogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
@RestController
@RequestMapping("/job/api/catalog")
@Api(value = "JobInstance REST API - Catalog")
public class CatalogController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final String MetaCatalog = "metacatalog";

	private Map<Integer, CatalogJson> catalogCache;

	@Autowired
	private CatalogService catalogService;

	@PostConstruct
	private void init() {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#init()");
		}

		this.catalogCache = new HashMap<>();

		this.catalogService.listAllCatalogs().stream().map(catalog -> this.catalogService.convertCatalogToJson(catalog))
				.forEach(catalog -> this.catalogCache.put(catalog.getCatalogId(), catalog));
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get all catalog entries.", notes = "${CatalogController.findAll.notes}", response = List.class)
	public List<CatalogJson> findAll() {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#findAll()");
		}

		return this.catalogCache.entrySet().stream().map(catalog -> catalog.getValue()).collect(Collectors.toList());
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get one catalog entry by id.", notes = "${CatalogController.findOne.notes}", response = CatalogJson.class)
	public CatalogJson findOne(
			@ApiParam(value = "${CatalogController.findOne.id}", required = true)
			@PathVariable("id") int id) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#findOne()");
		}

		return this.catalogCache.get(id);
	}

	@RequestMapping(value = "/type", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get all catalog entries which are a catalog type.", notes = "${CatalogController.findAllTypes.notes}", response = List.class)
	public List<CatalogJson> findAllTypes() {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#findAllTypes()");
		}

		int rootId = this.catalogCache.values().stream()
				.filter(catalog -> this.MetaCatalog.equals(catalog.getSysname())).findFirst().get().getCatalogId();

		return this.catalogCache.values().stream().filter(catalog -> catalog.getCatalogTypeId() == rootId)
				.collect(Collectors.toList());
	}

	@RequestMapping(value = "/type/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get all catalog entry by the catalog type.", notes = "${CatalogController.findByType.notes}", response = List.class)
	public List<CatalogJson> findByType(
			@ApiParam(value = "${CatalogController.findByType.id}", required = true)
			@PathVariable("id") final int id) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#findByType()");
		}

		return this.catalogCache.values().stream().filter(catalog -> catalog.getCatalogTypeId() == id)
				.collect(Collectors.toList());
	}

	@RequestMapping(value = "/name/{sysname}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get one catalog entry by sysname.", notes = "${CatalogController.findByName.notes}", response = CatalogJson.class)
	public CatalogJson findByName(
			@ApiParam(value = "${CatalogController.findByName.sysname}", required = true)
			@PathVariable("sysname") final String sysname) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#findByName()");
		}

		return this.catalogCache.values().stream().filter(catalog -> sysname.equals(catalog.getSysname())).findFirst()
				.get();
	}
}