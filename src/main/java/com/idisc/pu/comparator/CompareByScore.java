package com.idisc.pu.comparator;

import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

/**
 * @author Josh
 * @param <E>
 */
public class CompareByScore<E> implements Comparator<E> {
    
    private final boolean reverseOrder;
    
    private final Scorer<E> scorer;

    public CompareByScore(Scorer<E> scorer) {
        this(scorer, false);
    }

    public CompareByScore(Scorer<E> scorer, boolean reverseOrder) {
        this.scorer = Objects.requireNonNull(scorer);
        this.reverseOrder = reverseOrder;
    }
    
    @Override
    public int compare(E e1, E e2) {
        
        final long score1 = this.scorer.apply(e1);
        
        final long score2 = this.scorer.apply(e2);
        
        return this.compareLongs(score1, score2);
    }
    
    public final int compareInts(int x, int y) { 
        
        return this.reverseOrder ? Integer.compare(y, x) : Integer.compare(x, y);
    }
  
    public final int compareLongs(long x, long y) { 
        
        return this.reverseOrder ? Long.compare(y, x) : Long.compare(x, y);
    }

    public final int compareDates(Date date_a, Date date_b) { 
        if ((date_a == null) && (date_b == null)) {
            return 0;
        }    
        if (date_a == null) {
            return this.reverseOrder ? 1 : -1;
        }
        if (date_b == null) {
            return this.reverseOrder ? -1 : 1;
        }
        return this.reverseOrder ? date_b.compareTo(date_a) : date_a.compareTo(date_b);
    }
  
    public final boolean isReverseOrder() {
        
        return reverseOrder;
    }

    public Scorer<E> getScorer() {
        return scorer;
    }
}