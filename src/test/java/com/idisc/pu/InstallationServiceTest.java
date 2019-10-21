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
import com.bc.jpa.dao.Delete;
import com.bc.jpa.dao.Select;
import com.idisc.pu.entities.Country;
import com.idisc.pu.entities.Installation;
import com.idisc.pu.entities.Installation_;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Josh
 */
public class InstallationServiceTest extends TestStub {
    
    public InstallationServiceTest() { }

    /**
     * Test of from method, of class InstallationDao.
     */
    @Test
    public void testFrom() {
        System.out.println("from");
        final PersistenceUnitContext jpa = this.getPersistenceUnitContext();
        final String installationkey = "689455AAAAAAAAAAAAAAAAAAAAAAAAAAAA48EEA7";
        try{
            User user = null;
            Integer installationid = null;
            String screenname = null;
            String ip = null;
            final Country country = jpa.getDao().find(Country.class, (short)566);
            final ZonedDateTime zdt = ZonedDateTime.now(ZoneOffset.UTC);
            long firstinstallationtime = zdt.toInstant().toEpochMilli();
            long lastinstallationtime = zdt.toInstant().toEpochMilli();
            boolean createIfNone = true;
            InstallationDao instance = new InstallationDao(jpa);
            Installation result = instance.from(user, installationid, installationkey, screenname, ip, country, firstinstallationtime, lastinstallationtime, createIfNone);
            this.log("Result: "+result);
        }finally{
            final Select<Installation> select = jpa.getDaoForSelect(Installation.class);
            final List<Installation> list = select
                    .where(Installation_.installationkey, installationkey).getResultsAndClose();
            if(list.size() == 1) {
                try(Delete<Installation> delete = jpa.getDaoForDelete(Installation.class)) {
                    final int updateCount = delete
                            .from(Installation.class)
                            .where(Installation_.installationkey, installationkey)
                            .executeUpdateCommitAndClose();
                    System.out.println(updateCount + " UPDATED");
                }
            }else{
                System.out.println("Expected only One result but found: " + list);
            }
        }
    }

    public void useThisToCreate() {
        final boolean thisRecordExitsInRemoteDatabase = true;
        System.out.println("from");
        User user = null;
        Integer installationid = 406;
        final String installationkey = "68945590B1703E6039325EE43D77CED29E48EEA7";
        String screenname = "user_5706b1758_9";
        String ip = null;
        final PersistenceUnitContext jpa = this.getPersistenceUnitContext();
        final Country country = null; 
        final ZonedDateTime zdt = ZonedDateTime.of(2016, 10, 18, 9, 8, 38, 0, ZoneOffset.UTC);
        long firstinstallationtime = zdt.toInstant().toEpochMilli();
        long lastinstallationtime = zdt.toInstant().toEpochMilli();
        boolean createIfNone = true;
        InstallationDao instance = new InstallationDao(jpa);
        Installation result = instance.from(user, installationid, installationkey, screenname, ip, country, firstinstallationtime, lastinstallationtime, createIfNone);
        this.log("Result: "+result);
    }
}
