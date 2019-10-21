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
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 3, 2018 1:26:38 PM
 */
@Entity
@Table(name = "feeduser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Feeduser.findAll", query = "SELECT f FROM Feeduser f")})
public class Feeduser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "feeduserid")
    private Integer feeduserid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "emailAddress")
    private String emailAddress;
    @Size(max = 100)
    @Column(name = "lastName")
    private String lastName;
    @Size(max = 100)
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "dateOfBirth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    @Size(max = 25)
    @Column(name = "phoneNumber1")
    private String phoneNumber1;
    @Size(max = 25)
    @Column(name = "phoneNumber2")
    private String phoneNumber2;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 25)
    @Column(name = "fax")
    private String fax;
    @Size(max = 255)
    @Column(name = "image1")
    private String image1;
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
    @JoinColumn(name = "gender", referencedColumnName = "genderid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Gender gender;
    @JoinColumn(name = "howDidYouFindUs", referencedColumnName = "howdidyoufindusid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Howdidyoufindus howDidYouFindUs;
    @JoinColumn(name = "localaddressid", referencedColumnName = "localaddressid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Localaddress localaddressid;
    @OneToMany(mappedBy = "feeduserid", fetch = FetchType.LAZY)
    private List<Installation> installationList;

    public Feeduser() {
    }

    public Feeduser(Integer feeduserid) {
        this.feeduserid = feeduserid;
    }

    public Feeduser(Integer feeduserid, String emailAddress, Date datecreated, Date timemodified) {
        this.feeduserid = feeduserid;
        this.emailAddress = emailAddress;
        this.datecreated = datecreated;
        this.timemodified = timemodified;
    }

    public Integer getFeeduserid() {
        return feeduserid;
    }

    public void setFeeduserid(Integer feeduserid) {
        this.feeduserid = feeduserid;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Howdidyoufindus getHowDidYouFindUs() {
        return howDidYouFindUs;
    }

    public void setHowDidYouFindUs(Howdidyoufindus howDidYouFindUs) {
        this.howDidYouFindUs = howDidYouFindUs;
    }

    public Localaddress getLocaladdressid() {
        return localaddressid;
    }

    public void setLocaladdressid(Localaddress localaddressid) {
        this.localaddressid = localaddressid;
    }

    @XmlTransient
    public List<Installation> getInstallationList() {
        return installationList;
    }

    public void setInstallationList(List<Installation> installationList) {
        this.installationList = installationList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (feeduserid != null ? feeduserid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Feeduser)) {
            return false;
        }
        Feeduser other = (Feeduser) object;
        if ((this.feeduserid == null && other.feeduserid != null) || (this.feeduserid != null && !this.feeduserid.equals(other.feeduserid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.idisc.pu.entities.Feeduser[ feeduserid=" + feeduserid + " ]";
    }

}
