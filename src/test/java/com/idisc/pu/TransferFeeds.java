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

import com.bc.jpa.context.PersistenceUnitContext;
import com.bc.jpa.dao.search.TextSearch;
import com.idisc.pu.entities.Feed;

/**
 * @author Chinomso Bassey Ikwuagwu on Aug 10, 2018 2:14:58 AM
 */
public class TransferFeeds extends TransferFromProductionToDev {

    public static void main(String... args) {
        new TransferFeeds().transfer(Feed.class, 40, 20);
    }
    
    private FeedDao _fd;
    
    public FeedDao getFeedDao(PersistenceUnitContext puCtx) {
        if(_fd == null) {
            _fd = new FeedDao(puCtx);
        }
        return _fd;
    }
    
    @Override
    public <E> void transferIfNone(PersistenceUnitContext targetPuContext, 
            TextSearch textSearch, DaoBase dao, E entity) {
        
        final Feed update = (Feed)dao.shallowCopy(entity);

        if(this.getFeedDao(targetPuContext).create(update, true)) {
            System.out.println("- - - - - - - \n\tPersisted: " + update);
        }
    }
}
