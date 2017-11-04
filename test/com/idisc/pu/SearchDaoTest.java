/*
 * Copyright 2017 NUROX Ltd.
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

import com.bc.jpa.context.JpaContext;
import com.idisc.pu.entities.Feed;
import com.idisc.pu.functions.GetColumnNamesOfType;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Josh
 */
public class SearchDaoTest extends TestStub {
    
    public SearchDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of format method, of class SearchDao.
     */
    @Test
    public void test() {
        System.out.println("test");
        final String query = "bellanaija lindaikeji naij";
        final JpaContext jpa = this.getJpaContext();
        
        final List<String> cols = Arrays.asList("author", "title", "categories", "description", "content");//new GetColumnNamesOfType(jpa.getMetaData()).apply(Feed.class, String.class);
System.out.println("Columns: " + cols);        
        try(final SearchDao instance = new SearchDao(jpa, Feed.class, 0, 20, query, cols.toArray(new String[0]))) {
            final List<Feed> results = instance.getResultList();
            for(Feed feed : results) {
System.out.println("ID: " + feed.getFeedid() + ", site: " + feed.getSiteid().getSite() + "\nCategories: " + feed.getCategories());                
            }
        }
    }
}
