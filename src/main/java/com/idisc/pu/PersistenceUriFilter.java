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

import com.bc.jpa.util.PersistenceURISelector;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Sep 26, 2018 9:26:15 AM
 */
public class PersistenceUriFilter implements PersistenceURISelector.URIFilter {

    private transient static final Logger LOG = Logger.getLogger(PersistenceUriFilter.class.getName());

    @Override
    public boolean accept(URI uri) {
        
        final String uriStr = uri.toString();
        
        boolean accepted = uriStr.contains("idisc");
        
        if(LOG.isLoggable(false ? Level.WARNING : Level.FINE)) {        
            LOG.log(false ? Level.WARNING : Level.FINE, 
            "Accepted: {0}, persistence config: {1}", new Object[]{accepted, uriStr});
        }
        
        return accepted;
    }
}
