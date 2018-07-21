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

import com.idisc.pu.entities.Country;
import com.idisc.pu.entities.Installation;
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
        User user = null;
        Integer installationid = null;
        String installationkey = "68945590B1703E6039325EE43D77CED29E48EEA7";
        String screenname = null;
        Country country = null;
        long firstinstallationtime = -1L;
        long lastinstallationtime = -1L;
        boolean createIfNone = false;
        InstallationDao instance = new InstallationDao(this.getJpaContext());
//        Installation expResult = null;
        Installation result = instance.from(user, installationid, installationkey, screenname, country, firstinstallationtime, lastinstallationtime, createIfNone);
//        assertEquals(expResult, result);
        this.log("Found: "+result);
    }
}
