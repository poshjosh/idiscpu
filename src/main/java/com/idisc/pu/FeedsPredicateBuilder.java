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

import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

/**
 * @author Chinomso Bassey Ikwuagwu on Mar 13, 2019 10:47:00 AM
 */
public class FeedsPredicateBuilder extends SearchPredicateBuilder<com.idisc.pu.entities.Feed> {

    private CriteriaQuery criteriaQuery;
    
    @Override
    public List<Predicate> build() throws EntityNotFoundException {
        
        if(getEntityClass() == null) {
            this.entityClass(com.idisc.pu.entities.Feed.class);
        }
        
        final List<Predicate> result = super.build();
        
        return result;
    }

    @Override
    public void format(List<Predicate> target) { 
//        final Predicate feedNotYetViewed = this.feedNotYetViewedPredicate();
//        if(feedNotYetViewed != null) {
//            target.add(feedNotYetViewed);
//        }
    }

    public Predicate feedNotYetViewedPredicate() {
        throw new UnsupportedOperationException();
    }

    public CriteriaQuery getCriteriaQuery() {
        return criteriaQuery;
    }

    public FeedsPredicateBuilder criteriaQuery(CriteriaQuery arg) {
        this.criteriaQuery = arg;
        return this;
    }
}
