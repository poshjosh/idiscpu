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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 3, 2018 1:26:37 PM
 */
@Entity
@Table(name = "gender")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Gender.findAll", query = "SELECT g FROM Gender g")})
public class Gender implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "genderid")
    private Short genderid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "gender")
    private String gender;
    @OneToMany(mappedBy = "gender", fetch = FetchType.LAZY)
    private List<Feeduser> feeduserList;

    public Gender() {
    }

    public Gender(Short genderid) {
        this.genderid = genderid;
    }

    public Gender(Short genderid, String gender) {
        this.genderid = genderid;
        this.gender = gender;
    }

    public Short getGenderid() {
        return genderid;
    }

    public void setGenderid(Short genderid) {
        this.genderid = genderid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @XmlTransient
    public List<Feeduser> getFeeduserList() {
        return feeduserList;
    }

    public void setFeeduserList(List<Feeduser> feeduserList) {
        this.feeduserList = feeduserList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (genderid != null ? genderid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gender)) {
            return false;
        }
        Gender other = (Gender) object;
        if ((this.genderid == null && other.genderid != null) || (this.genderid != null && !this.genderid.equals(other.genderid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.idisc.pu.entities.Gender[ genderid=" + genderid + " ]";
    }

}
