package com.idisc.pu;

import com.bc.jpa.dao.util.EntityMemberAccess;
import com.idisc.pu.entities.Feed;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.bc.jpa.context.PersistenceContext;
import com.bc.jpa.context.PersistenceUnitContext;
import com.bc.jpa.dao.JpaObjectFactory;
import com.bc.jpa.functions.GetMapForEntity;
import com.bc.jpa.dao.sql.MySQLDateTimePatterns;
import com.bc.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Paths;
import java.util.logging.LogManager;


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
    static{
        final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try(InputStream in = cl.getResourceAsStream("META-INF/logging.properties")) {
            LogManager.getLogManager().readConfiguration(in);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private static final Map<URI, PersistenceContext> persistenceContextMap = new HashMap<>();
    
    public Map<String, String> getDefaultOutputParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("installationid", "2");
        parameters.put("installationkey", "abdb33ee-a09e-4d7d-b861-311ee7061325");
        parameters.put("screenname", "user_2");
        parameters.put("format", "application/json");
        parameters.put("versionCode", "58");
        parameters.put("tidy", "true");
        return parameters;
    }
    
    public boolean isRemote() {
        return false;
    }
    
    public PersistenceContext getPersistenceContextProductionMode() {
        final String path = this.getSrcDir() + "/main/resources/META-INF/persistence.xml";
        final PersistenceContext output = this.getPersistenceContext(Paths.get(path).toUri());
        return output;
    }
    
    public PersistenceContext getPersistenceContextDevMode() {
        final String path = this.getSrcDir() + "/test/resources/META-INF/persistence.xml";
        final PersistenceContext output = this.getPersistenceContext(Paths.get(path).toUri());
        return output;
    }

    public String getSrcDir() {
        final String userHome = System.getProperty("user.home");
        final String srcDir = userHome + "/Documents/NetBeansProjects/idiscpu/src";
        return srcDir;
    }
    
    public PersistenceUnitContext getPersistenceUnitContext() {
        return this.getPersistenceContext("META-INF/persistence.xml").getContext(PersistenceNames.PERSISTENCE_UNIT_NAME);
    }

    public PersistenceContext getPersistenceContext() {
        return this.getPersistenceContext("META-INF/persistence.xml");
    }
    
    public PersistenceContext getPersistenceContext(String resourceName) {
        return this.getPersistenceContext(this.getUri(resourceName));
    }
    
    public PersistenceContext getPersistenceContext(URI uri) {
        PersistenceContext jpaContext = persistenceContextMap.getOrDefault(uri, null);
        if(jpaContext == null) {
            jpaContext = new IdiscPersistenceContext(uri, new MySQLDateTimePatterns());
            persistenceContextMap.put(uri, jpaContext);
        }
        return jpaContext;
    }

    public JpaObjectFactory createJpaObjectFactory() {
        return this.createJpaObjectFactory(PersistenceNames.PERSISTENCE_UNIT_NAME);
    }
    
    public JpaObjectFactory createJpaObjectFactory(String persistenceUnit) {
//        try{
            final String jpaPropsLocation = com.bc.idiscjpaconfig.Resources.JPA_PROPERTIES;
            final JpaObjectFactory jpa = JpaObjectFactory.builder()
                    .persistenceUnitName(persistenceUnit)
                    .properties(jpaPropsLocation)
                    .sqlVendor(JpaObjectFactory.Builder.SQL_VENDOR_MYSQL)
                    .jpaVendor(JpaObjectFactory.Builder.JPA_VENDOR_ECLIPELINK)
                    .build();
            
//            final JpaObjectFactory jpa = new IdiscJpaObjectFactoryDevmode(persistenceUnit, new MySQLDateTimePatterns());
            return jpa;
//        }catch(IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public Class getDefaultEntityClass() {
        return Feed.class;
    }
    
    private List f;
    public List getFound() {
        if(f == null) {
            f = this.getPersistenceUnitContext().getDaoForSelect(this.getDefaultEntityClass()).findAllAndClose(this.getDefaultEntityClass());
        }
        return f;
    }
    
    protected Map extractMap(Object entity) {
        final PersistenceUnitContext jpa = this.getPersistenceUnitContext();
        final Map entityMap = new GetMapForEntity(true).apply(entity);
        final String idColumn = jpa.getMetaData().getIdColumnName(this.getDefaultEntityClass());
        this.checkNull(entityMap.get(idColumn), "Map extracted from entity does not contain idColumn: "+idColumn+", Map.keySet"+entityMap.keySet());
        return entityMap;
    }
    
    protected Object insertFromRandom() throws Exception {
        final Object oldEntity = this.getRandomEntity();
        final Map entityMap = this.extractMap(oldEntity);
        final Class entityClass = this.getDefaultEntityClass();
        final PersistenceUnitContext jpa = this.getPersistenceUnitContext();
        final EntityMemberAccess ema = jpa.getEntityMemberAccess(entityClass);
        final Object newEntity = ema.create(entityMap, true);
        ema.setId(newEntity, null);
        jpa.getDao().persistAndClose(newEntity);
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
    
    
    public void printStats(String prefix, long tb4, long mb4) {
        System.out.println(prefix);
        System.out.println("Consumed. time: " +
                (System.currentTimeMillis() - tb4) +
                ", memory: " + Util.usedMemory(mb4));
        this.printMemoryStats();
    }
    
    public void printMemoryStats() {
        System.out.println("Memory. free: " + Runtime.getRuntime().freeMemory() + 
                ", available: " + Util.availableMemory());
    }

    public void sleep(int timeoutSeconds) {
        for(int i=0; i<timeoutSeconds; i++) {
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void log(Object msg) {
System.out.println(msg);        
    }

    public URI getUri(String resourceName) {
        try{
            final URI uri = Thread.currentThread().getContextClassLoader()
                    .getResource(resourceName).toURI();
            System.out.println("Resource: " + resourceName + ", URI: " + uri);
            return uri;
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
/**
 *
 * 
    public URI getJpaUri(String resourceName) {
        try{
            final URI uri;
            if("META-INF/persistence.xml".equals(resourceName)) {
                uri = Thread.currentThread().getContextClassLoader()
                    .getResource(resourceName).toURI();
            }else{
                final Path path = Paths.get(System.getProperty("user.home"), "Documents", 
                        "NetBeansProjects", "idiscpu", "src", "test", "resources", resourceName);
                uri = path.toFile().toURI();
            }
            System.out.println("Persistence config URI: " + uri);
            return uri;
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
 * 
 * 
 */
}
