package com.idisc.pu;

import com.bc.jpa.dao.SelectDao;
import com.bc.jpa.search.SearchResults;
import java.util.Iterator;

/**
 * @author Josh
 */
public interface SearchResultsHandlerFactory extends AutoCloseable {

    Iterator<Class> getEntityTypes();
    
    Iterator<String> getKeys(Class entityType);
    
    <E> SearchResults<E> get(String sessionId, Class<E> entityType, SearchResults<E> outputIfNone);
    
    <E> SearchResults<E> get(String sessionId, SelectDao<E> dao, boolean createNew);

    int removeAll(boolean close);
    
    int removeAll(String sessionId, boolean close);
    
    <E> SearchResults<E> remove(String sessionId, Class<E> entityType, boolean close);
}
