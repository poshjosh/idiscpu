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
import com.bc.xml.PersistenceXmlDomImpl;
import com.idisc.pu.entities.Feed;
import com.idisc.pu.entities.Feed_;
import com.idisc.pu.entities.Site;
import com.idisc.pu.entities.Site_;
import java.net.URI;
import java.util.Properties;

/**
 * @author Chinomso Bassey Ikwuagwu on Sep 11, 2018 5:47:24 PM
 */
public class JpaContextDummyTest extends TestStub {

    public static void main(String... args) {
        try{
            new JpaContextDummyTest().test();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void test() {
        
        final String puName = PersistenceNames.PERSISTENCE_UNIT_NAME;
        
        final Properties jpaJdbcProperties;
        int leaks;
        try (final JpaObjectFactory puCtx = this.createJpaObjectFactory()) {

            try(final Select<Site> select = puCtx.getDaoForSelect(Site.class)) {
                select.ascOrder(Site_.siteid).createQuery().getResultList().stream().forEach((site) -> {
                    System.out.println(site.getSiteid() + ", " + site.getSite() + 
                            ", " + site.getSitetypeid() + ", " + site.getIconurl());
                });
            }
            
            try(final Select<Feed> select = puCtx.getDaoForSelect(Feed.class)) {
                select.descOrder(Feed_.feedid).createQuery()
                        .setFirstResult(0).setMaxResults(20).getResultList().stream().forEach((feed) -> {
                            System.out.println(feed.getFeedid() + ", " + feed.getTitle());
                        });
            }
            
            
            final URI uri = getUri("META-INF/persistence.xml");
            jpaJdbcProperties = new PersistenceXmlDomImpl(uri).getProperties(puName);
//            System.out.println(new JsonFormat(true, true, "  ").toJSONString(jpaJdbcProperties));
            
            leaks = ConnectionLeakValidator.newPropertiesInstance().countLeaks(
                    jpaJdbcProperties, ConnectionLeakValidator.MYSQL);
            
            System.out.println("BEFORE CLOSING. Connection leak(s): " + leaks);
        }

        this.sleep(7);
        
        leaks = ConnectionLeakValidator.newPropertiesInstance().countLeaks(
                jpaJdbcProperties, ConnectionLeakValidator.MYSQL);
        System.out.println("AFTER CLOSING. Connection leak(s): " + leaks);
    }
}
