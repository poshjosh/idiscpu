package com.idisc.pu;

import com.bc.jpa.JpaContext;
import com.idisc.pu.entities.Site;
import com.idisc.pu.entities.Site_;
import com.idisc.pu.entities.Sitetype;
import java.util.Date;
import java.util.List;
import com.bc.jpa.dao.BuilderForSelect;

/**
 * @author Chinomso Bassey Ikwuagwu on Aug 13, 2016 9:25:31 AM
 */
public class Sites {
    
  private final JpaContext jpaContext;
  
  public Sites(JpaContext jpaContext) {
    this.jpaContext = jpaContext;
  }    
     
  public Site from(String siteName, Sitetype sitetype, boolean createIfNone) {
      
    Site output;   
    
    try(BuilderForSelect<Site> qb = jpaContext.getBuilderForSelect(Site.class)) {
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
        
          qb.getEntityManager().persist(output);
          
        }else{
            
          output = null;  
        }
      }else{
          
        output = found.get(0);
      }
    }

    return output;
  }
}


