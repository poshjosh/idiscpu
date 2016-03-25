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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;















@Entity
@Table(name="usersitehitcount")
@XmlRootElement
@NamedQueries({@javax.persistence.NamedQuery(name="Usersitehitcount.findAll", query="SELECT u FROM Usersitehitcount u"), @javax.persistence.NamedQuery(name="Usersitehitcount.findByInstallationid", query="SELECT u FROM Usersitehitcount u WHERE u.usersitehitcountPK.installationid = :installationid"), @javax.persistence.NamedQuery(name="Usersitehitcount.findBySiteid", query="SELECT u FROM Usersitehitcount u WHERE u.usersitehitcountPK.siteid = :siteid"), @javax.persistence.NamedQuery(name="Usersitehitcount.findByHitcount", query="SELECT u FROM Usersitehitcount u WHERE u.hitcount = :hitcount"), @javax.persistence.NamedQuery(name="Usersitehitcount.findByDatecreated", query="SELECT u FROM Usersitehitcount u WHERE u.datecreated = :datecreated"), @javax.persistence.NamedQuery(name="Usersitehitcount.findByTimemodified", query="SELECT u FROM Usersitehitcount u WHERE u.timemodified = :timemodified")})
public class Usersitehitcount
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  @EmbeddedId
  protected UsersitehitcountPK usersitehitcountPK;
  @Basic(optional=false)
  @Column(name="hitcount")
  private int hitcount;
  @Basic(optional=false)
  @Column(name="datecreated")
  @Temporal(TemporalType.TIMESTAMP)
  private Date datecreated;
  @Basic(optional=false)
  @Column(name="timemodified")
  @Temporal(TemporalType.TIMESTAMP)
  private Date timemodified;
  @JoinColumn(name="installationid", referencedColumnName="installationid", insertable=false, updatable=false)
  @ManyToOne(optional=false)
  private Installation installation;
  @JoinColumn(name="siteid", referencedColumnName="siteid", insertable=false, updatable=false)
  @ManyToOne(optional=false)
  private Site site;
  
  public Usersitehitcount() {}
  
  public Usersitehitcount(UsersitehitcountPK usersitehitcountPK)
  {
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
    return this.usersitehitcountPK;
  }
  
  public void setUsersitehitcountPK(UsersitehitcountPK usersitehitcountPK) {
    this.usersitehitcountPK = usersitehitcountPK;
  }
  
  public int getHitcount() {
    return this.hitcount;
  }
  
  public void setHitcount(int hitcount) {
    this.hitcount = hitcount;
  }
  
  public Date getDatecreated() {
    return this.datecreated;
  }
  
  public void setDatecreated(Date datecreated) {
    this.datecreated = datecreated;
  }
  
  public Date getTimemodified() {
    return this.timemodified;
  }
  
  public void setTimemodified(Date timemodified) {
    this.timemodified = timemodified;
  }
  
  public Installation getInstallation() {
    return this.installation;
  }
  
  public void setInstallation(Installation installation) {
    this.installation = installation;
  }
  
  public Site getSite() {
    return this.site;
  }
  
  public void setSite(Site site) {
    this.site = site;
  }
  
  public int hashCode()
  {
    int hash = 0;
    hash += (this.usersitehitcountPK != null ? this.usersitehitcountPK.hashCode() : 0);
    return hash;
  }
  

  public boolean equals(Object object)
  {
    if (!(object instanceof Usersitehitcount)) {
      return false;
    }
    Usersitehitcount other = (Usersitehitcount)object;
    if (((this.usersitehitcountPK == null) && (other.usersitehitcountPK != null)) || ((this.usersitehitcountPK != null) && (!this.usersitehitcountPK.equals(other.usersitehitcountPK)))) {
      return false;
    }
    return true;
  }
  
  public String toString()
  {
    return "com.idisc.pu.entities.Usersitehitcount[ usersitehitcountPK=" + this.usersitehitcountPK + " ]";
  }
}
