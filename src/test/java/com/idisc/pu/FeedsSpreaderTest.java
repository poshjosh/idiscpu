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

import com.bc.jpa.dao.JpaObjectFactory;
import com.bc.jpa.dao.Select;
import com.bc.util.Util;
import com.idisc.pu.entities.Feed;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Josh
 */
public class FeedsSpreaderTest extends TestStub{
    
    private static JpaObjectFactory jpa;
    
    public FeedsSpreaderTest() { 
        if(jpa == null) {
            jpa = this.createJpaObjectFactory();
        }
    }

    /**
     * Test of spread method, of class FeedsSpreader.
     */
    @Test
    public void testSpread() {
        
        System.out.println("spread");

        final Select<Feed> select = jpa.getDaoForSelect(Feed.class);
        
        final List<Feed> feeds = select.getResultsAndClose(0, 400);
        
        System.out.println("Spreading " + feeds.size() + " feeds");
        
        long mb4 = Util.availableMemory();
        long tb4 = System.currentTimeMillis();
        
        final List<Feed> spread = new FeedsSpreader().spread(feeds, feeds.size() / 2);
        
        System.out.println("Done spreading " + spread.size() + " / " + feeds.size() + " feeds Consumed: time: " +
                (System.currentTimeMillis() - tb4) + ", memory: " + Util.usedMemory(mb4));
    }
}
