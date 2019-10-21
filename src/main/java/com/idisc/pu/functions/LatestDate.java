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

package com.idisc.pu.functions;

import com.idisc.pu.entities.Feed;
import java.io.Serializable;
import java.util.Date;
import java.util.function.BiFunction;

/**
 * @author Chinomso Bassey Ikwuagwu on Jan 21, 2019 6:06:54 PM
 */
public class LatestDate implements BiFunction<Feed, Date, Date>, Serializable {

    @Override
    public Date apply(Feed feed, Date resultIfNone) {
        Date latest = null;
        final Date [] dates = {feed.getTimemodified(), feed.getFeeddate(), feed.getDatecreated()};
        for(Date date : dates) {
            if(latest == null) {
                latest = date;
            }else{
                if(date != null) {
                    latest = date.after(latest) ? date : latest;
                }
            }
        }
        return latest == null ? resultIfNone : latest;
    }
}
