/*
 * Copyright 2016 NUROX Ltd.
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

import com.bc.jpa.dao.util.EntityMemberAccess;
import com.bc.jpa.dao.Criteria;
import com.bc.jpa.dao.JpaObjectFactory;
import java.util.logging.Logger;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import com.bc.jpa.dao.Select;
import com.bc.jpa.dao.functions.GetIdAttribute;
import com.bc.reflection.ReflectionUtil;
import java.util.Collection;

/**
 * @author Chinomso Bassey Ikwuagwu on Sep 24, 2016 1:45:25 PM
 */
public class DaoBase {
    
    private transient static final Logger LOG = Logger.getLogger(DaoBase.class.getName());
    
    private final JpaObjectFactory jpaObjectFactory;

    public DaoBase(JpaObjectFactory jpaObjectFactory) {
        this.jpaObjectFactory = jpaObjectFactory;
    }
    
    public void convertParams(Class entityType, Map params) {
      
        Map typeParams = this.jpaObjectFactory.getDatabaseFormat().toDatabaseFormat(entityType, params);
      
        if(typeParams == null || typeParams.isEmpty()) {
            return;
        }
      
        final Select<?> dao = this.jpaObjectFactory.getDaoForSelect(entityType);
      
        final List<?> found = dao.where(entityType, typeParams).getResultsAndClose();
      
        if(found != null && !found.isEmpty()) {
            if(found.size() == 1) {

                Object entity = found.get(0);
              
//              String name = this.jpaContext.getMetaData().getIdColumnName(entityType);
                final String name = this.getIdColumnName(entityType, Object.class);

                params.put(name, entity);
                params.keySet().removeAll(typeParams.keySet());
            }else{
                if(LOG.isLoggable(Level.WARNING)){
                        LOG.log(Level.WARNING, 
                        "Found > 1 results, where only 1 expected in table: {0}, using parameters: {1}", new Object[]{ entityType.getSimpleName(),  typeParams});
                }
            }
        }
    }
  
    public Map<String, String> parseJsonParameters(Class entityType, Map<String, String> params) {
        Map<String, String> output = params;
//      final String idColumnName = this.jpaContext.getMetaData().getIdColumnName(entityType);
        final String idColumnName = this.getIdColumnName(entityType, Object.class);
        String idColumnValue = params.get(idColumnName);
        if(idColumnValue != null) {
            idColumnValue = idColumnValue.trim();
            final int len = idColumnValue.length();
            if(len > 1 && idColumnValue.charAt(0) == '{' && idColumnValue.charAt(len-1) == '}') { 
                if(idColumnValue.replaceAll("\\s", "").equals("{}")) {
                    params.remove(idColumnName);
                    output = params;
                }else{
                    try{  
                        JSONObject json = (JSONObject)JSONValue.parseWithException(idColumnValue);
                        output = json;
                    }catch(ParseException e) {
                        LOG.log(Level.WARNING, "Error parsing: "+idColumnValue, e);
                    }
                }
            }
        }
        return output;
    }
  
    public <E> E getEntity(Class<E> entityType, Map params, E defaultValue) {
        Objects.requireNonNull(entityType);
        Objects.requireNonNull(params);
        E entity;
        Map entityParams = jpaObjectFactory.getDatabaseFormat().toDatabaseFormat(entityType, params);
        if(entityParams == null || entityParams.isEmpty()) {
            entity = defaultValue;
        }else{
            try{
                entity = jpaObjectFactory.getDaoForSelect(entityType)
                    .where(entityType, entityParams).getSingleResultAndClose();
            }catch(NoResultException e) {
                entity = defaultValue;
            }
        }
        return entity;
    }
  
    public <E> E getEntity(Class<E> entityType, String columnName, Object columnValue, E defaultValue) {
      
        E output;

        try(Select<E> dao = this.jpaObjectFactory.getDaoForSelect(entityType)) {

            Objects.requireNonNull(columnName, "Attempted to select a 'null' column from the table: " + 
                    entityType.getSimpleName());

            if (columnValue == null || columnValue.toString().isEmpty()) {
                throw new NullPointerException("Required parameter "+columnName+" is missing");
            }

            try {

                List<E> found = dao.where(entityType, columnName, columnValue).createQuery().getResultList();

                if (found==null || found.isEmpty()) {
                    output = defaultValue;
                } else { 
                    final int resultsCount = found.size();  
                    if (resultsCount == 1) {
                        output = found.get(0);
                    } else {
                        throw new UnsupportedOperationException(
                            "Found "+resultsCount+" records, whereas only 1 was expected for " + 
                                    entityType.getSimpleName()+'.'+columnName+" = " + columnValue);
                    }  
                }
            } catch (RuntimeException e) {
                throw new RuntimeException("Error accessing database for " + 
                        entityType.getSimpleName()+'.'+columnName+" = " + columnValue, e);
            }
        }
        return output;
    }

    public boolean isExisting(Class entityType, Criteria.LogicalOperator logicalOptr, Map params)  {
        
        LOG.fine(() -> "Entity type: " + entityType.getSimpleName() + 
                ", Logical optr: " + logicalOptr.name() +
                ", where: " + params);
      
        try(Select dao = jpaObjectFactory.getDaoForSelect(entityType)) {

            final Map update = jpaObjectFactory.getDatabaseFormat().toDatabaseFormat(entityType, params);
            
            if(update.isEmpty()) {
                throw new IllegalArgumentException(
                "Where parameters is empty. An SQL where clause is required for the requested operation");
            }
            
            dao.where(entityType, Select.EQ, logicalOptr, update);

            final List found = dao.createQuery().setMaxResults(1).getResultList();

            final boolean result = found != null && !found.isEmpty();

            LOG.log(Level.FINE, "Result: {0}", result);

            return result;
        }
    } 

    public boolean isExisting(Class entityType, String column, Object value) {
        boolean found;
        EntityManager em = this.jpaObjectFactory.getEntityManager();
        try{
          Query query = em.createQuery(
                  "SELECT a."+column+" FROM "+entityType.getSimpleName()+" a WHERE a."+column+" = :"+column);
          query.setParameter(column, value);
          query.setFirstResult(0).setMaxResults(1);
          found = query.getSingleResult() != null;
        }catch(NoResultException ignored) {
            found = false;
        }finally{
            if(em.isOpen()) {
                em.close();
            }
        }
        return found;
    }
  
    public <E> E shallowCopy(E src) {
        final Class<E> srcClass = (Class<E>)src.getClass();
        final E tgt = new ReflectionUtil().newInstance(srcClass);
        final EntityMemberAccess.UpdateTest test = (name, srcValue, tgtValue) -> {
            return !(srcValue instanceof Collection);
        };
        final EntityMemberAccess<E, Object> memberAccess = this.jpaObjectFactory.getEntityMemberAccess(srcClass);
        memberAccess.update(src, tgt, test);
        return tgt;
    }
    
    public String getIdColumnName(Class entityType, Class idType) {
        final String name = new GetIdAttribute(
                this.jpaObjectFactory.getEntityManagerFactory()).apply(entityType, idType).getName();
        return name;
    }
    
    public JpaObjectFactory getJpaContext() {
        return jpaObjectFactory;
    }
}
