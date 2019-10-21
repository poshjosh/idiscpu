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

import com.bc.jpa.context.PersistenceUnitContext;
import com.bc.jpa.dao.functions.GetColumnNames;
import com.bc.jpa.dao.functions.GetColumnNamesFromAnnotations;
import com.bc.jpa.dao.functions.GetEntityClasses;
import com.bc.jpa.dao.functions.GetIdAttribute;
import com.bc.jpa.dao.functions.GetTableNameFromAnnotation;
import com.bc.jpa.util.MapBuilderForEntity;
import com.bc.util.JsonFormat;
import com.bc.util.Util;
import com.idisc.pu.entities.Feed;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

/**
 * @author Chinomso Bassey Ikwuagwu on Sep 26, 2018 9:54:32 AM
 */
public class MetaModelTest extends TestStub {

    public static void main(String... args) {
        new MetaModelTest().test();
    }
    
    private void test() {
        
        final long mb4 = Util.availableMemory();
        final long tb4 = System.currentTimeMillis();

        try(final PersistenceUnitContext jpa = this.getPersistenceUnitContext()) {
            
            final Class<Feed> entityClass = Feed.class;
            
            System.out.println("Persistence unit: " + jpa.getPersistenceUnitName());
            
            final EntityManagerFactory emf = jpa.getEntityManagerFactory();

            final Set<Class> entityClasses = new GetEntityClasses().apply(emf);
            System.out.println(new JsonFormat().toJSONString(
                    Collections.singletonMap("entity classes", entityClasses)
            ));
            
            for(EntityType e : emf.getMetamodel().getEntities()) {
                System.out.println();
                System.out.println("Name: " + e.getName());
                System.out.println(e);
                System.out.println("BindableJavaType: " + e.getBindableJavaType());
                System.out.println("BindableType: " + e.getBindableType());
                System.out.println("Class: " + e.getClass());
                System.out.println("JavaType: " + e.getJavaType());
                final String tableName = new GetTableNameFromAnnotation().apply(e.getJavaType());
                System.out.println("Table: " + tableName);
                final List<String> columnNames_0 = new GetColumnNames(emf).apply(e.getJavaType());
                System.out.println("Columns from annotations: " + columnNames_0);
                final List<String> columnNames_1 = new GetColumnNamesFromAnnotations().apply(e.getJavaType());
                System.out.println("Columns from annotations: " + columnNames_1);
            }
            
            final SingularAttribute<Feed, Integer> id = new GetIdAttribute(
                    jpa.getEntityManagerFactory()).apply(entityClass, Object.class);
            System.out.println("Entity type: " + entityClass);
            System.out.println("ID: " + id);
            
            System.out.println("Loaded JpaContext, consumed. time: " +
                    (System.currentTimeMillis() - tb4) +
                    ", memory: " + Util.usedMemory(mb4));

            final String site = "bellanaija";

            new FeedDao(jpa).getMostRecentForSite(site).ifPresent(
                    (feed) -> print("Most recent "+site+" feed: ", feed, false)
            );
        }
    }
    
    public void print(String prefix, Feed feed, boolean verbose) {
        if(prefix != null) {
            System.out.print(prefix);
        }
        if(verbose) {
            final MapBuilderForEntity mapBuilder = new MapBuilderForEntity();
            final Map map = feed == null ? Collections.EMPTY_MAP : mapBuilder.maxDepth(1).nullsAllowed(true).source(feed).build();
            System.out.println(new JsonFormat(true, true, " ").toJSONString(map));        
        }else{
            System.out.println(feed == null ? null : feed.getFeedid() + 
                    ", URL: " + feed.getUrl() +
                    "\nTitle: " + feed.getTitle() + 
                    "\nDescription: " + feed.getDescription());
        }
    }
}
