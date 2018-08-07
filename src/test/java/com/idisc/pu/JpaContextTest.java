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

import com.bc.jpa.context.JpaContext;
import com.bc.jpa.dao.Select;
import com.idisc.pu.entities.Feed;
import javax.persistence.NoResultException;

/**
 * @author Chinomso Bassey Ikwuagwu on Jul 31, 2018 10:51:36 PM
 */
public class JpaContextTest extends TestStub {

    public static void main(String... args) {
        new JpaContextTest().test();
    }
    
    private void test() {

        final JpaContext jpa = this.getJpaContext();
        
        final String site = "bellanaija";
        
        new FeedDao(jpa).getMostRecentForSite(site).ifPresent(
                (feed) -> print("Most recent "+site+" feed: ", feed)
        );
        
        try(Select<Integer> sel = jpa.getDaoForSelect(Integer.class)) {
            final Integer feedid = 1000;
            final Feed feed = sel.find(Feed.class, 1000);
            this.print("Selected feed with ID = "+feedid+": ", feed);
        }catch(NoResultException e) {
            e.printStackTrace();
        }
        
    }
    
    public void print(String prefix, Feed feed) {
        if(prefix != null) System.out.print(prefix);
        System.out.println(feed == null ? null : feed.getFeedid() + ", title: " + feed.getTitle());
    }
}
