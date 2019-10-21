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
import com.bc.jpa.dao.Select;
import com.bc.util.Util;
import com.bc.xml.PersistenceXmlDom;
import com.bc.xml.PersistenceXmlDomImpl;
import com.idisc.pu.entities.Feed;
import com.idisc.pu.entities.Feed_;
import java.net.URI;
import java.util.List;
import java.util.Properties;

/**
 * @author Chinomso Bassey Ikwuagwu on Aug 10, 2018 2:14:58 AM
 */
public class JpaContextLoadTest extends TestStub {

    public static void main(String... args) {
        new JpaContextLoadTest().test();
    }
    
    private void test() {
        
        final long mb4 = Util.availableMemory();
        final long tb4 = System.currentTimeMillis();
        this.printMemoryStats();
        // Memory. free: 61_664_472, available: 929_361_112
        
        final String persistenceUnit = com.idisc.pu.PersistenceNames.PERSISTENCE_UNIT_NAME;

        final Properties jpaJdbcProperties;
        int leaks;
//        try (final PersistenceContext ctx = this.getPersistenceContext(persistenceConfigUri)) {

//            try(final PersistenceUnitContext puCtx = ctx.getContext(persistenceUnit)) {
            try(final JpaObjectFactory puCtx = this.createJpaObjectFactory(persistenceUnit)) {
                
                this.printStats("BEFORE GC", tb4, mb4);

                Runtime.getRuntime().gc();
                this.sleep(7);

                this.printStats("AFTER GC", tb4, mb4);

                try(final Select<Feed> select = puCtx.getDaoForSelect(Feed.class)) {
                    final List<Feed> feedList = select
                            .descOrder(Feed_.feedid).createQuery()
                            .setFirstResult(0).setMaxResults(3).getResultList();
                    System.out.println("Feed list live: " + feedList);
                }

                final String persistenceConfigResource = "META-INF/persistence.xml";
                final URI persistenceConfigUri = this.getUri(persistenceConfigResource);
                final PersistenceXmlDom dom = new PersistenceXmlDomImpl(persistenceConfigUri);

                jpaJdbcProperties = dom.getProperties(persistenceUnit);
                System.out.println("Jpa jdbc properties: " + jpaJdbcProperties.stringPropertyNames());

                leaks = ConnectionLeakValidator.newPropertiesInstance().countLeaks(
                        jpaJdbcProperties, ConnectionLeakValidator.MYSQL);

                System.out.println("BEFORE CLOSING. Connection leak(s): " + leaks);
            }
//        }

        this.sleep(7);
        
        leaks = ConnectionLeakValidator.newPropertiesInstance().countLeaks(
                jpaJdbcProperties, ConnectionLeakValidator.MYSQL);
        System.out.println("AFTER CLOSING. Connection leak(s): " + leaks);
    }
}
