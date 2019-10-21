package com.idisc.pu.comparator.feed;

import com.bc.util.Similarity;
import com.bc.util.TextSimilarity;
import com.idisc.pu.comparator.CompareByScore;
import com.idisc.pu.entities.Feed;

/**
 * @author Josh
 */
public class FeedComparatorRelatedContent extends CompareByScore<Feed> {
    
    private final String text;
    
    private final Similarity similarity;

    public FeedComparatorRelatedContent(Feed feed) {
        this(toText(feed));
    }
    
    public FeedComparatorRelatedContent(String text) {
        this(text, true);
    }

    public FeedComparatorRelatedContent(Feed feed, boolean reverseOrder) {
        this(toText(feed), reverseOrder);
    }
    
    public FeedComparatorRelatedContent(String text, boolean reverseOrder) {
        super(new DefaultFeedScorer(), reverseOrder);
        this.text = text;
        this.similarity = new TextSimilarity(true);
    }

    @Override
    public int compare(Feed lhs, Feed rhs) {
        final int output;
        if(isNullOrEmpty(text)) {
            output = 0;
        }else{
            final String l = toText(lhs);
            final String r = toText(rhs);
            output = this.compare(l, r);
        }
        return output;
    }
    
    public String getText(Feed feed) {
        return toText(feed);
    }
    
    private static String toText(Feed f) {
        final String ouput;
        final String title = f.getTitle();
        if( ! isNullOrEmpty(title)) {
            ouput = title;
        }else {
            final String desc = f.getDescription();
            if( ! isNullOrEmpty(desc)) {
                ouput = desc;
            }else{
                ouput = null;
            }
        }
        return ouput;
    }
    
    public int compare(String lhs, String rhs) {
        final int output;
        if(isNullOrEmpty(text)) {
            output = 0;
        }else{
            if(isNullOrEmpty(lhs) && isNullOrEmpty(rhs)) {
                output = 0;
            }else if(isNullOrEmpty(lhs)) {
                output = this.isReverseOrder() ? 1 : -1;
            }else if(isNullOrEmpty(rhs)) {
                output = this.isReverseOrder() ? -1 : 1;
            }else{
                final int l = this.similarity.similarity(text, lhs);
                final int r = this.similarity.similarity(text, rhs);
                return this.isReverseOrder() ? Integer.compare(r, l) : Integer.compare(l, r);
            }
        }
        return output;
    }
    
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
