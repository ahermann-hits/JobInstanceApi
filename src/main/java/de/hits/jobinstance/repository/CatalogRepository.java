package de.hits.jobinstance.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.hits.jobinstance.domain.Catalog;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
public interface CatalogRepository extends CrudRepository<Catalog, Long> {

	/**
	 * 
	 * @param catalogId
	 * @return
	 */
	Catalog findByCatalogId(int catalogId);

	/**
	 * 
	 * @param sysname
	 * @return
	 */
	Catalog findBySysname(String sysname);

	/**
	 * 
	 * @param catalogTypeId
	 * @return
	 */
	List<Catalog> findByCatalogTypeIdOrderBySortorderAsc(long catalogTypeId);

	/**
	 * 
	 * @param catalogTypeId
	 * @param sysname
	 * @return
	 */
	Catalog findByCatalogTypeIdAndSysname(long catalogTypeId, String sysname);
}