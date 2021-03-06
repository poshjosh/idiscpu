/*
 * Copyright 2018 NUROX Ltd.
 *
 * Licensed under the NUROX Ltd Software License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.looseboxes.com/legal/licenses/software.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idisc.pu.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 3, 2018 1:26:38 PM
 */
@Entity
@Table(name = "applaunchlog")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Applaunchlog.findAll", query = "SELECT a FROM Applaunchlog a")})
public class Applaunchlog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "applaunchlogid")
    private Integer applaunchlogid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "launchtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date launchtime;
    @JoinColumn(name = "installationid", referencedColumnName = "installationid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Installation installationid;

    public Applaunchlog() {
    }

    public Applaunchlog(Integer applaunchlogid) {
        this.applaunchlogid = applaunchlogid;
    }

    public Applaunchlog(Integer applaunchlogid, Date launchtime) {
        this.applaunchlogid = applaunchlogid;
        this.launchtime = launchtime;
    }

    public Integer getApplaunchlogid() {
        return applaunchlogid;
    }

    public void setApplaunchlogid(Integer applaunchlogid) {
        this.applaunchlogid = applaunchlogid;
    }

    public Date getLaunchtime() {
        return launchtime;
    }

    public void setLaunchtime(Date launchtime) {
        this.launchtime = launchtime;
    }

    public Installation getInstallationid() {
        return installationid;
    }

    public void setInstallationid(Installation installationid) {
        this.installationid = installationid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (applaunchlogid != null ? applaunchlogid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Applaunchlog)) {
            return false;
        }
        Applaunchlog other = (Applaunchlog) object;
        if ((this.applaunchlogid == null && other.applaunchlogid != null) || (this.applaunchlogid != null && !this.applaunchlogid.equals(other.applaunchlogid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.idisc.pu.entities.Applaunchlog[ applaunchlogid=" + applaunchlogid + " ]";
    }

}
