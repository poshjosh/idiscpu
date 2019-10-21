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

import com.bc.jpa.dao.Criteria;
import com.bc.jpa.dao.JpaObjectFactory;
import com.bc.jpa.dao.Select;
import com.idisc.pu.entities.Feed;
import com.idisc.pu.entities.Feed_;
import com.idisc.pu.entities.Feedhit;
import com.idisc.pu.entities.Feedhit_;
import com.idisc.pu.entities.Installation;
import com.idisc.pu.entities.Installation_;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 22, 2018 12:32:01 AM
 */
public class SelectAdvertForInstallation extends TestStub {

    public static void main(String... args) {
        
        final SelectAdvertForInstallation test = new SelectAdvertForInstallation();
        try(final JpaObjectFactory jpa = test.createJpaObjectFactory()) {
            test.test(jpa, 0, 100);
        }
    }
    
    public void test(JpaObjectFactory jpa, int offset, int limit) {
        
        final Installation installation = this.selectUserWithMaxFeedhits(jpa, null, offset, limit);
        
        if(installation == null) {
            System.err.println("Failed to find installation");
            return;
        }
        
        final List<String> userViewedCategories = this.selectUserViewedCategories(
                jpa, installation, offset, limit);
        
        final String selectedCategory = this.getMostFrequent(userViewedCategories);
        
        System.out.println("Selected category: " + selectedCategory);
        
        if(selectedCategory == null) {
            return;
        }
        
        String category = "newsminute-advert";
        category = selectedCategory;
        
        final List<Integer> userViewedAdverts = this.selectUserViewedFeedidsOfCategory(
                jpa, installation, category, offset, limit);
  
        System.out.println("Selected " + userViewedAdverts.size());
        
        for(Integer feedid : userViewedAdverts) {
            System.out.println(feedid);
        }
    }
    
    public List<Integer> selectUserViewedFeedidsOfCategory(
            JpaObjectFactory jpa, Installation installation, String category, int offset, int limit) {
        
        final Select<Integer> select = jpa.getDaoForSelect(Integer.class);
        
        final List<Integer> result = select
                .distinct(true)
                .from(Feed.class)
                .select(Feed_.feedid)
                .where(Feed_.categories, Criteria.LIKE, category)
                .join(Feed.class, Feed_.feedhitList, Feedhit.class)
                .and().where(Feedhit.class, Feedhit_.installationid, installation)
                .getResultsAndClose(offset, limit);
        
        System.out.println("Category: " + category + ", user: " + installation + ", viewed feedids: " + result);
        
        return result;
    }
    
    public String getMostFrequent(List<String> list) {
        
        final Map<String, Integer> countMap = new HashMap<>();
        
        String mostFrequent = null;
        Integer maxCount = 0;
        
        for(String cat : list) {
            if(cat == null) {
                continue;
            }
            Integer count = countMap.get(cat);
            final Integer update = count == null ? 0 : ++count;
            countMap.put(cat, update);
            if(count == null || update > maxCount) {
                mostFrequent = cat;
                maxCount = update;
            }
        }
        
        return mostFrequent;
    }
    
    public List<String> selectUserViewedCategories(
            JpaObjectFactory jpa, Installation installation, int offset, int limit) {
        
        final Select<String> selectUserViewedCategories = jpa.getDaoForSelect(String.class);
        
        final List<String> userViewedCategories = selectUserViewedCategories
                .distinct(true)
                .from(Feed.class)
                .select(Feed_.categories)
                .join(Feed.class, Feed_.feedhitList, Feedhit.class)
                .and().where(Feedhit.class, Feedhit_.installationid, installation)
                .getResultsAndClose(offset, limit);
        
        System.out.println("For user: " + installation + ", viewed categories: " + userViewedCategories);
        
        return userViewedCategories;
    }
    
    public Integer selectMostRecentFeedhit(
            JpaObjectFactory jpa, Installation outputIfNone) {

        final Select<Integer> select = jpa.getDaoForSelect(Integer.class);
        
        final Integer mostRecentFeedhit = select
                .from(Feedhit.class)
                .max(Feedhit.class, Feedhit_.feedhitid)
                .getSingleResultAndClose();
        
        System.out.println("Most recent feedhit: " + mostRecentFeedhit);
        
        return mostRecentFeedhit;
    }

    public Installation selectUserWithMaxFeedhits(
            JpaObjectFactory jpa, Installation outputIfNone, int offset, int limit) {

        final Select<Installation> selectUsers = jpa.getDaoForSelect(Installation.class);
        
        final List<Installation> instList = selectUsers
                .descOrder(Installation_.installationid)
                .getResultsAndClose(offset, limit);
        
        Installation installation = null;
        
        int hitCount = 0;
        for(Installation inst : instList) {
            
            if(installation == null) {
                installation = inst;
                hitCount = inst.getFeedhitList() == null ? 0 : inst.getFeedhitList().size();
            }else{
                int n = inst.getFeedhitList() == null ? 0 : inst.getFeedhitList().size();
                if(n > hitCount) {
                    installation = inst;
                    hitCount = n;
                }
            }
        }
        
        System.out.println("Found user with max feedhits: " + installation);
        
        return installation == null ? outputIfNone : installation;
    }
    
    public void selectAndPrintInstallation(JpaObjectFactory jpa) {
        
        Installation installation = this.getInstallation(jpa, 
                "installationkey", "abdb33ee-a09e-4d7d-b861-311ee7061325");
        
        if(installation == null) {
            System.err.println("Failed to find installation");
            return;
        }else{
            System.out.println("ID:"+installation.getInstallationid()+
                    ", name:" + installation.getScreenname() +
                    ", location:" + installation.getLocationid() +
                    ", key:" + installation.getInstallationkey() +
                    "\nDates. First install:" + installation.getFirstinstallationdate() +
                    ", first subscription:" + installation.getFirstsubscriptiondate() +
                    ", last install:" + installation.getLastinstallationdate());
        }
    }

    public Installation getInstallation(JpaObjectFactory jpa, String key, Object val) {
        final Installation result =        
                jpa.getDaoForSelect(Installation.class)
                .from(Installation.class)
                        .where(key, val)
                        .getSingleResultAndClose();
        return result;
    }
    
    public List<Installation> getInstallationList(JpaObjectFactory jpa, String key, Object val, int offset, int limit) {
        final List<Installation> resultList =        
                jpa.getDaoForSelect(Installation.class)
                .from(Installation.class)
                        .where(key, val)
                        .getResultsAndClose(offset, limit);
        return resultList;
    }
}
