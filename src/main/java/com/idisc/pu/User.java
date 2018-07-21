package com.idisc.pu;

import com.idisc.pu.entities.Feeduser;
import com.idisc.pu.entities.Gender;
import com.idisc.pu.entities.Howdidyoufindus;
import com.idisc.pu.entities.Installation;
import com.idisc.pu.entities.Localaddress;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class User extends Feeduser {
    
  private transient static final Logger LOG = Logger.getLogger(User.class.getName());
    
  private final Map authdetails;
  
  private final Feeduser delegate;
  
  public User(Feeduser delegate, Map authdetails) {
    this.delegate = Objects.requireNonNull(delegate);
    this.authdetails = Objects.requireNonNull(authdetails);
  }
  
  public final Feeduser getDelegate() {
    return this.delegate;
  }
  
  public final Map getAuthdetails() {
    return this.authdetails;
  }
  
  public Object getAppuserid() {
    return this.authdetails.get("appuserid");
  }
  
  public void setAppuserid(Object appuserid) {
    this.authdetails.put("appuserid", appuserid);
  }
  
  public String getAuthEmailaddress() {
    return (String)this.authdetails.get("emailaddress");
  }
  
  public void setAuthEmailaddress(String emailaddress) {
    this.authdetails.put("emailaddress", emailaddress);
  }
  
  public String getPassword() {
    return (String)this.authdetails.get("password");
  }
  
  public void setPassword(String password) {
    this.authdetails.put("password", password);
  }
  
  public Object getAppid() {
    return this.authdetails.get("appid");
  }
  
  public void setAppid(Object appid) {
    this.authdetails.put("appid", appid);
  }
  
  public String getUsername() {
    return (String)this.authdetails.get("username");
  }
  
  public void setUsername(String username) {
    this.authdetails.put("username", username);
  }
  
  public Object getUserstatus() {
    return this.authdetails.get("userstatus");
  }
  
  public void setUserstatus(Object userstatus) {
    this.authdetails.put("userstatus", userstatus);
  }
  
  public Integer getFeeduserid()
  {
    return this.delegate.getFeeduserid();
  }
  
  public void setFeeduserid(Integer feeduserid)
  {
    this.delegate.setFeeduserid(feeduserid);
  }
  
  public String getEmailAddress()
  {
    return this.delegate.getEmailAddress();
  }
  
  public void setEmailAddress(String emailAddress)
  {
    this.delegate.setEmailAddress(emailAddress);
  }
  
  public String getLastName()
  {
    return this.delegate.getLastName();
  }
  
  public void setLastName(String lastName)
  {
    this.delegate.setLastName(lastName);
  }
  
  public String getFirstName()
  {
    return this.delegate.getFirstName();
  }
  
  public void setFirstName(String firstName)
  {
    this.delegate.setFirstName(firstName);
  }
  
  public Date getDateOfBirth()
  {
    return this.delegate.getDateOfBirth();
  }
  
  public void setDateOfBirth(Date dateOfBirth)
  {
    this.delegate.setDateOfBirth(dateOfBirth);
  }
  
  public String getPhoneNumber1()
  {
    return this.delegate.getPhoneNumber1();
  }
  
  public void setPhoneNumber1(String phoneNumber1)
  {
    this.delegate.setPhoneNumber1(phoneNumber1);
  }
  
  public String getPhoneNumber2()
  {
    return this.delegate.getPhoneNumber2();
  }
  
  public void setPhoneNumber2(String phoneNumber2)
  {
    this.delegate.setPhoneNumber2(phoneNumber2);
  }
  
  @Override
  public String getFax() {
    return this.delegate.getFax();
  }
  
  @Override
  public void setFax(String fax){
    this.delegate.setFax(fax);
  }
  
  @Override
  public Localaddress getLocaladdressid(){
    return this.delegate.getLocaladdressid();
  }
  
  @Override
  public void setLocaladdressid(Localaddress localaddressid) {
    this.delegate.setLocaladdressid(localaddressid);
  }
  
  @Override
  public String getImage1(){
    return this.delegate.getImage1();
  }
  
  @Override
  public void setImage1(String image1){
    this.delegate.setImage1(image1);
  }
  
  @Override
  public Date getDatecreated(){
    return this.delegate.getDatecreated();
  }
  
  @Override
  public void setDatecreated(Date datecreated) {
    this.delegate.setDatecreated(datecreated);
  }
  
  public Date getTimemodified()
  {
    return this.delegate.getTimemodified();
  }
  
  public void setTimemodified(Date timemodified)
  {
    this.delegate.setTimemodified(timemodified);
  }
  
  public String getExtradetails()
  {
    return this.delegate.getExtradetails();
  }
  
  public void setExtradetails(String extradetails)
  {
    this.delegate.setExtradetails(extradetails);
  }
  
  public Gender getGender()
  {
    return this.delegate.getGender();
  }
  
  public void setGender(Gender gender)
  {
    this.delegate.setGender(gender);
  }
  
  public Howdidyoufindus getHowDidYouFindUs()
  {
    return this.delegate.getHowDidYouFindUs();
  }
  
  public void setHowDidYouFindUs(Howdidyoufindus howDidYouFindUs)
  {
    this.delegate.setHowDidYouFindUs(howDidYouFindUs);
  }

    @Override
    public List<Installation> getInstallationList() {
        return delegate.getInstallationList();
    }

    @Override
    public void setInstallationList(List<Installation> installationList) {
        delegate.setInstallationList(installationList);
    }
  
  @Override
  public int hashCode() {
    return this.delegate.hashCode();
  }
  
  @Override
  public boolean equals(Object object) {
    return this.delegate.equals(object);
  }
  
  @Override
  public String toString() {
    return this.getClass().getName()+"{id="+this.getFeeduserid()+", email: "+this.getEmailAddress()+'}';
  }
}
