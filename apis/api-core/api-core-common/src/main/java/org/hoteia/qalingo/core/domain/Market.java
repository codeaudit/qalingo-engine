/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.8.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2014
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;

@Entity
@Table(name="TECO_MARKET")
public class Market extends AbstractEntity<Market> {

	/**
	 * Generated UID
	 */
    private static final long serialVersionUID = 5759002146568820577L;

    public static final String CACHE_NAME = "web_cache_market";

    public final static String MARKET_ATTRIBUTE_DOMAIN_NAME = "MARKET_DOMAIN_NAME";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Version
    @Column(name = "VERSION", nullable = false, columnDefinition = "int(11) default 1")
    private int version;

    @Column(name = "CODE", unique = true, nullable = false)
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    @Lob
    private String description;

    @Column(name = "IS_DEFAULT", nullable = false, columnDefinition = "tinyint(1) default 0")
    private boolean isDefault;

    @Column(name = "THEME")
    private String theme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MARKETPLACE_ID", insertable = true, updatable = true)
    private MarketPlace marketPlace;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MARKET_ID")
    private Set<MarketArea> marketAreas = new HashSet<MarketArea>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MARKET_ID")
    private Set<MarketAttribute> marketAttributes = new HashSet<MarketAttribute>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_CREATE")
    private Date dateCreate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_UPDATE")
    private Date dateUpdate;

    public Market() {
        this.dateCreate = new Date();
        this.dateUpdate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public MarketPlace getMarketPlace() {
        return marketPlace;
    }

    public void setMarketPlace(MarketPlace marketPlace) {
        this.marketPlace = marketPlace;
    }

    public Set<MarketArea> getMarketAreas() {
        return marketAreas;
    }

    public MarketArea getMarketArea(String marketAreaCode) {
        MarketArea marketAreaToReturn = null;
        if (marketAreas != null 
                && Hibernate.isInitialized(marketAreas)) {
            for (Iterator<MarketArea> iterator = marketAreas.iterator(); iterator.hasNext();) {
                MarketArea marketArea = (MarketArea) iterator.next();
                if (marketArea.getCode().equalsIgnoreCase(marketAreaCode)) {
                    marketAreaToReturn = marketArea;
                }
            }
        }
        return marketAreaToReturn;
    }

    public void setMarketAreas(Set<MarketArea> marketAreas) {
        this.marketAreas = marketAreas;
    }

    public MarketArea getDefaultMarketArea() {
        MarketArea defaultMarketArea = null;
        if (marketAreas != null
                && Hibernate.isInitialized(marketAreas)) {
            for (Iterator<MarketArea> iterator = marketAreas.iterator(); iterator.hasNext();) {
                MarketArea marketArea = (MarketArea) iterator.next();
                if (marketArea.isOpened() && marketArea.isDefault()) {
                    defaultMarketArea = marketArea;
                    break;
                }
            }
            if (defaultMarketArea == null) {
                for (Iterator<MarketArea> iterator = marketAreas.iterator(); iterator.hasNext();) {
                    MarketArea marketArea = (MarketArea) iterator.next();
                    if (marketArea.isOpened()) {
                        defaultMarketArea = marketArea;
                        break;
                    }
                }
            }
        }
        return defaultMarketArea;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getDomainName(String contextNameValue) {
        return getAttributeValueString(MARKET_ATTRIBUTE_DOMAIN_NAME, contextNameValue);
    }

    private String getAttributeValueString(String attributeDefinitionCode, String contextNameValue) {
        if (marketAttributes != null 
                && Hibernate.isInitialized(marketAttributes)) {
            for (Iterator<MarketAttribute> iterator = marketAttributes.iterator(); iterator.hasNext();) {
                MarketAttribute marketAttribute = (MarketAttribute) iterator.next();
                AttributeDefinition attributeDefinition = marketAttribute.getAttributeDefinition();
                if (StringUtils.isNotEmpty(marketAttribute.getContext()) && marketAttribute.getContext().equals(contextNameValue) && attributeDefinition.getCode().equals(attributeDefinitionCode)) {
                    return (String) marketAttribute.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((dateCreate == null) ? 0 : dateCreate.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object sourceObj) {
        Object obj = deproxy(sourceObj);
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Market other = (Market) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (dateCreate == null) {
            if (other.dateCreate != null)
                return false;
        } else if (!dateCreate.equals(other.dateCreate))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Market [id=" + id + ", version=" + version + ", name=" + name + ", description=" + description + ", isDefault=" + isDefault + ", code=" + code + ", theme=" + theme + ", dateCreate="
                + dateCreate + ", dateUpdate=" + dateUpdate + "]";
    }

}