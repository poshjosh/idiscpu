package com.idisc.pu.comparator.feed;

import com.bc.util.StringComparator;
import com.bc.util.StringComparatorImpl;
import com.idisc.pu.comparator.CompareByScore;
import com.idisc.pu.entities.Feed;
import java.util.Objects;

/**
 * @author Josh
 */
public class FeedComparatorRelatedContents extends CompareByScore<Feed> {
    
    private final float tolerance;
    
    private final String text;
    
    private final StringComparator comp;

    public FeedComparatorRelatedContents(Feed feed) {
        this(feed, 0.25f, false);
    }

    public FeedComparatorRelatedContents(Feed feed, float tolerance, boolean reverseOrder) {
        super(new DefaultFeedScorer(), reverseOrder);
        if(tolerance < 0.0f || tolerance > 1.0f) {
            throw new IllegalArgumentException("Requires value between 0 and 1. Tolerance: " + tolerance);
        }
        this.tolerance = tolerance;
        this.text = this.getText(Objects.requireNonNull(feed));
        this.comp = new StringComparatorImpl();
    }

    @Override
    public int compare(Feed lhs, Feed rhs) {
        final int output;
        if(this.isNullOrEmpty(text)) {
            output = 0;
        }else{
            final String l = this.getText(lhs);
            final String r = this.getText(rhs);
            output = this.compare(l, r);
        }
        return output;
    }
    
    public String getText(Feed f) {
        final String ouput;
        final String title = f.getTitle();
        if(!this.isNullOrEmpty(title)) {
            ouput = title;
        }else {
            final String desc = f.getDescription();
            if(!this.isNullOrEmpty(desc)) {
                ouput = desc;
            }else{
                ouput = null;
            }
        }
        return ouput;
    }
    
    public int compare(String lhs, String rhs) {
        final int output;
        if(this.isNullOrEmpty(text)) {
            output = 0;
        }else{
            final boolean l = this.matches(lhs);
            final boolean r = this.matches(rhs);
            output = Boolean.compare(l, r);
        }
        return output;
    }
    
    private boolean matches(String s) {
        final boolean matches = this.isNullOrEmpty(s) ? false : this.comp.compare(text, s, tolerance);
        return matches;
    }
    
    private boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
