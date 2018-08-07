package com.idisc.pu;

import com.bc.jpa.context.PersistenceUnitContext;
import com.idisc.pu.entities.Site;
import com.idisc.pu.entities.Site_;
import com.idisc.pu.entities.Sitetype;
import java.util.Date;
import java.util.List;
import com.idisc.pu.entities.Timezone;
import java.util.Objects;
import com.bc.jpa.dao.Select;
import com.idisc.pu.entities.Sitetype_;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Aug 13, 2016 9:25:31 AM
 */
public class SiteDao extends DaoBase {
    
  private transient static final Logger LOG = Logger.getLogger(SiteDao.class.getName());
    
  private static Timezone defaultTimezone;
  
  public SiteDao(PersistenceUnitContext jpaContext) {
    super(jpaContext);
    if(defaultTimezone == null) {
      defaultTimezone = jpaContext.getEntityManager().find(Timezone.class, (short)0);
    }
  }  

  public Integer getIdForSitename(String sitename) {
    final Integer siteId;
    try(Select<Integer> qb = this.getJpaContext().getDaoForSelect(Integer.class)) {
        siteId = qb
                .select(Site.class, Site_.siteid)
                .where(Site_.site, sitename).createQuery().getSingleResult();
    }
    return siteId;
  }  

  public Site from(String siteName, String sitetypeName, boolean createIfNone) {
      return findSingle(siteName, sitetypeName, defaultTimezone, createIfNone);
  }
  
  public Site from(String siteName, String sitetypeName, Timezone timezone, boolean createIfNone) {
    return this.findSingle(siteName, sitetypeName, timezone, createIfNone);
  }

  public Site from(String siteName, Sitetype sitetype, boolean createIfNone) {
      return from(siteName, sitetype, defaultTimezone, createIfNone);
  }

  public Site from(String siteName, Sitetype sitetype, Timezone timezone, boolean createIfNone) {
    return this.findSingle(siteName, sitetype, timezone, createIfNone);
  }
  
  protected Site findSingle(String siteName, Object sitetype, Timezone timezone, boolean createIfNone) {
    
    Objects.requireNonNull(siteName);
    Objects.requireNonNull(sitetype);
    Objects.requireNonNull(timezone);
    
    final Site output;   
    
    try(Select<Site> select = getJpaContext().getDaoForSelect(Site.class)) {
      
      final Site found = select.from(Site.class)
              .where(Site_.site, siteName)
              .and().where(Site_.sitetypeid, sitetype)
              .createQuery().getSingleResult();
      
      if(found == null) {
          
        if(createIfNone) {
            
          output = new Site();    
          output.setDatecreated(new Date());
          output.setSite(siteName);
          final Sitetype type;
          if(sitetype instanceof Sitetype) {
            type = (Sitetype)sitetype;
          }else{
            type = this.getJpaContext().getDaoForSelect(Sitetype.class)
                    .from(Sitetype.class)
                    .where(Sitetype_.sitetype, sitetype.toString())
                    .getSingleResultAndClose();
          }
          output.setSitetypeid(type);
          output.setTimezoneid(timezone);
          
          select.begin().persist(output).commit();
          
        }else{
            
          output = null;  
        }
      }else{
          
        output = found;
      }
    }
    
    return output;
  }
  
  public List<Site> find(String siteName, Sitetype sitetype, Timezone timezone, boolean createIfNone) {
      
    Objects.requireNonNull(timezone);
    
    final List<Site> output;   
    
    try(Select<Site> select = getJpaContext().getDaoForSelect(Site.class)) {
      
      select.from(Site.class);
      
      if (siteName != null) {
        select.where(Site_.site, siteName);
      }
      if (sitetype != null) {
        select.where(Site_.sitetypeid, sitetype);
      }
      
      final List<Site> found = select.createQuery().getResultList();
      
      if(found == null || found.isEmpty()) {
          
        if(createIfNone) {
            
          final Site site = new Site();    
          site.setDatecreated(new Date());
          site.setSite(siteName);
          site.setSitetypeid(sitetype);
          site.setTimezoneid(timezone);
          
          select.begin().persist(site).commit();
          
          output = Collections.singletonList(site);
          
        }else{
            
          output = Collections.EMPTY_LIST;  
        }
      }else{
          
        output = found;
      }
    }
    
    return output;
  }

  public Timezone getDefaultTimezone() {
      return defaultTimezone;
  }
}


