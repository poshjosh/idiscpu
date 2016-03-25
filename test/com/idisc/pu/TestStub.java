package com.idisc.pu;

import com.idisc.pu.entities.Feed;
import com.bc.jpa.ControllerFactory;
import com.bc.jpa.EntityController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @(#)TestStub.java   29-Nov-2014 07:04:11
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
public class TestStub {
    
    private ControllerFactory factory;
    
    public boolean isRemote() {
        return false;
    }
    
    public ControllerFactory getControllerFactory() {
        if(factory == null) {
            try{
                factory = new IdiscControllerFactory();
            }catch(IOException e) {
                throw new RuntimeException(e);
            }
        }
        return factory;
    }
    
    public EntityController getEntityController(Class aClass) {
        return this.getControllerFactory().getEntityController(aClass);
    }
    
    public Class getDefaultEntityClass() {
        return Feed.class;
    }
    
    private List f;
    public List getFound() {
        if(f == null) {
            f = this.getEntityController(this.getDefaultEntityClass()).find();
        }
        return f;
    }
    
    protected Map extractMap(Object entity) {
        Map m = getEntityController(getDefaultEntityClass()).toMap(entity);
        String idColumn = getEntityController(getDefaultEntityClass()).getIdColumnName();
        this.checkNull(m.get(idColumn), "Map extracted from entity does not contain idColumn: "+idColumn+", Map.keySet"+m.keySet());
        return m;
    }
    
    protected Object insertFromRandom() throws Exception {
        Object oldEntity = this.getRandomEntity();
        Map m = this.extractMap(oldEntity);
        Object newEntity = getEntityController(getDefaultEntityClass()).toEntity(m, true);
        getEntityController(getDefaultEntityClass()).setId(newEntity, null);
        getEntityController(getDefaultEntityClass()).create(newEntity);
        return newEntity;
    }

    protected void checkEntityEquality(Object o1, String name1, Object o2, String name2) {
        Map m1 = this.extractMap(o1);
        Map m2 = this.extractMap(o2);
        this.checkEquality(m1, name1, m2, name2);
    }
    
    protected void checkEquality(Object o1, String name1, Object o2, String name2) {
        if((o1 == null && o2 != null) || (o1 != null && !o1.equals(o2))) {
            StringBuilder msg = new StringBuilder();
            if(name1 != null) {
                msg.append(name1).append(": ");
            }
            msg.append(o1);
            msg.append(" NOT EQUALS TO ");
            if(name2 != null) {
                msg.append(name2).append(": ");
            }
            msg.append(o2);
            fail(msg);
        }
    }

    protected void checkTrue(boolean b, Object msg) {
        if(!b) {
            fail(msg);
        }
    }

    protected void checkNull(Object tgt, Object msg) {
        if(tgt == null) {
            fail(msg);
        }
    }
    
    protected void fail(Object msg) {
        if(msg != null) {
            throw new AssertionError(msg);
        }else{
            throw new AssertionError();
        }
    }
    
    protected Map getRandomOrderBy(Map m, int size) {
        if(size > m.size()) {
            size = m.size();
        }    
        Iterator iter = m.keySet().iterator();
        HashMap orderBy = new HashMap(size, 1.0f);
        for(int i=0; iter.hasNext(); i++) {
            Object col = iter.next();
            orderBy.put(col, "ASC");
            if(i == size-1) {
                break;
            }
        }
        return orderBy;
    }
    
    protected int getLimit(Collection collection) {
        int limit = 11;
        if(limit > collection.size()) {
            limit = collection.size();
        }
        return limit;
    }
    
    protected Object getRandomEntity() {
        final int size = this.getFound().size();
        int n = (int)(Math.random() * size);
        if(n > size-1) {
            n = size - 1;
        }
        return this.getFound().get(n);
    }

    protected List getRandomEntities(int count) {
        if(count > this.getFound().size()) {
            throw new IndexOutOfBoundsException();
        }
        if(count == this.getFound().size()) {
            return new ArrayList(this.getFound());
        }
        ArrayList list = new ArrayList(count);
        int i = 0;
        while(list.size() < count) {
            Object o = this.getRandomEntity();
            if(!list.contains(o)) {
                list.add(o);
            }
            if(++i == this.getFound().size()) {
                throw new UnsupportedOperationException();
            }
        }
        return list;
    }
    
    protected void log(Object msg) {
System.out.println(msg);        
    }
}
