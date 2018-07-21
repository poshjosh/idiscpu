package com.idisc.pu;

import com.bc.jpa.context.JpaContext;
import com.idisc.pu.entities.Site;
import com.idisc.pu.entities.Site_;
import com.idisc.pu.entities.Sitetype;
import java.util.Date;
import java.util.List;
import com.idisc.pu.entities.Timezone;
import java.util.Objects;
import com.bc.jpa.dao.Select;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Aug 13, 2016 9:25:31 AM
 */
public class SiteDao extends DaoBase {
    
  private transient static final Logger LOG = Logger.getLogger(SiteDao.class.getName());
    
  private final Timezone defaultTimezone;
  
  public SiteDao(JpaContext jpaContext) {
    super(jpaContext);
    this.defaultTimezone = jpaContext.getEntityManager(Timezone.class).find(Timezone.class, (short)0);
  }  

  public Integer getIdForSitename(String sitename) {
    final Integer siteId;
    try(Select<Integer> qb = this.getJpaContext().getDaoForSelect(Site.class, Integer.class)) {
        siteId = qb
                .select(Site.class, Site_.siteid)
                .where(Site_.site, sitename).createQuery().getSingleResult();
    }
    return siteId;
  }  

  public Site from(String siteName, Sitetype sitetype, boolean createIfNone) {
      
      return from(siteName, sitetype, this.defaultTimezone, createIfNone);
  }
  
  public Site from(String siteName, Sitetype sitetype, Timezone timezone, boolean createIfNone) {
      
    Objects.requireNonNull(timezone);
    
    Site output;   
    
    try(Select<Site> qb = getJpaContext().getDaoForSelect(Site.class)) {
      
      qb.from(Site.class);
      
      if (siteName != null) {
        qb.where(Site_.site.getName(), siteName);
      }
      if (sitetype != null) {
        qb.where(Site_.sitetypeid.getName(), sitetype);
      }
      
      List<Site> found = qb.createQuery().getResultList();
      
      if(found == null || found.isEmpty()) {
          
        if(createIfNone) {
            
          output = new Site();    
          output.setDatecreated(new Date());
          output.setSite(siteName);
          output.setSitetypeid(sitetype);
          output.setTimezoneid(timezone);
          
          qb.begin().persist(output).commit();
          
        }else{
            
          output = null;  
        }
      }else{
          
        output = found.get(0);
      }
    }
    
    return output;
  }
  
  public Timezone getDefaultTimezone() {
      return defaultTimezone;
  }
}


