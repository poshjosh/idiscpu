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

import com.bc.connectionleak.ConnectionLeakValidator;
import com.bc.jpa.dao.JpaObjectFactory;
import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 * @author Chinomso Bassey Ikwuagwu on Sep 10, 2018 8:04:25 PM
 */
public class JpaConnectionLeakCounter implements Serializable, com.bc.connectionleak.DatabaseDialect {

    private transient static final Logger LOG = Logger.getLogger(JpaConnectionLeakCounter.class.getName());

    private final String dbDialect;
    private final JpaObjectFactory jpaContext;
    private static final ConnectionLeakValidator<EntityManager> connLeakValidator =
            ConnectionLeakValidator.newEntityManagerInstance();
    
    private static final long LOG_INTERVAL_MILLIS = 30_000;
    
    private static long lastLogTimeMillis;
    
    public JpaConnectionLeakCounter(String dbDialect, JpaObjectFactory jpaContext) {
        this.dbDialect = Objects.requireNonNull(dbDialect);
        this.jpaContext = Objects.requireNonNull(jpaContext);
        LOG.info(() -> "Database dialect: " + dbDialect + ", jpa context: " + jpaContext);
    }
    
    public int countLeaks() {
        final EntityManager entityManager = jpaContext.getEntityManager();
        try{
            final int leaks = connLeakValidator.countLeaks(entityManager, dbDialect);
            if(System.currentTimeMillis() - lastLogTimeMillis >= LOG_INTERVAL_MILLIS) {
                LOG.log(Level.INFO, "Leaked connections: {0}", leaks);
                lastLogTimeMillis = System.currentTimeMillis();
            }
            return leaks;
        }finally{
            if(entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}
