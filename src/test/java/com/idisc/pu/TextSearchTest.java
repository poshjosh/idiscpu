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
import com.bc.jpa.dao.Criteria;
import com.idisc.pu.entities.Sitetype;
import java.util.List;

/**
 * @author Chinomso Bassey Ikwuagwu on Aug 16, 2018 2:03:28 PM
 */
public class TextSearchTest extends TestStub {

    public static void main(String... args) {
        new TextSearchTest().test();
    }
    
    private void test() {
        
        final PersistenceUnitContext jpa = this.getPersistenceUnitContext();
        
        final String sitetype = "web";
        
        final List resultList = jpa.getTextSearch().search(Sitetype.class, sitetype, Criteria.ComparisonOperator.EQUALS);
        
        System.out.println("Search for " + sitetype + " in " + Sitetype.class.getSimpleName() + " yielded: " + resultList);
    }
}
