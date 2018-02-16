package de.hits.jobinstance.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
@Entity
@Table(name = "catalog", schema = "manage", uniqueConstraints = { @UniqueConstraint(columnNames = { "SYSNAME" }) })
public class Catalog {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATALOG_SEQ")
	@SequenceGenerator(name = "CATALOG_SEQ", sequenceName = "CATALOG_SEQ", initialValue = 1)
	@Column(name = "CATALOG_ID")
	private int catalogId;

	@Column(name = "SYSNAME", nullable = false, insertable = true, updatable = false, length = 40)
	private String sysname;
	@Column(name = "NAME", nullable = false, insertable = true, updatable = true, length = 64)
	private String name;
	@Column(name = "DESCRIPTION", nullable = true, insertable = true, updatable = true, length = 512)
	private String description;

	@Column(name = "PARENT_ID", unique = false, nullable = true, updatable = true)
	private Integer parentId;
	@Column(name = "SORTORDER", nullable = false, insertable = true, updatable = true)
	private int sortorder;
	@Column(name = "CATALOG_TYPE_ID", unique = false, nullable = false, updatable = true)
	private int catalogTypeId;

	private Catalog() {}

	/**
	 * @return the catalogId
	 */
	public int getCatalogId() {
		return catalogId;
	}

	/**
	 * @param catalogId
	 *            the catalogId to set
	 */
	public void setCatalogId(final int catalogId) {
		this.catalogId = catalogId;
	}

	/**
	 * @return the sysname
	 */
	public String getSysname() {
		return sysname;
	}

	/**
	 * @param sysname
	 *            the sysname to set
	 */
	public void setSysname(final String sysname) {
		this.sysname = sysname;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the parentId
	 */
	public Integer getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(final Integer parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the sortorder
	 */
	public int getSortorder() {
		return sortorder;
	}

	/**
	 * @param sortorder
	 *            the sortorder to set
	 */
	public void setSortorder(final int sortorder) {
		this.sortorder = sortorder;
	}

	/**
	 * @return the catalogTypeId
	 */
	public int getCatalogTypeId() {
		return catalogTypeId;
	}

	/**
	 * @param catalogTypeId
	 *            the catalogTypeId to set
	 */
	public void setCatalogTypeId(final int catalogTypeId) {
		this.catalogTypeId = catalogTypeId;
	}

	@Override
	public boolean equals(final Object catalog) {
		boolean equals = super.equals(catalog);
		if (!equals && catalog instanceof Catalog) {
			final Catalog toCompare = (Catalog) catalog;

			final String toCompareSysname = toCompare.getSysname();
			final String toCompareName = toCompare.getName();
			final String toCompareDescription = toCompare.getDescription();
			final Integer toCompareParent = toCompare.getParentId();
			final int toCompareSortorder = toCompare.getSortorder();
			final int toCompareCatalogType = toCompare.getCatalogTypeId();

			final boolean equalSysname = toCompareSysname != null && toCompareSysname.equals(getSysname());
			final boolean equalName = toCompareName != null && toCompareName.equals(getName());
			final boolean equalDescription = toCompareDescription != null
					&& toCompareDescription.equals(getDescription());
			final boolean equalParent = (toCompareParent == null && getParentId() == null)
					|| (toCompareParent != null && toCompareParent.equals(getParentId()));
			final boolean equalSortorder = toCompareSortorder == getSortorder();
			final boolean equalCatalogType = toCompareCatalogType == getCatalogTypeId();

			equals = equalSysname && equalName && equalDescription && equalParent && equalSortorder && equalCatalogType;
		}

		return equals;
	}

	@Override
	public String toString() {
		return String.format("Catalog[id=%d, sysname='%s', name='%s', parent='%s', type='%s']",
				getCatalogId(), getSysname(), getName(), getParentId(), getCatalogTypeId());
	}
}