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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 3, 2018 1:26:37 PM
 */
@Entity
@Table(name = "extractedemail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Extractedemail.findAll", query = "SELECT e FROM Extractedemail e")})
public class Extractedemail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "extractedemailid")
    private Integer extractedemailid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "emailAddress")
    private String emailAddress;
    @Size(max = 100)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datecreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecreated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "timemodified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timemodified;
    @Size(max = 500)
    @Column(name = "extradetails")
    private String extradetails;
    @JoinColumn(name = "installationid", referencedColumnName = "installationid")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Installation installationid;
    @JoinColumn(name = "emailstatus", referencedColumnName = "emailstatusid")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Emailstatus emailstatus;

    public Extractedemail() {
    }

    public Extractedemail(Integer extractedemailid) {
        this.extractedemailid = extractedemailid;
    }

    public Extractedemail(Integer extractedemailid, String emailAddress, Date datecreated, Date timemodified) {
        this.extractedemailid = extractedemailid;
        this.emailAddress = emailAddress;
        this.datecreated = datecreated;
        this.timemodified = timemodified;
    }

    public Integer getExtractedemailid() {
        return extractedemailid;
    }

    public void setExtractedemailid(Integer extractedemailid) {
        this.extractedemailid = extractedemailid;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getExtradetails() {
        return extradetails;
    }

    public void setExtradetails(String extradetails) {
        this.extradetails = extradetails;
    }

    public Installation getInstallationid() {
        return installationid;
    }

    public void setInstallationid(Installation installationid) {
        this.installationid = installationid;
    }

    public Emailstatus getEmailstatus() {
        return emailstatus;
    }

    public void setEmailstatus(Emailstatus emailstatus) {
        this.emailstatus = emailstatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (extractedemailid != null ? extractedemailid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Extractedemail)) {
            return false;
        }
        Extractedemail other = (Extractedemail) object;
        if ((this.extractedemailid == null && other.extractedemailid != null) || (this.extractedemailid != null && !this.extractedemailid.equals(other.extractedemailid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.idisc.pu.entities.Extractedemail[ extractedemailid=" + extractedemailid + " ]";
    }

}
