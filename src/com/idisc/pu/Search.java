package com.idisc.pu;

import com.bc.jpa.dao.BuilderForSelectImpl;
import javax.persistence.TypedQuery;
import com.bc.jpa.JpaContext;

/**
 *
 * @author Josh
 */
public class Search<R> extends BuilderForSelectImpl<R> {

    private final int offset;
    
    private final int limit;

    public Search(JpaContext cf, Class<R> resultType, String query, String... cols) {
        this(cf, resultType, -1, -1, query, cols);
    }
    
    public Search(JpaContext cf, Class<R> resultType, int offset, int limit, String query, String... cols) {
        
        super(cf.getEntityManager(resultType), resultType, cf.getDatabaseFormat());
        
        Search.this.from(resultType);
        
        Search.this.descOrder(cf.getMetaData().getIdColumnName(resultType));
        
        if(query != null) {
        
            Search.this.search(query, cols);
        }
        
        this.offset = offset;
        
        this.limit = limit;
    }

    @Override
    public TypedQuery<R> format(TypedQuery<R> tq) {
        tq = super.format(tq); 
        if(this.offset >= 0) {
            tq.setFirstResult(offset);
        }
        if(this.limit > 0) {
            tq.setMaxResults(limit);
        }
        return tq;
    }
}
