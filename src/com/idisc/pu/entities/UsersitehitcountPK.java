package com.idisc.pu.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;








@Embeddable
public class UsersitehitcountPK
  implements Serializable
{
  @Basic(optional=false)
  @Column(name="installationid")
  private int installationid;
  @Basic(optional=false)
  @Column(name="siteid")
  private int siteid;
  
  public UsersitehitcountPK() {}
  
  public UsersitehitcountPK(int installationid, int siteid)
  {
    this.installationid = installationid;
    this.siteid = siteid;
  }
  
  public int getInstallationid() {
    return this.installationid;
  }
  
  public void setInstallationid(int installationid) {
    this.installationid = installationid;
  }
  
  public int getSiteid() {
    return this.siteid;
  }
  
  public void setSiteid(int siteid) {
    this.siteid = siteid;
  }
  
  public int hashCode()
  {
    int hash = 0;
    hash += this.installationid;
    hash += this.siteid;
    return hash;
  }
  

  public boolean equals(Object object)
  {
    if (!(object instanceof UsersitehitcountPK)) {
      return false;
    }
    UsersitehitcountPK other = (UsersitehitcountPK)object;
    if (this.installationid != other.installationid) {
      return false;
    }
    if (this.siteid != other.siteid) {
      return false;
    }
    return true;
  }
  
  public String toString()
  {
    return "com.idisc.pu.entities.UsersitehitcountPK[ installationid=" + this.installationid + ", siteid=" + this.siteid + " ]";
  }
}
