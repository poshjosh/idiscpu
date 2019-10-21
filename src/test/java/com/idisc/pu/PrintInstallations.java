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
import com.idisc.pu.entities.Installation;
import java.util.List;
import org.junit.Test;

/**
 * @author Chinomso Bassey Ikwuagwu on Sep 29, 2018 3:10:08 PM
 */
public class PrintInstallations extends TestStub {

    public static void main(String... args) {
        final PrintInstallations test = new PrintInstallations();
        test.testDefault();
    }
    
    @Test
    public void testDefault() {
        try(final JpaObjectFactory jpa = this.createJpaObjectFactory(this.getPuName())) {
            this.test(jpa, 0, 10_000);
        }
    }
    
    public String getPuName() {
        return com.idisc.pu.PersistenceNames.PERSISTENCE_UNIT_NAME;
    }
    
    public void test(JpaObjectFactory jpa, int offset, int limit) {
        final List<Installation> resultList =        
                jpa.getDaoForSelect(Installation.class)
                .from(Installation.class).getResultsAndClose(offset, limit);
        for(Installation inst : resultList) {
            System.out.println("ID:"+inst.getInstallationid()+
                    ", name:" + inst.getScreenname() +
                    ", location:" + inst.getLocationid() +
                    ", key:" + inst.getInstallationkey() +
                    "\nDates. First install:" + inst.getFirstinstallationdate() +
                    ", first subscription:" + inst.getFirstsubscriptiondate() +
                    ", last install:" + inst.getLastinstallationdate());
        }
    }
}
