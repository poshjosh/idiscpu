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

import com.idisc.pu.entities.Feed;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author Chinomso Bassey Ikwuagwu on Sep 27, 2018 8:18:38 PM
 */
public class FeedsSpreader implements Spreader<Feed>, Serializable{
    
    private final int bucketSize;
    
    private final long maxTimeDifferenceBetweenFeedsInBucketMillis = TimeUnit.HOURS.toMillis(1);

    public FeedsSpreader() {
        this(50);
    }
    
    public FeedsSpreader(int chunkSize) {
        this.bucketSize = chunkSize;
    }

    @Override
    public List<Feed> spread(List<Feed> feeds, int outputSize) {
        
        final int chunkSize = this.bucketSize < outputSize ? outputSize : this.bucketSize;

        final List<Feed> output = new ArrayList<>(outputSize);
        
        final List<Feed> chunk = new ArrayList<>(chunkSize);
        
        final int batchSize = 5;
        int indexInBatch = 0;
        
        for(Feed feed : feeds) {
            
            chunk.add(feed);
            
            final long diffMillis;
            if(++indexInBatch >= batchSize) {
                diffMillis = this.timeDifferenceBetweenFeedsMillis(chunk, -1);
                indexInBatch = 0;
            }else{
                diffMillis = -1;
            }
            
            if(diffMillis >= this.maxTimeDifferenceBetweenFeedsInBucketMillis || chunk.size() >= chunkSize) {
                
                Collections.shuffle(chunk, ThreadLocalRandom.current());
                
                output.addAll(chunk);
                
                chunk.clear();
                
                if(output.size() >= outputSize) {
                    
                    break;
                }
            }
        }
        
        while(output.size() > outputSize) {
            
            output.remove(output.size() - 1);
        }
        
        return output;
    }
    
    private long timeDifferenceBetweenFeedsMillis(Collection<Feed> feeds, long outputIfNone) {
        long earliest = -1;
        long latest = -1;
        for(Feed feed : feeds) {
            final Date date = this.getDate(feed, null);
            if(date == null) {
                continue;
            }
            final long time = date.getTime();
            if(earliest == -1) {
                earliest = time;
            }else{
                earliest = Math.min(earliest, time);
            }
            latest = Math.max(latest, time);
        }
        return earliest == -1 || latest == -1 ? outputIfNone : latest - earliest;
    }
    
    public Date getDate(Feed feed, Date outputIfNone) {
        Date date = feed.getFeeddate();
        if(date == null) {
            date = feed.getTimemodified();
            if(date == null) {
                date = feed.getDatecreated();
            }
        }
        return date == null ? outputIfNone : date;
    }
}
