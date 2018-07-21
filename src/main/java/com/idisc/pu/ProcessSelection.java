/*
 * Copyright 2017 NUROX Ltd.
 *
 * Licensed under the NUROX Ltd Software License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.looseboxes.com/legal/licenses/software.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idisc.pu;

import com.bc.jpa.context.PersistenceUnitContext;
import com.bc.jpa.dao.Select;
import com.bc.task.AbstractStoppableTask;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Dec 12, 2017 10:07:05 PM
 * @param <E>
 */
public class ProcessSelection<E> extends AbstractStoppableTask<Integer> {

    private transient static final Logger LOG = Logger.getLogger(ProcessSelection.class.getName());

    private final int offset;
    
    private final int pageSize;
    
    private final int max;
    
    private int position;
    
    private int pagesAttempted;
    
    private int succeeded;
    
    private final PersistenceUnitContext persistenceUnitContext;
    
    private final Function<Select<E>, Select<E>> formatter;
    
    private final Consumer<List<E>> batchConsumer;
    
    private final Class<E> entityClass;
    
    public ProcessSelection(
            PersistenceUnitContext persistenceUnitContext, 
            Function<Select<E>, Select<E>> formatter,
            Consumer<List<E>> batchConsumer,
            Class<E> entityClass,
            int offset, int pageSize, int max) {
    
        this.persistenceUnitContext = Objects.requireNonNull(persistenceUnitContext);
        
        this.formatter = Objects.requireNonNull(formatter);
        
        this.batchConsumer = Objects.requireNonNull(batchConsumer);
        
        this.entityClass = Objects.requireNonNull(entityClass);
        
        this.offset = offset;
        
        this.position = offset;
        
        this.pageSize = pageSize;
        
        this.max = max;
    }

    @Override
    public String getTaskName() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    protected Integer doCall() {

        do{
            
            List<E> batch;
            
            ++this.pagesAttempted;
            
            try(Select<E> select = persistenceUnitContext.getDaoForSelect(entityClass)) {
                
                select.from(entityClass);
                
                batch = this.formatter.apply(select)
                    .createQuery().setFirstResult(position).setMaxResults(pageSize)
                    .getResultList();
            }

            final int currentBatchSize = this.sizeOf(batch);

            final int off = position;
            LOG.fine(() -> "Batch size: "+pageSize+", offset: "+off+", results: "+currentBatchSize);   

            position += currentBatchSize;
            this.succeeded += currentBatchSize;

            batchConsumer.accept(batch);

            if(currentBatchSize < pageSize) {
                break;
            }

        }while((max < 1 || position < max) && !this.isStopRequested());
        
        return this.succeeded;
    }    
    
    private int sizeOf(List list) {
        return list == null ? 0 : list.size();
    }

    public int getOffset() {
        return offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getMax() {
        return max;
    }

    public int getPosition() {
        return position;
    }

    public int getPagesAttempted() {
        return pagesAttempted;
    }

    public int getSucceeded() {
        return succeeded;
    }

    public PersistenceUnitContext getPersistenceUnitContext() {
        return persistenceUnitContext;
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+"{offset=" + offset + ", pageSize=" + pageSize + ", max=" + max + ", position=" + position + ", pages attemptd: " + pagesAttempted + ", entityClass=" + entityClass + '}';
    }
}
