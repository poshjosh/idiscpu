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

package com.idisc.pu.comparator.feed;

import com.idisc.pu.entities.Feed;
import java.util.List;
import com.idisc.pu.comparator.Scorer;

/**
 * @author Chinomso Bassey Ikwuagwu on Dec 12, 2017 9:42:25 PM
 */
public class DefaultFeedScorer implements Scorer<Feed>{

    @Override
    public Long apply(Feed feed) {
        
        long score = 0;
        
        score = this.addFeedhitScore(feed, score);
        
        score = this.addImageUrlScore(feed, score);
        
        return score;
    }
    
    public long addFeedhitScore(Feed feed, long score) {
        return score + this.getFeedhitScore(feed);
    }
    
    public int getFeedhitScore(Feed feed) {
        return this.countFeedHits(feed);
    }
    
    public long addImageUrlScore(Feed feed, long score) {
        
        String imageUrl = feed.getImageurl();
        
        if(imageUrl != null) {
            
            score = score < 1 ? 1 : (long)(score * 2);
        }
        
        return score;
    }
    
    public final int countFeedHits(Feed feed) {
        
        return sizeOf(feed.getFeedhitList());
    }

    private int sizeOf(List list) {
        
        return list == null ? 0 : list.size();
    }
}
