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

package com.idisc.pu.comparator.feed;

import com.idisc.pu.comparator.ComparatorBase;
import com.idisc.pu.entities.Feed;
import java.util.Comparator;
import java.util.Date;

/**
 * @author Chinomso Bassey Ikwuagwu on Jan 21, 2019 5:17:50 PM
 */
public class FeedComparatorFeeddate extends ComparatorBase implements Comparator<Feed> {

    public FeedComparatorFeeddate(boolean reverseOrder) {
        super(reverseOrder);
    }

    @Override
    public int compare(Feed o1, Feed o2) {
        
        return this.compareDates(this.getDate(o1, null), this.getDate(o2, null));
    }
    
    private Date getDate(Feed feed, Date resultIfNone) {
        
        final Date date = feed.getFeeddate() == null ? feed.getDatecreated() : feed.getFeeddate();
        
        return date == null ? resultIfNone : date;
    }
}
