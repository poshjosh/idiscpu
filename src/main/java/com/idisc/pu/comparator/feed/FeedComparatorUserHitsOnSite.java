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

package com.idisc.pu.comparator.feed;

import com.idisc.pu.comparator.CompareByScore;
import com.idisc.pu.entities.Feed;
import com.idisc.pu.entities.Installation;
import java.util.Map;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;

/**
 * @author Chinomso Bassey Ikwuagwu on Dec 2, 2017 9:55:52 PM
 */
public class FeedComparatorUserHitsOnSite extends CompareByScore<Feed> implements AutoCloseable {
    
  public FeedComparatorUserHitsOnSite(EntityManager entityManager) {
    this(entityManager, null, false);
  } 
  
  public FeedComparatorUserHitsOnSite(
          EntityManager entityManager,
          Installation installation, 
          boolean reverseOrder) { 
    super(
            new FeedScoreUserSiteHitcount(entityManager, installation), 
            reverseOrder
    );
  }
  
  public FeedComparatorUserHitsOnSite(
          EntityManager entityManager,
          Map<Pattern, Integer> elite, 
          long valueToAddPerHit,
          long maxPossibleValueToAddPerHit, 
          boolean reverseOrder) { 
    this(entityManager, null, elite, valueToAddPerHit, maxPossibleValueToAddPerHit, reverseOrder);      
  }
  
  public FeedComparatorUserHitsOnSite(
          EntityManager entityManager,
          Installation installation, 
          Map<Pattern, Integer> elite, 
          long valueToAddPerHit,
          long maxPossibleValueToAddPerHit, 
          boolean reverseOrder) { 
    super(
            new FeedScoreUserSiteHitcount(
                    entityManager, installation, elite, 
                    valueToAddPerHit, maxPossibleValueToAddPerHit
            ), 
            reverseOrder
    );
  }  
  
  public boolean isOpen() {
    return ((FeedScoreUserSiteHitcount)this.getScorer()).isOpen();
  }

  @Override
  public void close() {
    if(this.isOpen()) {
      ((FeedScoreUserSiteHitcount)this.getScorer()).close();
    }
  }
}
