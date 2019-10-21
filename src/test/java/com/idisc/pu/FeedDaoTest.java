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
import com.bc.jpa.dao.Select;
import com.bc.jpa.dao.functions.GetIdAttribute;
import com.bc.jpa.util.MapBuilderForEntity;
import com.bc.util.JsonFormat;
import com.bc.util.Util;
import com.idisc.pu.entities.Feed;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.NoResultException;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

/**
 * @author Chinomso Bassey Ikwuagwu on Jul 31, 2018 10:51:36 PM
 */
public class FeedDaoTest extends TestStub {

    public static void main(String... args) {
        new FeedDaoTest().test();
    }
    
    private void test() {
        
        final long mb4 = Util.availableMemory();
        final long tb4 = System.currentTimeMillis();

        try(final PersistenceUnitContext jpa = this.getPersistenceUnitContext()) {
            
            final SingularAttribute<Feed, Integer> id = new GetIdAttribute(
                    jpa.getEntityManagerFactory()).apply(Feed.class, Integer.class);
            
            System.out.println("ID: " + id);
            
            for(EntityType e : jpa.getEntityManagerFactory().getMetamodel().getEntities()) {
                System.out.println();
                System.out.println(e);
                System.out.println("BindableJavaType: " + e.getBindableJavaType());
                System.out.println("BindableType: " + e.getBindableType());
                System.out.println("Class: " + e.getClass());
                System.out.println("JavaType: " + e.getJavaType());
                System.out.println("Name: " + e.getName());
            }
            
            System.out.println("Loaded JpaContext, consumed. time: " +
                    (System.currentTimeMillis() - tb4) +
                    ", memory: " + Util.usedMemory(mb4));

            final String site = "bellanaija";

            new FeedDao(jpa).getMostRecentForSite(site).ifPresent(
                    (feed) -> print("Most recent "+site+" feed: ", feed, false)
            );

            try(Select<Integer> sel = jpa.getDaoForSelect(Integer.class)) {
                final Integer feedid = 200;
                final Feed feed = sel.find(Feed.class, 200);
                this.print("Selected feed with ID = "+feedid+": ", feed, false);
            }catch(NoResultException e) {
                e.printStackTrace();
            }

            try(final Select<Feed> select = jpa.getDaoForSelect(Feed.class)) {

                final List<Feed> resultList = select.createQuery()
                        .setFirstResult(0)
                        .setMaxResults(10)
                        .getResultList();

                final FeedDao feedDao = new FeedDao(jpa);

                int i = 0;

                for(Feed feed : resultList) {

                    switch(i) {
                        case 0: 
                        case 1: feed.setUrl("http://www.abc.com"); break;
                        case 2:
                        case 3: feed.setTitle("Umunundu"); break;
                        case 4:
                        case 5: feed.setDescription("Ajobi combie"); break;
                        case 6:
                        case 7: feed.setContent("Hmmmmmmmmmmmmm");
                    }

                    final List<Feed> matchingFeedList = feedDao.selectMatchingFeeds(feed, 0, 1);

    //                System.out.println("NEW: " + matchingFeedList);

                    final Feed matchingFeed_1 = feedDao.selectFirstWithMatchingData(feed, null);

    //                System.out.println("OLD: " + matchingFeed_1);

                    final Feed matchingFeed_0 = matchingFeedList.isEmpty() ? null : matchingFeedList.get(0);

                    if(!Objects.equals(matchingFeed_0, matchingFeed_1)) {
                        this.print("NEW: ", matchingFeed_0, false);
                        this.print("OLD: ", matchingFeed_1, false);
                    }

                    ++i;
                }
            }
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
