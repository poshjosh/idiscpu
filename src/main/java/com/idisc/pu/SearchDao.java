package com.idisc.pu;

import com.bc.jpa.dao.SelectImpl;
import javax.persistence.TypedQuery;
import com.bc.jpa.context.JpaContext;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * @author Josh
 */
public class SearchDao<R> extends SelectImpl<R> {
    
    private transient static final Logger LOG = Logger.getLogger(SearchDao.class.getName());

    private final int offset;
    
    private final int limit;

    public SearchDao(JpaContext jpa, Class<R> resultType, String query, String... cols) {
        this(jpa, resultType, -1, -1, query, cols);
    }
    
    public SearchDao(JpaContext jpa, Class<R> resultType, 
            int offset, int limit, String query, String... cols) {
        this(jpa, resultType, offset, limit, Collections.singleton(query), cols);
    }
        
        
    public SearchDao(JpaContext jpa, Class<R> resultType, int offset, int limit, 
            Collection<String> queryList, String... cols) {
        
        super(jpa.getEntityManager(resultType), resultType, jpa.getDatabaseFormat());
        
        SearchDao.this.from(resultType);
        
        if(queryList != null && !queryList.isEmpty()) {
        
            Objects.requireNonNull(cols);
            
            for(String query : queryList) {
                if(query == null) {
                    continue;
                }
                query = query.trim();
                if(query.isEmpty()) {
                    continue;
                }
                SearchDao.this.search(query, cols);
            }
        }
       
        SearchDao.this.descOrder(jpa.getMetaData().getIdColumnName(resultType));
        
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
