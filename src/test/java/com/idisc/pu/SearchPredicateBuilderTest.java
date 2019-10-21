/*
 * Copyright 2019 NUROX Ltd.
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

import com.bc.jpa.dao.JpaObjectFactory;
import com.bc.jpa.dao.Select;
import com.bc.util.Util;
import com.idisc.pu.comparator.feed.FeedComparatorRelatedContent;
import com.idisc.pu.entities.Feed;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.Predicate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Josh
 */
public class SearchPredicateBuilderTest extends TestStub{
    
    final long epochSecond = LocalDateTime.of(2019, Month.JANUARY, 1, 0, 0).toEpochSecond(ZoneOffset.UTC);
    final Date after = new Date(TimeUnit.SECONDS.toMillis(epochSecond));
    
    private final int firstResult = 0;
    
    private final int maxResults = 200;
    
    private final Class<Feed> entityClass = Feed.class;
    
    private final String dateColumnName = "feeddate";
    
    private final String [] queries = {"Tinubu", "Ambode"};
    
    private static JpaObjectFactory jpa;
    
    public SearchPredicateBuilderTest() { 
        if(jpa == null) {
            jpa = this.createJpaObjectFactory();
        }
    }

    @Test
    public void test() {
        
        System.out.println("build");

        final Select select = jpa.getDaoForSelect(entityClass);

        SearchPredicateBuilder instance = this.getInstance(select, queries);
        
        long mb4 = Util.availableMemory();
        long tb4 = System.currentTimeMillis();
        
        final List<Predicate> predicateList = instance.build();
        
        System.out.println("Built " + predicateList.size() + " predicates. Spent:: time: " + 
                (System.currentTimeMillis() - tb4) + ", memory: " + (Util.usedMemory(mb4)));
        
        if( ! predicateList.isEmpty()) {
            
            select.getCriteriaQuery().where(predicateList.toArray(new Predicate[0]));
        }
        
        mb4 = Util.availableMemory();
        tb4 = System.currentTimeMillis();

        final List results = select.createQuery().setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
        
        System.out.println("Done searching. Spent:: time: " + 
                (System.currentTimeMillis() - tb4) + ", memory: " + (Util.usedMemory(mb4)));

        System.out.println("Printing " + results.size() + " results.");
        for(Object o : results) {
            System.out.println(o);
        }
        
        if(results.size() > 2 && Feed.class.isAssignableFrom(entityClass)) {
            
            final List<Feed> feeds = new ArrayList<>((List<Feed>)results);
            
//            final String text = Arrays.asList(queries).stream().collect(Collectors.joining(" "));
            final String text = "ambode budget";
            
            final FeedComparatorRelatedContent comparator = new FeedComparatorRelatedContent(text);
            
            feeds.sort(comparator);
            
            System.out.println("Printing " + feeds.size() + " feeds in order compared to: " + text);
            for(Feed feed : feeds) {
                System.out.println(comparator.getText(feed));
            }
        }
    }

    /**
     * Test of build method, of class SearchPredicateBuilder.
     */
//    @Test
    public void testBuild() {
        System.out.println("build");

        final Select select = jpa.getDaoForSelect(entityClass);

        SearchPredicateBuilder instance = this.getInstance(select, queries);
        List<Predicate> result = instance.build();

        instance = this.getInstance(select, new String[0]);
        result = instance.build();
    }
    
    public SearchPredicateBuilder getInstance(Select select, String [] queries) {
        return this.getInstance(select, entityClass, dateColumnName, Collections.EMPTY_LIST, queries);
    }

    public <T> SearchPredicateBuilder<T> getInstance(
            Select<T> select, Class<T> entityClass, 
            String dateColumnName, List ids, String [] queries) {
        final SearchPredicateBuilder<T> instance = new SearchPredicateBuilder<>();
        final EntityManagerFactory emf = jpa.getEntityManagerFactory();
        final Collection<String> colsToSearch = new com.bc.jpa.dao.functions.GetColumnNamesOfType(emf).apply(entityClass, String.class);
        final String idColumnName = new com.bc.jpa.dao.functions.GetIdAttribute(emf).apply(entityClass, Integer.class).getName();
        instance.after(after)
                .columnsToSearch(colsToSearch.toArray(new String[0]))
                .databaseFormat(jpa.getDatabaseFormat())
                .dateColumn(dateColumnName)
                .idColumnName(idColumnName)
                .ids(ids)
                .queries(queries)
                .selector(select);
        return instance;        
    }
}
