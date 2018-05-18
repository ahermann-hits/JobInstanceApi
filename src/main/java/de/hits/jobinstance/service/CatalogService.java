package de.hits.jobinstance.service;

import java.util.List;

import de.hits.jobinstance.data.CatalogJson;
import de.hits.jobinstance.domain.CatalogEntity;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
public interface CatalogService {

	/**
	 * Findet eine Liste von Katalogeinträgen.
	 * 
	 * @return Gibt eine Liste von {@link CatalogEntity Katalogeinträgen} zurück, falls
	 *         vorhanden.
	 */
	List<CatalogEntity> listAllCatalogs();

	/**
	 * Findet einen Katalogeintrag anhand seiner Id.
	 * 
	 * @param id
	 *            die Id des zu findenden Katalogeintrags.
	 * @return Gibt einen {@link CatalogEntity Katalogeintrag} zurück, falls vorhanden.
	 */
	CatalogEntity getCatalogById(int id);

	/**
	 * Findet einen Katalogeintrag anhand des übergebenen Systemnamens.
	 * 
	 * @param sysname
	 *            der Systemname des zu findenden Katalogeintrags.
	 * @return Gibt einen {@link CatalogEntity Katalogeintrag} zurück, falls vorhanden.
	 */
	CatalogEntity getCatalogBySysname(String sysname);

	/**
	 * Findet eine Liste von Katalogeinträgen zu einem Katalogtyp.
	 * 
	 * @param catalogTypeId
	 *            die ID des Katalogeintrags, der den Typ darstellt.
	 * @return Gibt eine Liste von {@link CatalogEntity Katalogeinträgen} zurück, falls
	 *         vorhanden.
	 */
	List<CatalogEntity> listByCatalogType(int catalogTypeId);

	/**
	 * Findet einen Katalogeintrag anhand eines Katalogtyps und des übergebenen
	 * Systemnamens.
	 * 
	 * @param catalogTypeId
	 *            die ID des Katalogeintrags, der den Typ darstellt.
	 * @param sysname
	 *            der Systemname des zu findenden Katalogeintrags.
	 * @return Gibt einen {@link CatalogEntity Katalogeintrag} zurück, falls vorhanden.
	 */
	CatalogEntity getCatalogByTypeAndSysname(int catalogTypeId, String sysname);

	/**
	 * 
	 * @param catalog
	 * @return
	 */
	CatalogJson convertCatalogToJson(CatalogEntity catalog);
}