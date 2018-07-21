/*
 * Copyright 2016 NUROX Ltd.
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
import com.bc.jpa.dao.Criteria;
import java.util.logging.Logger;
import com.idisc.pu.entities.Feed;
import com.idisc.pu.entities.Site;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.bc.jpa.dao.Select;

/**
 * @author Josh
 */
public class FeedServiceTest extends TestStub {
    private transient static final Logger LOG = Logger.getLogger(FeedServiceTest.class.getName());
    
    public FeedServiceTest() { }
    
    @Test
    public void test() {
        
        Map<String, String> params = new HashMap();
        params.put("table", "installation");
        params.put("screenname", "user_2");

        if(LOG.isLoggable(Level.FINE)){
            LOG.log(Level.FINE, "Request parameters: {0}", params);
        }

        String table = (String)params.remove("table");

        if (table == null) {
//          throw new ServletException("Missing value for required parameter: 'table'");
        }

        final JpaContext jpaContext = this.getJpaContext();

        final Class entityType = jpaContext.getMetaData().findEntityClass(table);

        if (entityType == null) {
//          throw new ServletException("Invalid value for required parameter: 'table'");
        }else{
          if(LOG.isLoggable(Level.FINE)){
               LOG.log(Level.FINE, "{0} = {1}",new Object[]{ table,  entityType});
          }
        }

        final String logicalOptrStr = (String)params.remove("connector");

        final Criteria.LogicalOperator logicalOptr;
        if(logicalOptrStr == null) {
            logicalOptr = Criteria.AND;
        }else{
            switch(logicalOptrStr) {
                case "OR": logicalOptr = Criteria.OR; break;
                default: logicalOptr = Criteria.AND; break;
            }
        }

        try(Select dao = jpaContext.getDaoForSelect(entityType)) {

            dao.from(entityType);

            final Set<String> keys = params.keySet();

            for(String key : keys) {

                Object val = params.get(key);

                dao.where(key, Criteria.EQ, val, logicalOptr);
            }

            final List found = dao.createQuery().setMaxResults(1).getResultList();

            final boolean output = found != null && !found.isEmpty();

            if(LOG.isLoggable(Level.INFO)){
                  LOG.log(Level.INFO, "x x x x x x x Output: {0}", output);
            }
        }
    }

    private void testOld() {
        
        final int offset = 0;
        final int limit = 2;
        final boolean spreadOutput = true;
        
        FeedDao instance = new FeedDao(this.getJpaContext());

        final List<Feed> feeds = instance.getFeeds(offset, limit, spreadOutput);

//        final Site site = this.getJpaContext().getDaoForSelect(Site.class).findAndClose(28);
//        List<Feed> feeds = Arrays.asList(this.getFeed1(site), this.getFeed2(site));
//        Collection<Feed> failedToCreate = instance.createIfNoneExistsWithMatchingData(feeds);

        Collection<Feed> failedToCreate = instance.createIfNoneExistsWithMatchingData(feeds);
        
System.out.println("Success rate: "+(feeds.size()-failedToCreate.size())+'/'+feeds.size()+"\nZERO success rate is expected");        

System.out.println("Printing "+failedToCreate.size()+" failed");
for(Feed failed : failedToCreate) {
    System.out.println(failed.getTitle());
}     

        assertTrue(failedToCreate.isEmpty());
    }
    
    private Feed getFeed1(Site site) {
        Feed feed = new Feed();
        feed.setAuthor("Posh Josh");
        feed.setCategories("interesting,history,insult");
        feed.setContent("<p><img alt=\"Winston Churchhill\" src=\"https://all-that-is-interesting.com/wordpress/wp-content/uploads/2012/03/history-insults-winston-churchill.jpg\"/></p>The extremely witty and much-loved British Prime Minister Winston Churchill tops the list with his verbal spat with Lady Astor. The conservative dame forever admonished Churchill for his cigars and alcohol habits, and Churchill was not one to take the insults lying down. Of their famous squabbles, the most memorable is when Astor commented, “If you were my husband, I’d poison your tea.” Churchill’s riposte? “Madame, if you were my wife, I’d drink it.”<p><img alt=\\\"Gandhi\\\" src=\\\"https://all-that-is-interesting.com/wordpress/wp-content/uploads/2012/03/history-insults-winston-gandhi.jpg\\\"/></p>His vocalization of non-violence doesn’t mean Gandhi wasn’t lethal with his wit. One such incident was when Gandhi traveled to London and a reporter asked him...<a target=\"_blank\" href=\"http://all-that-is-interesting.com/best-insults-history\">Continue reading</a>");
        feed.setDatecreated(new Date());
        feed.setDescription("The best insults in history");
        feed.setFeeddate(new Date());
        feed.setImageurl("https://all-that-is-interesting.com/wordpress/wp-content/uploads/2012/03/history-insults-winston-churchill.jpg");
        feed.setKeywords("interesting,history,insult,winston churchill,gandhi");
        feed.setSiteid(site);
        feed.setTitle("The Best Instults in History");
        feed.setUrl("http://all-that-is-interesting.com/best-insults-history");
        return feed;
    }

    private Feed getFeed2(Site site) {
        Feed feed = new Feed();
        feed.setAuthor("Posh Josh");
        feed.setCategories("stargist,social,gossip,nollywood,entertainment");
        feed.setContent("<p><img alt=\"Funke Akindele & JJC Skills\" src=\"http://stargist.com/wp-content/uploads/2016/08/funke-akindele-and-jjc-skills-wedding.png\"/></p>Nollywood star, Funke Akindele who has been speculated to be married to Abdulrasheed Bello aka JJC Skills on her birthday recently in London have finally opened up on their secret marriage. The couple, in an official press statement signed by Funke Akindele’s publicist, Biodun Kupoluyi, revealed that they chose a quiet wedding in London because neither of them wanted to be on show or at the center of attention having been on that lane before. “The decision to do it quietly was right for us and we pray for the understanding of our close friends and fans at this offer of a lifetime moment. At a good time, we shall look back and in appreciation of divine grace and your support, we shall celebrate milestones and where we are in life”...<a target=\"_blank\" href=\"http://stargist.com/nigerian_celebrity/funke-akindele-jjc-skillz-open-secret-wedding/\">Continue reading</a>");
        feed.setDatecreated(new Date());
        feed.setDescription("Read Why Funke Akindele & JJC Skills Opted For A Secret Wedding In London");
        feed.setFeeddate(new Date());
        feed.setImageurl("http://stargist.com/wp-content/uploads/2016/08/funke-akindele-and-jjc-skills-wedding.png");
        feed.setKeywords("stargist,social,gossip,nollywood,entertainment,funke akindele,jjc skills,wedding");
        feed.setSiteid(site);
        feed.setTitle("Read Why Funke Akindele & JJC Skills Opted For A Secret Wedding In London");
        feed.setUrl("http://stargist.com/nigerian_celebrity/funke-akindele-jjc-skillz-open-secret-wedding/");
        return feed;
    }
}
