package com.idisc.pu;

import com.bc.jpa.dao.JpaObjectFactory;
import java.util.logging.Logger;
import com.idisc.pu.entities.Country;
import com.idisc.pu.entities.Installation;
//import com.idisc.pu.entities.Installation_;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import com.bc.jpa.dao.Select;
import com.idisc.pu.entities.Localaddress;
import com.idisc.pu.entities.Location;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 * @author Chinomso Bassey Ikwuagwu on Aug 13, 2016 9:47:40 AM
 */
public class InstallationDao extends DaoBase {
    
  private transient static final Logger LOG = Logger.getLogger(InstallationDao.class.getName());
    
  private static final AtomicInteger COUNT = new AtomicInteger();
  
  private static final long UID_TIMEOFFSET_MILLIS;
  static {
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    cal.set(2016, 0, 22);
    UID_TIMEOFFSET_MILLIS = cal.getTimeInMillis();
  }
  
  private static final long INSTL_TIMEOFFSET_MILLIS;
  static {
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    cal.set(2009, 0, 1);
    INSTL_TIMEOFFSET_MILLIS = cal.getTimeInMillis();
  }

  public InstallationDao(JpaObjectFactory jpaContext) {
    super(jpaContext);
  }

  public Installation from(
          User user, Integer installationid, String installationkey, String screenname, String ipaddress,  
          Country country, long firstinstallationtime, long lastinstallationtime, boolean createIfNone) {
      
    final Level level = Level.FINE;

    LOG.log(level, "User: {0}", user);

    Installation output;

    try {
        
      output = this.from(user, null);  
      
      if(output == null) {
        
        output = this.from(installationid, installationkey, null);

        if (output == null && createIfNone) {
          
          final List entities = this.create(user, installationid, installationkey, screenname, 
              ipaddress, country, firstinstallationtime, lastinstallationtime);
        
          final EntityManager em = this.getJpaContext().getEntityManager();
          try{
            em.getTransaction().begin();
            for(Object entity : entities) {
              em.persist(entity);
            }
            this.getJpaContext().commit(em.getTransaction());
          }finally{
            em.close();
          }
          output = (Installation)entities.get(entities.size() - 1);
//          this.getJpaContext().getDao().begin().persistAndClose(output);
        }
      }
    } catch (RuntimeException e) {
        
      output = null;  
      
      LOG.log(Level.WARNING, "Unexpected exception. installationid: " 
              + installationid+", installationkey: "+installationkey, e);
    }
    
    return output;
  }

  public Installation from(User user, Installation outputIfNone) {
      
    Level level = Level.FINE;

    LOG.log(level, "User: {0}", user);

    Installation output;
    
    final JpaObjectFactory jpaContext = this.getJpaContext();

    if (user != null) {

      List<Installation> list = user.getInstallationList();

      LOG.log(level, "User has {0} installations", (list==null?null:list.size()));

      if(list == null) {

        try(Select<Installation> select = jpaContext.getDaoForSelect(Installation.class)) {

          list = select.where(Installation.class, "feeduserid", user.getDelegate())
              .createQuery().getResultList();
        }
        
        LOG.log(level, "For user, selected {0} installations", (list==null?null:list.size()));

        if(list != null && !list.isEmpty()) {

          user.setInstallationList(list);

          jpaContext.getDao().begin().mergeAndClose(user.getDelegate());
          
        }else{
         
          LOG.log(Level.WARNING, "Installation not found for user: {0}", user);  
        }
      }

      if (list != null && !list.isEmpty()) {

        output = (Installation)list.get(list.size() - 1);
        
      }else{
          
        output = null;
      }
    }else{
        
      output = null;  
    }
    
    return output == null ? outputIfNone : output;
  }

  public Installation from(Integer installationid, String installationkey, Installation outputIfNone) {
      
    Installation output;
    
    if((installationid != null && installationid > -1) || installationkey != null) {
        
      try(final Select<Installation> select = this.getJpaContext().getDaoForSelect(Installation.class)) {

        if(installationid != null && installationid > -1) {
          select.where("installationid", installationid).or();
        }
        
        if(installationkey != null) {
          select.where("installationkey", installationkey).or();
        }

        output = select.getSingleResultAndClose();
        
      }catch(NoResultException e) {
        output = null;  
      }
    }else{
      output = null;  
    }
    
    return output == null ? outputIfNone : output;
  }
  
  public List create(
          User user, Integer installationid, String installationkey, String screenname,
          String ipaddress, Country country, long firstinstallationtime, long lastinstallationtime) {

    LOG.log(Level.FINE, "User: {0}", user);

    Objects.requireNonNull(installationkey);

    if (screenname != null && !screenname.isEmpty()) {

      if (this.isExisting(Installation.class, "screenname", screenname)) {

        screenname = generateUniqueId();
      }
    }else{

      screenname = generateUniqueId();
    }

    final List result = new ArrayList(4);
    
    final Date now = new Date();
    
    final Installation installation = new Installation();

    installation.setInstallationkey(installationkey);

    installation.setScreenname(screenname);
    
    installation.setIpaddress(ipaddress);

    if(country != null) {
      
      final Localaddress localaddress = new Localaddress();
      localaddress.setDatecreated(now);
      localaddress.setCountry(country);
      result.add(localaddress);
      
      final Location location = new Location();
      location.setDatecreated(now);
      location.setLocaladdress(localaddress);
      result.add(location);
    }
    
    result.add(installation);

//        if(firstinstallationtime < INSTL_TIMEOFFSET_MILLIS) { throw new IllegalArgumentException(); }
    if(firstinstallationtime > INSTL_TIMEOFFSET_MILLIS) {
      installation.setFirstinstallationdate(new Date(firstinstallationtime));
    }
//        if(lastinstallationtime < INSTL_TIMEOFFSET_MILLIS) { throw new IllegalArgumentException(); }
    if(lastinstallationtime > INSTL_TIMEOFFSET_MILLIS) {
      installation.setLastinstallationdate(new Date(lastinstallationtime));
    }
    installation.setDatecreated(now);

    installation.setFeeduserid(user == null ? null : user.getDelegate());

    if(LOG.isLoggable(Level.FINE)) {
      LOG.log(Level.FINE, "For: installationkey = {0}, created: {1}", new Object[]{installationkey, installation});
    }
    
    return result;
  }

  private String generateUniqueId() {
    long n = System.currentTimeMillis() - UID_TIMEOFFSET_MILLIS;
    return "user_" + Long.toHexString(n) + "_" + COUNT.getAndIncrement();
  }
}
