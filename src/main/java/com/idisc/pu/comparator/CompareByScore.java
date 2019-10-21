package com.idisc.pu.comparator;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author Josh
 * @param <E>
 */
public class CompareByScore<E> extends ComparatorBase implements Comparator<E> {
    
    private final Scorer<E> scorer;

    public CompareByScore(Scorer<E> scorer) {
        this(scorer, false);
    }

    public CompareByScore(Scorer<E> scorer, boolean reverseOrder) {
        super(reverseOrder);
        this.scorer = Objects.requireNonNull(scorer);
    }
    
    @Override
    public int compare(E e1, E e2) {
        
        final int result;
        
        if(e1 == null && e2 == null) {
            result = 0;
        }else if(e1 == null) {
            result = this.isReverseOrder() ? 1 : -1;
        }else if(e2 == null) {
            result = this.isReverseOrder() ? -1 : 1;
        }else{
            final long score1 = this.scorer.apply(e1);
            final long score2 = this.scorer.apply(e2);
            result = this.compareLongs(score1, score2);
        }
        return result;
    }

    public Scorer<E> getScorer() {
        return scorer;
    }
}