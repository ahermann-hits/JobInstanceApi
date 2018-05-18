package de.hits.jobinstance.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.hits.jobinstance.domain.CatalogEntity;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
public interface CatalogRepository extends CrudRepository<CatalogEntity, Long> {

	/**
	 * 
	 * @param catalogId
	 * @return
	 */
	CatalogEntity findByCatalogId(int catalogId);

	/**
	 * 
	 * @param sysname
	 * @return
	 */
	CatalogEntity findBySysname(String sysname);

	/**
	 * 
	 * @param catalogTypeId
	 * @return
	 */
	List<CatalogEntity> findByCatalogTypeIdOrderBySortorderAsc(long catalogTypeId);

	/**
	 * 
	 * @param catalogTypeId
	 * @param sysname
	 * @return
	 */
	CatalogEntity findByCatalogTypeIdAndSysname(long catalogTypeId, String sysname);
}