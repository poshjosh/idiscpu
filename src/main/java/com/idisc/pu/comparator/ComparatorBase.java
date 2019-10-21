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

package com.idisc.pu.comparator;

import java.util.Date;

/**
 * @author Chinomso Bassey Ikwuagwu on Jan 21, 2019 5:19:53 PM
 */
public class ComparatorBase {

    private final boolean reverseOrder;
    
    public ComparatorBase(boolean reverseOrder) {
        this.reverseOrder = reverseOrder;
    }
    
    public final int compareInts(int x, int y) { 
        
        return this.reverseOrder ? Integer.compare(y, x) : Integer.compare(x, y);
    }
  
    public final int compareLongs(long x, long y) { 
        
        return this.reverseOrder ? Long.compare(y, x) : Long.compare(x, y);
    }

    public final int compareDates(Date date_a, Date date_b) { 
        if ((date_a == null) && (date_b == null)) {
            return 0;
        }    
        if (date_a == null) {
            return this.reverseOrder ? 1 : -1;
        }
        if (date_b == null) {
            return this.reverseOrder ? -1 : 1;
        }
        return this.reverseOrder ? date_b.compareTo(date_a) : date_a.compareTo(date_b);
    }
  
    public final boolean isReverseOrder() {
        return reverseOrder;
    }
}
