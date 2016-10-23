package com.idisc.pu;

import com.bc.jpa.dao.BuilderForSelectImpl;
import javax.persistence.TypedQuery;
import com.bc.jpa.JpaContext;
import java.util.Objects;

/**
 *
 * @author Josh
 */
public class SearchDao<R> extends BuilderForSelectImpl<R> {

    private final int offset;
    
    private final int limit;

    public SearchDao(JpaContext cf, Class<R> resultType, String query, String... cols) {
        this(cf, resultType, -1, -1, query, cols);
    }
    
    public SearchDao(JpaContext cf, Class<R> resultType, int offset, int limit, String query, String... cols) {
        
        super(cf.getEntityManager(resultType), resultType, cf.getDatabaseFormat());
        
        SearchDao.this.from(resultType);
        
        if(query != null) {
        
            Objects.requireNonNull(cols);
        
            SearchDao.this.search(query, cols);
        }
       
        SearchDao.this.descOrder(cf.getMetaData().getIdColumnName(resultType));
        
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
