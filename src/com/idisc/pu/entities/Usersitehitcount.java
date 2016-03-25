/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idisc.pu.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Josh
 */
@Entity
@Table(name = "usersitehitcount")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usersitehitcount.findAll", query = "SELECT u FROM Usersitehitcount u"),
    @NamedQuery(name = "Usersitehitcount.findByInstallationid", query = "SELECT u FROM Usersitehitcount u WHERE u.usersitehitcountPK.installationid = :installationid"),
    @NamedQuery(name = "Usersitehitcount.findBySiteid", query = "SELECT u FROM Usersitehitcount u WHERE u.usersitehitcountPK.siteid = :siteid"),
    @NamedQuery(name = "Usersitehitcount.findByHitcount", query = "SELECT u FROM Usersitehitcount u WHERE u.hitcount = :hitcount"),
    @NamedQuery(name = "Usersitehitcount.findByDatecreated", query = "SELECT u FROM Usersitehitcount u WHERE u.datecreated = :datecreated"),
    @NamedQuery(name = "Usersitehitcount.findByTimemodified", query = "SELECT u FROM Usersitehitcount u WHERE u.timemodified = :timemodified")})
public class Usersitehitcount implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsersitehitcountPK usersitehitcountPK;
    @Basic(optional = false)
    @Column(name = "hitcount")
    private int hitcount;
    @Basic(optional = false)
    @Column(name = "datecreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecreated;
    @Basic(optional = false)
    @Column(name = "timemodified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timemodified;
    @JoinColumn(name = "installationid", referencedColumnName = "installationid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Installation installation;
    @JoinColumn(name = "siteid", referencedColumnName = "siteid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Site site;

    public Usersitehitcount() {
    }

    public Usersitehitcount(UsersitehitcountPK usersitehitcountPK) {
        this.usersitehitcountPK = usersitehitcountPK;
    }

    public Usersitehitcount(UsersitehitcountPK usersitehitcountPK, int hitcount, Date datecreated, Date timemodified) {
        this.usersitehitcountPK = usersitehitcountPK;
        this.hitcount = hitcount;
        this.datecreated = datecreated;
        this.timemodified = timemodified;
    }

    public Usersitehitcount(int installationid, int siteid) {
        this.usersitehitcountPK = new UsersitehitcountPK(installationid, siteid);
    }

    public UsersitehitcountPK getUsersitehitcountPK() {
        return usersitehitcountPK;
    }

    public void setUsersitehitcountPK(UsersitehitcountPK usersitehitcountPK) {
        this.usersitehitcountPK = usersitehitcountPK;
    }

    public int getHitcount() {
        return hitcount;
    }

    public void setHitcount(int hitcount) {
        this.hitcount = hitcount;
    }

    public Date getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(Date datecreated) {
        this.datecreated = datecreated;
    }

    public Date getTimemodified() {
        return timemodified;
    }

    public void setTimemodified(Date timemodified) {
        this.timemodified = timemodified;
    }

    public Installation getInstallation() {
        return installation;
    }

    public void setInstallation(Installation installation) {
        this.installation = installation;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usersitehitcountPK != null ? usersitehitcountPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usersitehitcount)) {
            return false;
        }
        Usersitehitcount other = (Usersitehitcount) object;
        if ((this.usersitehitcountPK == null && other.usersitehitcountPK != null) || (this.usersitehitcountPK != null && !this.usersitehitcountPK.equals(other.usersitehitcountPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.idisc.pu.entities.Usersitehitcount[ usersitehitcountPK=" + usersitehitcountPK + " ]";
    }
    
}
