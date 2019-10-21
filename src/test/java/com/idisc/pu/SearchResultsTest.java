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

import com.bc.jpa.dao.JpaObjectFactory;
import com.bc.jpa.dao.Select;
import com.bc.jpa.dao.search.BaseSearchResults;
import com.bc.jpa.paging.PaginatedList;
import com.idisc.pu.entities.Feed;
import com.idisc.pu.entities.Feed_;

/**
 * @author Chinomso Bassey Ikwuagwu on Sep 29, 2018 10:33:13 PM
 */
public class SearchResultsTest extends TestStub {

    public static void main(String... args) {
        
        final SearchResultsTest test = new SearchResultsTest();
        try(final JpaObjectFactory jpa = test.createJpaObjectFactory()) {
            test.test(jpa, 0, 3000);
        }
    }
    
    public void test(JpaObjectFactory jpa, int offset, int limit) {
        final Select<Feed> select = jpa.getDaoForSelect(Feed.class);
        select.search("adeosun", Feed_.title, Feed_.description, Feed_.content);
        try(final BaseSearchResults<Feed> searchResults = new BaseSearchResults(select)) {
            final PaginatedList<Feed> pages = searchResults.getPages();
            System.out.println("Search Results size: " + pages.size());
            for(Feed feed : pages) {
                System.out.println("ID: " + feed.getFeedid()+", author: " + feed.getAuthor() +
                        ", site: " + feed.getSiteid().getSite() + ", title: " + feed.getTitle());
            }
        }
    }
    public void testCount(JpaObjectFactory jpa, Class entityClass, String idColumnName) {
        final Select<Number> select = jpa.getDaoForSelect(Number.class);
        final Number count = select.count(entityClass, idColumnName).getSingleResultAndClose();
        System.out.println("Count: " + count);
    }
}
