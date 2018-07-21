package com.idisc.pu;

import com.bc.functions.GetDateOfAge;
import com.bc.jpa.context.PersistenceUnitContext;
import com.bc.jpa.dao.Criteria.ComparisonOperator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import com.bc.jpa.dao.Select;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Aug 13, 2016 8:52:24 AM
 * @param <E>
 * @param <I>
 */
public class SelectByDate<E, I> implements Serializable {
    
    private transient static final Logger LOG = Logger.getLogger(SelectByDate.class.getName());

    private final PersistenceUnitContext puContext;
    
    private final Class<E> entityClass;
    
    public SelectByDate(PersistenceUnitContext jpaContext, Class<E> entityClass) {
    
        this.puContext = Objects.requireNonNull(jpaContext);
        
        this.entityClass = Objects.requireNonNull(entityClass);
    }

    public List<E> sort(List<E> resultList, Comparator<E> comparator, int maxOutputSize) {
        
        try{
            Collections.sort(resultList, comparator);
        }catch(IllegalArgumentException e) {
            LOG.log(Level.WARNING, "Exception while sorting list:\n{0}", resultList);             
            throw e;
        }

        final int resultsCount = resultList.size();

        final int finalMetricsSize = resultsCount < maxOutputSize ? resultsCount : maxOutputSize;

        List<E> outputList = finalMetricsSize >= resultsCount ? resultList : resultList.subList(0, finalMetricsSize);

        return outputList;
    }

    public List<E> getResultList(String dateColumnName, ComparisonOperator comparison, long maxAge, TimeUnit maxAgeTimeUnit, int batchSize) {
        
        return this.getResultList(dateColumnName, comparison, new GetDateOfAge().get(maxAge, maxAgeTimeUnit), batchSize);
    }
    
    public List<E> getResultList(String dateColumnName, ComparisonOperator comparison, Date maxAge, int batchSize) {
        
        return this.getResultList(dateColumnName, comparison, maxAge, -1, batchSize);
    }
        
    public List<E> getResultList(String dateColumnName, ComparisonOperator comparison, long maxAge, TimeUnit maxAgeTimeUnit, int maxSpread, int batchSize) {
        
        return this.getResultList(dateColumnName, comparison, new GetDateOfAge().get(maxAge, maxAgeTimeUnit), maxSpread, batchSize);
    }
    
    public List<E> getResultList(String dateColumnName, ComparisonOperator comparison, Date date, int maxSpread, final int batchSize) {

        final Function<Select<E>, Select<E>> formatter = date == null ? (select) -> select :
                (select) -> select.where(entityClass, dateColumnName, comparison, date);
        
        List<E> resultList = maxSpread < 1 ? new LinkedList() : new ArrayList<>(maxSpread);

        final Consumer<List<E>> batchConsumer = (batch) -> resultList.addAll(batch);
        
        new ProcessSelection(
                puContext, formatter, batchConsumer, 
                entityClass, 0, batchSize, maxSpread
        ).run();
        
        return resultList;
    }    
}
/**
 * 
    public List<E> getResultList_old(String dateColumnName, ComparisonOperator comparison, Date date, int maxSpread, final int batchSize) {

        int offset = 0;

        List<E> resultList = maxSpread < 1 ? new LinkedList() : new ArrayList<>(maxSpread);

        do{
            
            List<E> batch;
            
            try(Select<E> qb = puContext.getDaoForSelect(entityClass)) {
                
                if(date == null) {
                    batch = qb.from(entityClass).createQuery().setFirstResult(offset).setMaxResults(batchSize).getResultList();
                }else{
                    batch = qb.where(entityClass, dateColumnName, comparison, date)
                    .createQuery().setFirstResult(offset).setMaxResults(batchSize)
                    .getResultList();
                }    
            }

            final int currentBatchSize = this.sizeOf(batch);

            final int off = offset;
            logger.fine(() -> " =============Batch size: "+batchSize+", offset: "+off+", results: "+currentBatchSize);   

            if(currentBatchSize < 1) {
                break;
            }

            offset += currentBatchSize;

            resultList.addAll(batch);

        }while(maxSpread < 1 || offset < maxSpread);
        
        return resultList;
    }    
    
    private int sizeOf(List list) {
        return list == null ? 0 : list.size();
    }

 * 
 */