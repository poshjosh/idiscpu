package com.idisc.pu.entities.external;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @(#)UnofficialEmails.java   02-Apr-2015 19:36:58
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@Entity
@Table(name = "unofficial_emails")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnofficialEmails.findAll", query = "SELECT u FROM UnofficialEmails u"),
    @NamedQuery(name = "UnofficialEmails.findByEmailAddress", query = "SELECT u FROM UnofficialEmails u WHERE u.emailAddress = :emailAddress"),
    @NamedQuery(name = "UnofficialEmails.findByLastName", query = "SELECT u FROM UnofficialEmails u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "UnofficialEmails.findByFirstName", query = "SELECT u FROM UnofficialEmails u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "UnofficialEmails.findByDatein", query = "SELECT u FROM UnofficialEmails u WHERE u.datein = :datein"),
    @NamedQuery(name = "UnofficialEmails.findByEmailStatus", query = "SELECT u FROM UnofficialEmails u WHERE u.emailStatus = :emailStatus"),
    @NamedQuery(name = "UnofficialEmails.findByExtraDetails", query = "SELECT u FROM UnofficialEmails u WHERE u.extraDetails = :extraDetails")})
public class UnofficialEmails implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "emailAddress")
    private String emailAddress;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "firstName")
    private String firstName;
    @Basic(optional = false)
    @Column(name = "datein")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datein;
    @Column(name = "emailStatus")
    private Short emailStatus;
    @Column(name = "extraDetails")
    private String extraDetails;

    public UnofficialEmails() {
    }

    public UnofficialEmails(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public UnofficialEmails(String emailAddress, Date datein) {
        this.emailAddress = emailAddress;
        this.datein = datein;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getDatein() {
        return datein;
    }

    public void setDatein(Date datein) {
        this.datein = datein;
    }

    public Short getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(Short emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getExtraDetails() {
        return extraDetails;
    }

    public void setExtraDetails(String extraDetails) {
        this.extraDetails = extraDetails;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (emailAddress != null ? emailAddress.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnofficialEmails)) {
            return false;
        }
        UnofficialEmails other = (UnofficialEmails) object;
        if ((this.emailAddress == null && other.emailAddress != null) || (this.emailAddress != null && !this.emailAddress.equals(other.emailAddress))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.idiscweb.UnofficialEmails[ emailAddress=" + emailAddress + " ]";
    }

}
