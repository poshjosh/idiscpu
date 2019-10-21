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

import com.bc.jpa.dao.JpaObjectFactory;
import com.bc.jpa.dao.Select;
import com.idisc.pu.entities.Feeduser;
import java.util.List;

/**
 * @author Josh
 */
public class JpaUpdateListenerTest extends TestStub {
    
    public JpaUpdateListenerTest() { }
    
    public static void main(String... args) {
        new JpaUpdateListenerTest().transfer(Feeduser.class, 0, Integer.MAX_VALUE);
    }
    
    public <E> void transfer(Class<E> cls, int offset, int limit) {
        
        try(final JpaObjectFactory jpa = this.createJpaObjectFactory()) {    

            final JpaConnectionLeakCounter cc = new JpaConnectionLeakCounter(JpaConnectionLeakCounter.MYSQL, jpa);
            try{
                cc.countLeaks();
            }catch(Exception e) {
                e.printStackTrace();
            }

            try(final Select<E> select = jpa.getDaoForSelect(cls)) {

                final List<E> resultList = select.createQuery()
                        .setFirstResult(0)
                        .setMaxResults(10)
                        .getResultList();

                for(E e : resultList) {

                    System.out.println(e);
                }
            }
        }
    }
}
