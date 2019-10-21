/*
 * Copyright 2019 NUROX Ltd.
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

import com.bc.jpa.dao.util.DatabaseFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author Chinomso Bassey Ikwuagwu on Mar 13, 2019 9:36:45 AM
 */
public class SearchPredicateBuilder<T> {

    private transient static final Logger LOG = Logger.getLogger(SearchPredicateBuilder.class.getName());
    
    private Class<T> entityClass;
    
    private CriteriaBuilder criteriaBuilder;
    
    private Root<T> root;
    
    private String idColumnName;
    
    private String [] columnsToSearch; 
    
    private String [] queries; 
    
    private List ids;
    
    private String dateColumn;

    private Date after; 
    
    private Map<String, Object> searchParams; 
    
    private DatabaseFormat databaseFormat;
    
    public SearchPredicateBuilder() { }
    
    public SearchPredicateBuilder selector(com.bc.jpa.dao.Select<T> select) {
        this.criteriaBuilder(select.getCriteriaBuilder());
        final CriteriaQuery criteriaQuery = select.getCriteriaQuery();
        this.root(this.getRoot(criteriaQuery));
        if(this.entityClass == null) {
            this.entityClass = (Class<T>)root.getJavaType();
        }
        return this;
    }
    
    public Root<T> getRoot(CriteriaQuery criteriaQuery) {
        final Root mRoot;
        final Set<Root> roots;
        if((roots = criteriaQuery.getRoots()) == null || roots.isEmpty()) {
            mRoot = criteriaQuery.from(entityClass);
        }else if(roots.size() == 1) {
            mRoot = roots.iterator().next();
        }else{
            throw new IllegalStateException();  
        }
        return mRoot;
    }
    
    public List<Predicate> build() throws EntityNotFoundException {
        
        final Predicate eqIdVals;
        if(ids != null && !ids.isEmpty()) {
          final Predicate [] eqIdVals_arr = new Predicate[ids.size()];
          for(int i=0; i<ids.size(); i++) {
            eqIdVals_arr[i] = criteriaBuilder.equal(root.get(idColumnName), ids.get(i));
          }
          eqIdVals = criteriaBuilder.or(eqIdVals_arr);
        }else{
          eqIdVals = null;
        }

        final Predicate eqQueries;
        if(queries != null && queries.length != 0) {
          final Predicate [] eqQueries_arr = new Predicate[queries.length];
          for(int i=0; i<queries.length; i++) {
            if(queries[i] == null) {
                continue;
            }
            final String query = queries[i].trim();
            if(query.isEmpty()) {
                continue;
            }

            final Predicate [] eqQuery_arr = new Predicate[columnsToSearch.length];

            for(int j=0; j<columnsToSearch.length; j++) {
              final String col = columnsToSearch[j];
              eqQuery_arr[j] = criteriaBuilder.like(root.get(col), '%' + query + '%');    
            }

            eqQueries_arr[i] = criteriaBuilder.or(eqQuery_arr);
          }

          eqQueries = criteriaBuilder.or(eqQueries_arr);
        }else{
          eqQueries = null;  
        }

        final Predicate eqIdValsOrQueries;
        if(eqIdVals != null && eqQueries != null) {
          eqIdValsOrQueries = criteriaBuilder.or(eqIdVals, eqQueries);
        }else if(eqIdVals != null) {
          eqIdValsOrQueries = eqIdVals; 
        }else if(eqQueries != null) {
          eqIdValsOrQueries = eqQueries;
        }else{
          eqIdValsOrQueries = null;
        }

        final Predicate gtDate;
        if(after != null && dateColumn != null) {
          gtDate = criteriaBuilder.greaterThan(root.get(dateColumn), after);      
        }else{
          gtDate = null;  
        }

        final Predicate eqParams;
        if(searchParams != null && !searchParams.isEmpty()){

          final List<Predicate> eqParams_list = new ArrayList<>(searchParams.size());

          for(String key : searchParams.keySet()) {

            try{

                final Object val = searchParams.get(key);

                final Object update = databaseFormat.toDatabaseFormat(entityClass, key, val, val);
                
                LOG.finer(() -> MessageFormat.format(" DatabaseFormat. {0}#{1} {2} = {3}", 
                        entityClass.getSimpleName(), key, val, update));

                eqParams_list.add(criteriaBuilder.equal(root.get(key), update));

            }catch(EntityNotFoundException e) {

//                LOG.warning(() -> "Invalid value for: " + key + ". " + e);
                LOG.log(Level.WARNING, "Invalid value for: " + key, e);
            }
          }

          eqParams = criteriaBuilder.and(eqParams_list.toArray(new Predicate[0]));

        }else{
          eqParams = null;
        }

        final List<Predicate> whereList = new ArrayList<>();
        if(eqIdValsOrQueries != null) {
          whereList.add(eqIdValsOrQueries);
        }
        if(gtDate != null) {
          whereList.add(gtDate);
        }
        if(eqParams != null) {
          whereList.add(eqParams);
        }
        
        this.format(whereList);

        return whereList.isEmpty() ? Collections.EMPTY_LIST : Collections.unmodifiableList(whereList);
    }
    
    public void format(List<Predicate> target) { }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public SearchPredicateBuilder entityClass(Class<T> arg) {
        this.entityClass = arg;
        return this;
    }

    public DatabaseFormat getDatabaseFormat() {
        return databaseFormat;
    }

    public SearchPredicateBuilder databaseFormat(DatabaseFormat arg) {
        this.databaseFormat = arg;
        return this;
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return criteriaBuilder;
    }

    public SearchPredicateBuilder criteriaBuilder(CriteriaBuilder arg) {
        this.criteriaBuilder = arg;
        return this;
    }

    public Root<T> getRoot() {
        return root;
    }

    public SearchPredicateBuilder root(Root<T> arg) {
        this.root = arg;
        return this;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

    public SearchPredicateBuilder idColumnName(String arg) {
        this.idColumnName = arg;
        return this;
    }

    public String[] getColumnsToSearch() {
        return columnsToSearch;
    }

    public SearchPredicateBuilder columnsToSearch(String[] args) {
        this.columnsToSearch = args;
        return this;
    }

    public String[] getQueries() {
        return queries;
    }

    public SearchPredicateBuilder queries(String[] args) {
        this.queries = args;
        return this;
    }

    public List getIds() {
        return ids;
    }

    public SearchPredicateBuilder ids(List arg) {
        this.ids = arg;
        return this;
    }

    public String getDateColumn() {
        return dateColumn;
    }

    public SearchPredicateBuilder dateColumn(String arg) {
        this.dateColumn = arg;
        return this;
    }

    public Date getAfter() {
        return after;
    }

    public SearchPredicateBuilder after(Date arg) {
        this.after = arg;
        return this;
    }

    /**
     * @return A copy
     */
    public Map<String, Object> getSearchParams() {
        return Collections.unmodifiableMap(searchParams);
    }

    public SearchPredicateBuilder searchParams(Map<String, Object> arg) {
        this.searchParams = arg;
        return this;
    }
}
