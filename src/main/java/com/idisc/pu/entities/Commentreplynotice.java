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
@Table(name = "commentreplynotice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Commentreplynotice.findAll", query = "SELECT c FROM Commentreplynotice c")})
public class Commentreplynotice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "commentreplynoticeid")
    private Integer commentreplynoticeid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dateusernotified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateusernotified;
    @Column(name = "dateuserread")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateuserread;
    @Basic(optional = false)
    @NotNull
    @Column(name = "timemodified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timemodified;
    @Size(max = 500)
    @Column(name = "extradetails")
    private String extradetails;
    @JoinColumn(name = "commentid", referencedColumnName = "commentid")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Comment commentid;
    @JoinColumn(name = "installationid", referencedColumnName = "installationid")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Installation installationid;

    public Commentreplynotice() {
    }

    public Commentreplynotice(Integer commentreplynoticeid) {
        this.commentreplynoticeid = commentreplynoticeid;
    }

    public Commentreplynotice(Integer commentreplynoticeid, Date dateusernotified, Date timemodified) {
        this.commentreplynoticeid = commentreplynoticeid;
        this.dateusernotified = dateusernotified;
        this.timemodified = timemodified;
    }

    public Integer getCommentreplynoticeid() {
        return commentreplynoticeid;
    }

    public void setCommentreplynoticeid(Integer commentreplynoticeid) {
        this.commentreplynoticeid = commentreplynoticeid;
    }

    public Date getDateusernotified() {
        return dateusernotified;
    }

    public void setDateusernotified(Date dateusernotified) {
        this.dateusernotified = dateusernotified;
    }

    public Date getDateuserread() {
        return dateuserread;
    }

    public void setDateuserread(Date dateuserread) {
        this.dateuserread = dateuserread;
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

    public Comment getCommentid() {
        return commentid;
    }

    public void setCommentid(Comment commentid) {
        this.commentid = commentid;
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
        hash += (commentreplynoticeid != null ? commentreplynoticeid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Commentreplynotice)) {
            return false;
        }
        Commentreplynotice other = (Commentreplynotice) object;
        if ((this.commentreplynoticeid == null && other.commentreplynoticeid != null) || (this.commentreplynoticeid != null && !this.commentreplynoticeid.equals(other.commentreplynoticeid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.idisc.pu.entities.Commentreplynotice[ commentreplynoticeid=" + commentreplynoticeid + " ]";
    }

}
