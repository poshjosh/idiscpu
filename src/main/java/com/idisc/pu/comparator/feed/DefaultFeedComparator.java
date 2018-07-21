package com.idisc.pu.comparator.feed;

import com.idisc.pu.comparator.CompareByScore;
import com.idisc.pu.entities.Feed;
import java.io.Serializable;

/**
 * @author Josh
 */
public class DefaultFeedComparator extends CompareByScore<Feed> implements Serializable {
    
    public DefaultFeedComparator() { 
        super(new DefaultFeedScorer());
    }

    public DefaultFeedComparator(boolean reverseOrder) {
        super(new DefaultFeedScorer(), reverseOrder);
    }
}
