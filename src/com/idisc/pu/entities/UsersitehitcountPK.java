/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idisc.pu.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Josh
 */
@Embeddable
public class UsersitehitcountPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "installationid")
    private int installationid;
    @Basic(optional = false)
    @Column(name = "siteid")
    private int siteid;

    public UsersitehitcountPK() {
    }

    public UsersitehitcountPK(int installationid, int siteid) {
        this.installationid = installationid;
        this.siteid = siteid;
    }

    public int getInstallationid() {
        return installationid;
    }

    public void setInstallationid(int installationid) {
        this.installationid = installationid;
    }

    public int getSiteid() {
        return siteid;
    }

    public void setSiteid(int siteid) {
        this.siteid = siteid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) installationid;
        hash += (int) siteid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsersitehitcountPK)) {
            return false;
        }
        UsersitehitcountPK other = (UsersitehitcountPK) object;
        if (this.installationid != other.installationid) {
            return false;
        }
        if (this.siteid != other.siteid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.idisc.pu.entities.UsersitehitcountPK[ installationid=" + installationid + ", siteid=" + siteid + " ]";
    }
    
}
