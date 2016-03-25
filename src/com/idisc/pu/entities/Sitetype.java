/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idisc.pu.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Josh
 */
@Entity
@Table(name = "sitetype")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sitetype.findAll", query = "SELECT s FROM Sitetype s"),
    @NamedQuery(name = "Sitetype.findBySitetypeid", query = "SELECT s FROM Sitetype s WHERE s.sitetypeid = :sitetypeid"),
    @NamedQuery(name = "Sitetype.findBySitetype", query = "SELECT s FROM Sitetype s WHERE s.sitetype = :sitetype")})
public class Sitetype implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "sitetypeid")
    private Short sitetypeid;
    @Basic(optional = false)
    @Column(name = "sitetype")
    private String sitetype;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sitetypeid")
    private List<Site> siteList;

    public Sitetype() {
    }

    public Sitetype(Short sitetypeid) {
        this.sitetypeid = sitetypeid;
    }

    public Sitetype(Short sitetypeid, String sitetype) {
        this.sitetypeid = sitetypeid;
        this.sitetype = sitetype;
    }

    public Short getSitetypeid() {
        return sitetypeid;
    }

    public void setSitetypeid(Short sitetypeid) {
        this.sitetypeid = sitetypeid;
    }

    public String getSitetype() {
        return sitetype;
    }

    public void setSitetype(String sitetype) {
        this.sitetype = sitetype;
    }

    @XmlTransient
    public List<Site> getSiteList() {
        return siteList;
    }

    public void setSiteList(List<Site> siteList) {
        this.siteList = siteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sitetypeid != null ? sitetypeid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sitetype)) {
            return false;
        }
        Sitetype other = (Sitetype) object;
        if ((this.sitetypeid == null && other.sitetypeid != null) || (this.sitetypeid != null && !this.sitetypeid.equals(other.sitetypeid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.idisc.pu.entities.Sitetype[ sitetypeid=" + sitetypeid + " ]";
    }
    
}
