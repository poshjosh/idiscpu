package com.idisc.pu;

import com.bc.jpa.context.JpaContext;
import com.bc.util.XLogger;
import com.idisc.pu.entities.Country;
import com.idisc.pu.entities.Feeduser;
import com.idisc.pu.entities.Installation;
import com.idisc.pu.entities.Installation_;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import com.bc.jpa.dao.Select;

/**
 * @author Chinomso Bassey Ikwuagwu on Aug 13, 2016 9:47:40 AM
 */
public class InstallationService extends DaoService {
    
  private static final AtomicInteger COUNT = new AtomicInteger();
  
  private static final long TIMEOFFSET_MILLIS;
  static {
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    cal.set(2016, 0, 22);
    TIMEOFFSET_MILLIS = cal.getTimeInMillis();
  }
  
  public InstallationService(JpaContext jpaContext) {
    super(jpaContext);
  }

  public Installation from(
          User user, Integer installationid, String installationkey, String screenname, 
          Country country, long firstinstallationtime, long lastinstallationtime, boolean createIfNone) {
      
XLogger logger = XLogger.getInstance();
Level level = Level.FINE;
Class cls = this.getClass();
      
logger.log(level, "User: {0}", cls, user);

    Installation output;
    
    final JpaContext jpaContext = this.getJpaContext();

    if (user != null) {

      List<Installation> list = user.getInstallationList();

      logger.log(level, "User has {0} installations", cls, (list==null?null:list.size()));

      if(list == null) {

        try(Select<Installation> dao = jpaContext.getDaoForSelect(Installation.class)) {

          list = dao.where(Installation.class, Installation_.feeduserid.getName(), user.getDelegate())
              .createQuery().getResultList();
        }
        
        logger.log(level, "For user, selected {0} installations", cls, (list==null?null:list.size()));

        if(list != null && !list.isEmpty()) {

          user.setInstallationList(list);

          jpaContext.getDao(Feeduser.class).begin().mergeAndClose(user.getDelegate());
          
        }else{
         
          logger.log(Level.WARNING, "Installation not found for user: {0}", cls, user);  
        }
      }

      if (list != null && !list.isEmpty()) {

        return (Installation)list.get(list.size() - 1);
      }
    }

    try {

      final String installationkeyCol = Objects.requireNonNull(Installation_.installationkey.getName());
      output = getEntity(Installation.class, installationkeyCol, installationkey, null);

logger.log(level, "For {0} = {1}, found: {2}", cls, installationkeyCol, installationkey, output);

      if(output == null) {

        output = jpaContext.getDao(Installation.class).findAndClose(Installation.class, installationid);

logger.log(level, "For {0} = {1}, found: {2}", cls, Installation_.installationid.getName(), installationid, output);
      }

      if ((output == null) && (createIfNone)) {

        if (screenname != null && !screenname.isEmpty()) {

          if (this.isExisting(Installation.class, Installation_.screenname.getName(), screenname)) {

            screenname = generateUniqueId();
          }
        }else{

          screenname = generateUniqueId();
        }

        output = new Installation();

        output.setInstallationkey(installationkey);

        output.setScreenname(screenname);
        
        output.setCountryid(country);

        if(firstinstallationtime < TIMEOFFSET_MILLIS) { throw new IllegalArgumentException(); }
        output.setFirstinstallationdate(new Date(firstinstallationtime));

        if(lastinstallationtime < TIMEOFFSET_MILLIS) { throw new IllegalArgumentException(); }
        output.setLastinstallationdate(new Date(lastinstallationtime));

        output.setDatecreated(new Date());

        output.setFeeduserid(user == null ? null : user.getDelegate());
        
        jpaContext.getDao(Installation.class).begin().persistAndClose(output);

        logger.log(level, "For: {0} = {1}, created: ", cls, installationkeyCol, installationkey, output);
      }
    } catch (RuntimeException e) {
      output = null;  
      logger.log(Level.WARNING, "Error accessing database installation record for installationid: " 
              + installationid+", installationkey: "+installationkey, this.getClass(), e);
    }
    
    return output;
  }
  
  private String generateUniqueId() {
    long n = System.currentTimeMillis() - TIMEOFFSET_MILLIS;
    return "user_" + Long.toHexString(n) + "_" + COUNT.getAndIncrement();
  }
}
