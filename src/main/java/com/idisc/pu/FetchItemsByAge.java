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

import com.bc.functions.GetDateOfAge;
import com.bc.jpa.context.PersistenceUnitContext;
import com.bc.jpa.dao.Select;
import com.idisc.pu.comparator.CompareByScore;
import com.idisc.pu.comparator.Scorer;
import com.idisc.pu.comparator.feed.DefaultFeedScorer;
import com.idisc.pu.entities.Feed;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

/**
 * @author Chinomso Bassey Ikwuagwu on Apr 16, 2018 6:59:12 PM
 */
public class FetchItemsByAge implements Serializable, BiFunction<Long, TimeUnit, List<Feed>> {

    private transient static final Logger LOG = Logger.getLogger(FetchItemsByAge.class.getName());

    private static final class FeedScore implements Serializable {
        private final Integer feedid;
        private final Long score;
        private FeedScore(Integer feedid, Long score) {
            this.feedid = Objects.requireNonNull(feedid);
            this.score = Objects.requireNonNull(score);
        }
        @Override
        public int hashCode() {
            int hash = 5;
            hash = 89 * hash + Objects.hashCode(this.feedid);
            hash = 89 * hash + Objects.hashCode(this.score);
            return hash;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final FeedScore other = (FeedScore) obj;
            if (!Objects.equals(this.feedid, other.feedid)) {
                return false;
            }
            if (!Objects.equals(this.score, other.score)) {
                return false;
            }
            return true;
        }
    }
    private static final class ScorerFeedScore implements Serializable, Scorer<FeedScore> {
        @Override
        public Long apply(FeedScore instance) {
            return instance.score;
        }
    }
    
    private final int limit;
    
    private final Scorer<Feed> scorer;
    
    private final PersistenceUnitContext puContext;

    private ProcessSelection processSelection;
    
    private Date date;

    public FetchItemsByAge(PersistenceUnitContext puContext) {
        this(puContext, new DefaultFeedScorer(), 10);
    }
    
    public FetchItemsByAge(PersistenceUnitContext puContext, Scorer<Feed> scorer, int limit) {
        this.puContext = Objects.requireNonNull(puContext);
        this.scorer = Objects.requireNonNull(scorer);
        this.limit = limit;
    }
    
    @Override
    public List<Feed> apply(Long maxAge, TimeUnit maxAgeTimeUnit) {
        
        LOG.fine(() -> "Fetching items by max age of "+maxAge+' '+maxAgeTimeUnit); 
        
        Objects.requireNonNull(maxAge);
        Objects.requireNonNull(maxAgeTimeUnit);
        
        this.date = new GetDateOfAge().get(maxAge, maxAgeTimeUnit);

        final Function<Select<Feed>, Select<Feed>> formatter = date == null ? (select) -> select :
                (select) -> select.where(Feed.class, "feeddate", Select.GT, date);
        
        final int max = -1;
        
        final int batchSize = 10;
        
        final List<FeedScore> scores = max < 1 ? new ArrayList() : new ArrayList(max);
        final Consumer<List<Feed>> batchConsumer = (batch) -> {
            try{
                for(Feed feed : batch) {
                    final Integer feedid = feed.getFeedid();
                    final Long score = scorer.apply(feed);
                    LOG.finer(() -> "Score: " + score + ", Feed:: ID:" + feedid + ", title: " + feed.getTitle());                
                    scores.add(new FeedScore(feedid, score));
                }
                
                LOG.fine(FetchItemsByAge.this.toString());
                
            }catch(RuntimeException e) {
                LOG.log(Level.WARNING, "Unexpected Exception", e); 
                throw new RuntimeException();
            }    
        };
        
        LOG.fine("Selecting feeds"); 

        processSelection = new ProcessSelection(
                puContext, formatter, batchConsumer,
                Feed.class, 0, batchSize, max
        );
        
        processSelection.run();

        LOG.log(Level.FINE, "Selected {0} feeds", scores.size()); 
        
        Collections.sort(scores, new CompareByScore(new ScorerFeedScore()));
        
        LOG.log(Level.FINE, "Done sorting {0} feeds", scores.size()); 

        final EntityManager em = puContext.getEntityManager();
        
        try{
        
            final Function<FeedScore, Feed> mapper = (score) -> {
                try{
                    final Feed feed = em.find(Feed.class, score.feedid);
                    LOG.finer(() -> "SELECTED. Score: " + score + ", Feed:: ID:" + score.feedid + ", title: " + feed.getTitle());                
                    return feed;
                }catch(RuntimeException e) {
                    LOG.log(Level.WARNING, "Unexpected Exception", e); 
                    throw new RuntimeException();
                }    
            };

            final List<Feed> feedList = scores.stream().limit(limit).map(mapper).collect(Collectors.toList());

            return feedList;
            
        }finally{
            em.close();
        }
    }

    @Override
    public String toString() {
        return super.toString() + "{From=" + date +", Selector: " + this.processSelection + '}';
    }
}
