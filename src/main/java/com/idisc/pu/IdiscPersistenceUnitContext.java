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

package com.idisc.pu;

import com.bc.jpa.EntityManagerFactoryCreator;
import com.bc.jpa.context.PersistenceContextEclipselinkOptimized;
import com.bc.jpa.metadata.PersistenceMetaData;
import com.bc.jpa.util.PersistenceURISelector;
import com.bc.sql.SQLDateTimePatterns;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Jul 31, 2018 8:06:56 PM
 */
public class IdiscPersistenceUnitContext extends PersistenceContextEclipselinkOptimized {

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
    
    public IdiscPersistenceUnitContext(SQLDateTimePatterns dateTimePatterns, Class... entityClasses) {
        super(dateTimePatterns, entityClasses);
    }

    public IdiscPersistenceUnitContext(String persistenceFile, SQLDateTimePatterns dateTimePatterns) 
            throws IOException {
        this(
                new PersistenceURISelector().selectURI(persistenceFile, new PersistenceUriFilter()), 
                dateTimePatterns);
    }
    
    public IdiscPersistenceUnitContext(URI persistenceConfigUri, SQLDateTimePatterns dateTimePatterns) {
        super(persistenceConfigUri, dateTimePatterns);
    }

    public IdiscPersistenceUnitContext(EntityManagerFactoryCreator emfCreator, SQLDateTimePatterns dateTimePatterns, Class... entityClasses) {
        super(emfCreator, dateTimePatterns, entityClasses);
    }

    public IdiscPersistenceUnitContext(String persistenceFile, 
            EntityManagerFactoryCreator emfCreator, SQLDateTimePatterns dateTimePatterns) 
            throws IOException{
        this(new PersistenceURISelector().selectURI(persistenceFile, new PersistenceUriFilter()), 
                emfCreator, dateTimePatterns);
    }
    
    public IdiscPersistenceUnitContext(URI persistenceConfigUri, EntityManagerFactoryCreator emfCreator, SQLDateTimePatterns dateTimePatterns) {
        super(persistenceConfigUri, emfCreator, dateTimePatterns);
    }

    public IdiscPersistenceUnitContext(PersistenceMetaData metaData, EntityManagerFactoryCreator emfCreator, SQLDateTimePatterns dateTimePatterns) {
        super(metaData, emfCreator, dateTimePatterns);
    }
}
