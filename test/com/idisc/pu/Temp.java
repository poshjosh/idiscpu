package com.idisc.pu;

import com.idisc.pu.entities.Feed;
import com.bc.jpa.ControllerFactory;
import com.bc.jpa.EntityController;
import java.util.List;


/**
 * @(#)Temp.java   29-Nov-2014 10:18:29
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
public class Temp {

    public static void main(String [] args) {
        
        try{
            
            ControllerFactory factory = new IdiscControllerFactory();
            
            EntityController<Feed, ?> ec = factory.getEntityController(Feed.class);
            
            List<Feed> feeds = ec.find(10, 0);
            
            for(Feed feed:feeds) {
System.out.println(ec.toMap(feed, false));                
            }
            
        }catch(Throwable t) {
            
            t.printStackTrace();
        }
    }
}
