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

import com.bc.jpa.JpaContext;
import com.bc.jpa.dao.BuilderForSelect;
import com.bc.jpa.dao.Criteria;
import com.bc.util.XLogger;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import javax.persistence.NoResultException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/**
 * @author Chinomso Bassey Ikwuagwu on Sep 24, 2016 1:45:25 PM
 */
public class DaoService {
    
  private final JpaContext jpaContext;

  public DaoService(JpaContext jpaContext) {
    this.jpaContext = jpaContext;
  }
    
  public void convertParams(Class entityType, Map params) {
      
      Map typeParams = this.jpaContext.getDatabaseParameters(entityType, params);
      
      if(typeParams == null || typeParams.isEmpty()) {
          return;
      }
      
      final BuilderForSelect<?> dao = this.jpaContext.getBuilderForSelect(entityType);
      
      final List<?> found = dao.where(entityType, typeParams).getResultsAndClose();
      
      if(found != null && !found.isEmpty()) {
          if(found.size() == 1) {
              Object entity = found.get(0);
              String name = this.jpaContext.getMetaData().getIdColumnName(entityType);
              params.put(name, entity);
              params.keySet().removeAll(typeParams.keySet());
          }else{
              XLogger.getInstance().log(Level.WARNING, "Found > 1 results, where only 1 expected in table: {0}, using parameters: {1}", 
                      this.getClass(), entityType.getSimpleName(), typeParams);
          }
      }
  }
  
  public Map<String, String> parseJsonParameters(Class entityType, Map<String, String> params) {
      Map<String, String> output = params;
      final String idColumnName = this.jpaContext.getMetaData().getIdColumnName(entityType);
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
              XLogger.getInstance().log(Level.WARNING, "Error parsing: "+idColumnValue, this.getClass(), e);
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
    Map entityParams = jpaContext.getDatabaseParameters(entityType, params);
    if(entityParams == null || entityParams.isEmpty()) {
      entity = defaultValue;
    }else{
      try{
            entity = jpaContext.getBuilderForSelect(entityType)
                    .where(entityType, entityParams).getSingleResultAndClose();
      }catch(NoResultException e) {
        entity = defaultValue;
      }
    }
    return entity;
  }
  
  public <E> E getEntity(Class<E> entityType, String columnName, Object columnValue, E defaultValue) {
      
    E output;
    
    try(BuilderForSelect<E> dao = this.jpaContext.getBuilderForSelect(entityType)) {
    
        Objects.requireNonNull(columnName, "Attempted to select a 'null' column from the table: "+entityType.getSimpleName());

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

  public boolean exists(Class entityType, Criteria.LogicalOperator logicalOptr, Map<String, String> params)  {
      
    try(BuilderForSelect dao = jpaContext.getBuilderForSelect(entityType)) {
    
        dao.where(entityType, BuilderForSelect.EQ, logicalOptr, params);

        final List found = dao.createQuery().setMaxResults(1).getResultList();

        final boolean output = found != null && !found.isEmpty();

        XLogger.getInstance().log(Level.FINE, "Output: {0}", this.getClass(), output);

        return output;
    }
  }

  public JpaContext getJpaContext() {
    return jpaContext;
  }
}
