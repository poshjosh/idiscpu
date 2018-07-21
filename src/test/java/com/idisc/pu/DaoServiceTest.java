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

import com.bc.jpa.context.JpaContext;
import com.idisc.pu.entities.Installation;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import com.bc.jpa.dao.Select;

/**
 * @author Josh
 */
public class DaoServiceTest extends TestStub {
    
    public DaoServiceTest() { }

    /**
     * Test of isExistingScreenname method, of class InstallationService.
     */
    @Test
    public void testIsExisting() {
        
        System.out.println("isExisting");
        
//        this.isExisting(Installation.class, Installation_.screenname.getName(), String.class, 3);
        this.isExisting(Installation.class, "screenname", String.class, 3);
        
//        this.isExisting(Feed.class, Feed_.feeddate.getName(), Date.class, 3);
    }
    
    private <E> void isExisting(Class cls, String col, Class<E> colType, int count) {
        final JpaContext jpaContext = this.getJpaContext();
        final DaoBase instance = new DaoBase(jpaContext);
        try(Select<E> dao = jpaContext.getDaoForSelect(cls, colType)) {
            List<E> values = dao.select(col).createQuery().setMaxResults(count).getResultList();
            for(E value : values) {
                final boolean expResult = true;
                final boolean result = instance.isExisting(cls, col, value);
System.out.println("Is existing: "+result+", "+col+"="+value+" in table: "+cls.getSimpleName());                
                assertEquals(expResult, result);
            }
        }
    }
}
