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
import com.bc.jpa.dao.functions.GetColumnNames;
import com.bc.jpa.dao.util.EntityReference;
import com.bc.jpa.dao.util.EntityReferenceImpl;
import com.idisc.pu.entities.Comment;
import com.idisc.pu.entities.Comment_;
import java.time.LocalTime;
import java.util.List;
import org.junit.Test;

/**
 * @author Chinomso Bassey Ikwuagwu on Jan 11, 2019 2:12:09 PM
 */
public class EntityReferenceTest extends TestStub {

    @Test
    public void test() {
        
        final JpaObjectFactory jpa = this.createJpaObjectFactory();
        
        final EntityReference ref = new EntityReferenceImpl(jpa);
        
        final Number count;
        try(final Select<Number> select = jpa.getDaoForSelect(Number.class)) {
            count = select
                .from(Comment.class)
                .count(Comment_.commentid)
                .createQuery().getSingleResult();
        }
        System.out.println(LocalTime.now() + " Number of comments: " + count);
        
        if(count.longValue() > 0) {
        
            jpa.getDaoForSelect(Comment.class).where(Comment_.repliedto, Select.NE, null);
//            final List<Comment> found = jpa.getDaoForSelect(Comment.class)
//                    .where(Comment_.repliedto, Select.NE, null)
//                    .findAllAndClose(Comment.class, 0, 1);
            final List<Comment> found = jpa.getDaoForSelect(Comment.class).findAllAndClose(Comment.class, 0, 1);

            if(found.isEmpty()) {
                System.out.println(LocalTime.now() + " Nothing found");
            }else{

                final Comment comment = found.get(0);
                System.out.println(LocalTime.now() + " Found: " + comment);
                
                final Integer feedId = comment.getFeedid().getFeedid();
                final Integer instlId = comment.getInstallationid().getInstallationid();

                System.out.println(LocalTime.now() + " " + ref.getReferenceOptional(Comment.class, "feedid", feedId));
                System.out.println(LocalTime.now() + " " + ref.getReferenceOptional(Comment.class, "installationid", instlId));
//                System.out.println(LocalTime.now() + " " + ref.getReferenceOptional(Comment.class, "repliedto", commentId));
                
                final List<String> columnNames = new GetColumnNames(jpa.getEntityManagerFactory()).apply(Comment.class);
                System.out.println(LocalTime.now() + " Columns: " + columnNames);
            }
        }
    }
}
