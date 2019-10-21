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

import com.bc.jpa.context.PersistenceUnitContext;
import com.idisc.pu.entities.Installation;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import com.bc.jpa.dao.Select;
import com.idisc.pu.entities.Comment;
import com.idisc.pu.entities.Feeduser;

/**
 * @author Josh
 */
public class DaoBaseTest extends TestStub {
    
    public DaoBaseTest() { }

    /**
     * Test of isExistingScreenname method, of class InstallationService.
     */
    @Test
    public void testIsExisting() {
       
        System.out.println("isExisting");
        
        this.isExisting(Installation.class, "screenname", String.class, 2);
        
        this.isExisting(Feeduser.class, "emailAddress", String.class, 2);
        
//        this.isExisting(Feed.class, Feed_.feeddate.getName(), Date.class, 3);
    }
    
    private <E> void isExisting(Class cls, String col, Class<E> colType, int count) {
        
        final PersistenceUnitContext jpaContext = this.getPersistenceUnitContext();
        
        final DaoBase instance = new DaoBase(jpaContext);
        
        try(Select<E> select = jpaContext.getDaoForSelect(colType)) {
            List<E> values = select.from(cls).select(col).createQuery().setMaxResults(count).getResultList();
            for(E value : values) {
                final boolean result = instance.isExisting(cls, col, value);
System.out.println("Is existing: "+result+", "+col+"="+value+" in table: "+cls.getSimpleName());                
                assertTrue(result);
            }
        }
        final String shouldNotExist = "busuruXAXBXCXDXE";
        final boolean result = instance.isExisting(cls, col, shouldNotExist);
System.out.println("Is existing: "+result+", "+col+"="+shouldNotExist+" in table: "+cls.getSimpleName());                
        assertFalse(result);
    }
}
