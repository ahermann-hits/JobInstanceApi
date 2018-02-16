package de.hits.jobinstance.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "CatalogId", "SystemName", "Name", "Description", "ParentCatalog", "SortOrder", "CatalogType" })
public class CatalogJson {

	@JsonProperty(access = Access.READ_ONLY, value = "CatalogId")
	private int catalogId;

	@JsonProperty(access = Access.READ_ONLY, value = "SystemName")
	private String sysname;
	@JsonProperty("Name")
	private String name;
	@JsonProperty("Description")
	private String description;

	@JsonProperty("ParentCatalog")
	private Integer parentId;
	@JsonProperty("SortOrder")
	private int sortorder;
	@JsonProperty("CatalogType")
	private int catalogTypeId;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public CatalogJson() {}

	/**
	 * 
	 * @param catalogId
	 * @param sysname
	 * @param name
	 * @param description
	 * @param parentId
	 * @param sortorder
	 * @param catalogTypeId
	 */
	public CatalogJson(int catalogId, String sysname, String name, String description, Integer parentId,
			int sortorder, int catalogTypeId) {
		super();
		this.catalogId = catalogId;
		this.sysname = sysname;
		this.name = name;
		this.description = description;
		this.parentId = parentId;
		this.sortorder = sortorder;
		this.catalogTypeId = catalogTypeId;
	}

	/**
	 * @return the catalogId
	 */
	@JsonProperty("CatalogId")
	public int getCatalogId() {
		return catalogId;
	}

	/**
	 * @return the sysname
	 */
	@JsonProperty("SystemName")
	public String getSysname() {
		return sysname;
	}

	/**
	 * @return the name
	 */
	@JsonProperty("Name")
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	@JsonProperty("Description")
	public String getDescription() {
		return description;
	}

	/**
	 * @return the parentId
	 */
	@JsonProperty("ParentCatalog")
	public Integer getParentId() {
		return parentId;
	}

	/**
	 * @return the sortorder
	 */
	@JsonProperty("SortOrder")
	public int getSortorder() {
		return sortorder;
	}

	/**
	 * @return the catalogTypeId
	 */
	public int getCatalogTypeId() {
		return catalogTypeId;
	}

	@Override
	public String toString() {
		return String.format("Catalog[id=%d, sysname='%s', name='%s', parent='%s', type='%s']", getCatalogId(),
				getSysname(), getName(), getParentId(), getCatalogTypeId());
	}
}