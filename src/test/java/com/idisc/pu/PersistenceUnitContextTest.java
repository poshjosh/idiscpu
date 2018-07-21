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

import com.bc.jpa.context.PersistenceContext;
import com.bc.jpa.context.PersistenceContextEclipselinkOptimized;
import com.bc.jpa.search.SearchResults;
import com.bc.sql.MySQLDateTimePatterns;
import com.idisc.pu.entities.Feed;
import java.util.List;

/**
 * @author Chinomso Bassey Ikwuagwu on May 1, 2018 11:03:42 AM
 */
public class PersistenceUnitContextTest {

//    @Test
    public static void main(String... args) {
        final PersistenceContext ctx = new PersistenceContextEclipselinkOptimized(
                new MySQLDateTimePatterns(), Feed.class
        );
        final SearchResults<Feed> results = ctx.getDao("idiscpu").search(Feed.class, 20);
        System.out.println("Found: " + results.getSize() + " results");
        final List<Feed> firstPage = results.getPage(0);
        for(Feed feed : firstPage) {
            System.out.println(feed.getFeedid() + ", site: " + feed.getSiteid().getSite() + ", title: " + feed.getTitle());
        }
    }
}
