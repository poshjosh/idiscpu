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

package com.idisc.pu.comparator.site;

import com.bc.jpa.dao.JpaObjectFactory;
import com.idisc.pu.FeedDao;
import com.idisc.pu.comparator.ComparatorBase;
import com.idisc.pu.entities.Feed;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Sep 30, 2017 1:52:44 PM
 */
public class SitenameComparatorLastFeeddate extends ComparatorBase implements Comparator<String> {

    private transient static final Logger LOG = Logger.getLogger(SitenameComparatorLastFeeddate.class.getName());

    private final FeedDao feedService;
    
    private final Map<String, Feed> mostRecents;
    
    private final Feed FEED_PLACE_HOLDER;
    
    public SitenameComparatorLastFeeddate(JpaObjectFactory jpaContext, boolean reverseOrder) {
        super(reverseOrder);
        this.feedService = new FeedDao(Objects.requireNonNull(jpaContext));
        this.mostRecents = new HashMap<>();
        this.FEED_PLACE_HOLDER = new Feed();
    }
    
    @Override
    public int compare(String s1, String s2) {
        final int output;
        if(s1 == null && s2 == null) {
            output = 0;
        }else if(s2 == null) {
            output = this.isReverseOrder() ? -1 : 1;
        }else if(s1 == null) {
            output = this.isReverseOrder() ? 1 : -1;
        }else{
            final Date d1 = this.getDate(s1, null);
            final Date d2 = this.getDate(s2, null);
            output = super.compareDates(d1, d2);
            LOG.finer(() -> MessageFormat.format(
                    "Date comparison output: {0}, LHS:: site: {1}, last feeddate: {2}, RHS:: site: {3}, last feedddate: {4}", 
                    output, s1, d1, s2, d2));
        }
        return output;
    }
    
    public Date getDate(String site, Date outputIfNone) {
        Feed feed = this.mostRecents.getOrDefault(site, FEED_PLACE_HOLDER);
        if(feed == FEED_PLACE_HOLDER) {
            feed = this.feedService.getMostRecentForSite(site, null);
            this.mostRecents.put(site, feed);
        }
        final Date date = feed == null ? null : getDate(feed, null);
        return date == null ? outputIfNone : date;
    }
    
    public Date getDate(Feed feed, Date outputIfNone) {
        return feed.getFeeddate() == null ? feed.getDatecreated() : feed.getFeeddate();
    }
}
