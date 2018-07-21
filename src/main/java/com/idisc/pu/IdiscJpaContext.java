package com.idisc.pu;

import com.bc.jpa.context.JpaContextImpl;
import com.bc.jpa.util.PersistenceURISelector;
import com.bc.sql.MySQLDateTimePatterns;
import com.bc.sql.SQLDateTimePatterns;
import java.util.logging.Logger;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;

public class IdiscJpaContext extends JpaContextImpl{
    
  public static class PersistenceUriFilter implements PersistenceURISelector.URIFilter {
    private transient static final Logger LOG = Logger.getLogger(PersistenceUriFilter.class.getName());
    @Override
    public boolean accept(URI uri) {
        final String uriStr = uri.toString();
        boolean accepted = uriStr.contains("idisc");
        if(LOG.isLoggable(false ? Level.WARNING : Level.FINE)) {        
            LOG.log(false ? Level.WARNING : Level.FINE, 
            "Accepted: {0}, persistence config: {1}", new Object[]{accepted, uriStr});
        }
        return accepted;
    }
  }
  
  public IdiscJpaContext() throws IOException {
    this(new MySQLDateTimePatterns());
  }
  
  public IdiscJpaContext(SQLDateTimePatterns dateTimePatterns) throws IOException {
    this("META-INF/persistence.xml", dateTimePatterns);
  }

  public IdiscJpaContext(String persistenceFile, SQLDateTimePatterns dateTimePatterns) throws IOException {
    super(persistenceFile, new PersistenceUriFilter(), dateTimePatterns, References.ENUM_TYPES);
  }
    
//    public IdiscJpaContext(String persistenceFile, PersistenceURISelector.URIFilter uriFilter, SQLDateTimePatterns dateTimePatterns, ParametersFormatter paramFmt) throws IOException {
//        super(persistenceFile, uriFilter, dateTimePatterns, paramFmt, References.ENUM_TYPES);
//    }

  public IdiscJpaContext(URI persistenceFile) throws IOException {
    super(persistenceFile, References.ENUM_TYPES);
  }

  public IdiscJpaContext(URI persistenceFile, SQLDateTimePatterns dateTimePatterns) throws IOException {
    super(persistenceFile, dateTimePatterns, References.ENUM_TYPES);
  }

  public IdiscJpaContext(File persistenceFile, SQLDateTimePatterns dateTimePatterns) throws IOException {
    super(persistenceFile, dateTimePatterns, References.ENUM_TYPES);
  }
}
