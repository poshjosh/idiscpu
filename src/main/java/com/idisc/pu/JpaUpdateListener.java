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

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

/**
 * @author Chinomso Bassey Ikwuagwu on Sep 10, 2018 6:49:08 PM
 */
public class JpaUpdateListener implements Serializable {

    private transient static final Logger LOG = Logger.getLogger(JpaUpdateListener.class.getName());

    private static JpaConnectionLeakCounter connectionLeakCounter;
    
    public JpaUpdateListener() { }

    public static JpaConnectionLeakCounter getConnectionLeakCounter() {
        return connectionLeakCounter;
    }

    public static void setConnectionLeakCounter(JpaConnectionLeakCounter connectionLeakCounter) {
        JpaUpdateListener.connectionLeakCounter = connectionLeakCounter;
    }
    
    @PrePersist public void onPrePersist(Object o) {
        this.logLeakedConnections(Level.FINER, "Before persist", o);
    }
    @PostPersist public void onPostPersist(Object o) {
        this.logLeakedConnections(Level.FINE, "After persist", o);
    }
    @PostLoad public void onPostLoad(Object o) { 
        this.logLeakedConnections(Level.FINER, "After load", o);
    }
    @PreUpdate public void onPreUpdate(Object o) {
        this.logLeakedConnections(Level.FINER, "Before update", o);
    }
    @PostUpdate public void onPostUpdate(Object o) {
        this.logLeakedConnections(Level.FINE, "After update", o);
    }
    @PreRemove public void onPreRemove(Object o) {
        this.logLeakedConnections(Level.FINER, "Before remove", o);
    }
    @PostRemove public void onPostRemove(Object o) {
        this.logLeakedConnections(Level.FINE, "After remove", o);
    }
    
    public void logLeakedConnections(Level level, String suffix, Object o) {
        if(connectionLeakCounter != null) {
            LOG.log(level, () -> {
                    if(connectionLeakCounter != null) {
                        return String.valueOf(connectionLeakCounter.countLeaks()) + 
                                " connections leaked. " + suffix + ": " + o;
                    }else{
                        return "";
                    }
            });
        }
    }
}
