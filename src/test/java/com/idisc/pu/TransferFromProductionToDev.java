/*
 * Copyright 2018 NUROX Ltd.
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

import com.bc.jpa.context.PersistenceContext;
import com.bc.jpa.context.PersistenceUnitContext;
import com.bc.jpa.dao.Select;
import com.bc.jpa.dao.search.TextSearch;
import com.idisc.pu.entities.Feeduser;
import java.util.Collection;

/**
 * @author Chinomso Bassey Ikwuagwu on Sep 13, 2018 7:08:29 PM
 */
public class TransferFromProductionToDev extends TestStub {

    public static void main(String... args) {
        new TransferFromProductionToDev().transfer(Feeduser.class, 0, Integer.MAX_VALUE);
    }
    
    public <E> void transfer(Class<E> cls, int offset, int limit) {
        
        try (final PersistenceContext ctxLive = this.getPersistenceContextProductionMode();
                final PersistenceContext ctxDummy = this.getPersistenceContextDevMode()) {

            final PersistenceUnitContext puCtxLive = ctxLive.getContext(PersistenceNames.PERSISTENCE_UNIT_NAME);
            final PersistenceUnitContext puCtxDummy = ctxDummy.getContext(PersistenceNames.PERSISTENCE_UNIT_NAME);

            final TextSearch textSearch = puCtxDummy.getTextSearch();
            final DaoBase dao = new DaoBase(puCtxDummy);
            final String idColumnName = puCtxDummy.getMetaData().getIdColumnName(cls);

            try(final Select<E> select = puCtxLive.getDaoForSelect(cls)) {
                
                select.ascOrder(idColumnName).createQuery()
                        .setFirstResult(offset)
                        .setMaxResults(limit)
                        .getResultList().stream().forEach((entity) -> {
                            try{

                                transferIfNone(puCtxDummy, textSearch, dao, entity);
                                
                            }catch(Exception e) {
                                e.printStackTrace();
                            }
                        });
            }
        }
    }
    
    public <E> void transferIfNone(PersistenceUnitContext targetPuContext, 
            TextSearch textSearch, DaoBase dao, E entity) {
        
        final Collection<E> found = textSearch.searchEntityRecords(entity, String.class);

        final boolean persist = found == null || found.isEmpty();

        System.out.println("- - - - - - - \n\tPersist: " + persist + ", entity: " + entity);

        if(persist) {
            
            final E update = dao.shallowCopy(entity);
            
            targetPuContext.getDao().persistAndClose(update);
            
            System.out.println("- - - - - - - \n\tPersisted: " + update);
        }
    }
}
