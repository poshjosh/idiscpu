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

import com.bc.jpa.context.JpaContext;
import com.bc.jpa.dao.Select;
import com.idisc.pu.entities.Site;
import com.idisc.pu.entities.Site_;

/**
 * @author Chinomso Bassey Ikwuagwu on Jul 28, 2018 3:05:43 AM
 */
public class QueryTest extends TestStub {

    public static void main(String... args) {
        new QueryTest().test();
    }
    
    public void test() {
        final JpaContext jpaContext = this.getJpaContext();
        final Select<Site> select = jpaContext.getDao().forSelect(Site.class);
        final Site site = select.from(Site.class)
                .where(Site_.site, "naij")
                .and().where(Site_.sitetypeid, "web")
                .getSingleResultAndClose();
        if(site == null) {
            System.out.println("site == null");
        }else{
            System.out.println("ID: " + site.getSiteid() + ", name: " + site.getSite() + 
                    ", country: " + site.getCountryid() + ", timezone: " + site.getTimezoneid() +
                    ", iconurl: " + site.getIconurl());
        }
        System.out.println("Is open: " + select.isOpen());
    }
}
