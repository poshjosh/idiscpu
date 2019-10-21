package com.idisc.pu;

import com.bc.jpa.dao.util.EntityReference;
import java.io.IOException;
import java.util.Map;
import org.junit.Test;
import com.bc.jpa.context.PersistenceContext;
import com.bc.jpa.context.PersistenceUnitContext;
import com.bc.jpa.metadata.PersistenceMetaData;
import com.bc.jpa.metadata.PersistenceUnitMetaData;
import com.bc.node.NodeFormat;
import com.idisc.pu.entities.Applaunchlog;
import java.util.Arrays;
import java.util.Set;
import javax.persistence.EntityManager;


/**
 * @(#)PersistenceMetaDataTest.java   29-Nov-2014 07:00:15
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
public class PersistenceMetaDataTest extends TestStub {
    
    private final PersistenceContext jpaContext;
    
    private final PersistenceUnitContext puContext;
    
    private final PersistenceMetaData metaData;
    
    public PersistenceMetaDataTest() throws IOException { 
        this.jpaContext = this.getPersistenceContextDevMode();
        this.puContext = this.jpaContext.getContext(com.idisc.pu.PersistenceNames.PERSISTENCE_UNIT_NAME);
        this.metaData = jpaContext.getMetaData();
    }

    @Test
    public void testGetTableName() throws Exception {
        
        Set<String> puNames = metaData.getPersistenceUnitNames();
        System.out.println("Persistence units: " + puNames);
        
        final PersistenceUnitMetaData puMeta = metaData.getMetaData(com.idisc.pu.PersistenceNames.PERSISTENCE_UNIT_NAME);
        
        System.out.println(new NodeFormat().format(puMeta));

        final Class entityClass = Applaunchlog.class;
        final String table = puMeta.getTableName(entityClass);
        System.out.println("Entity class: " + entityClass.getSimpleName() + ", table: " + table);
        
        final Class etityClass = puMeta.getEntityClass(table);
        System.out.println("Table: " + table + ", entity class: " + entityClass.getSimpleName());
    }
    
    @Test
    public void testGetReference() throws Exception {
long tB4 = System.currentTimeMillis();
long mb4 = com.bc.util.Util.availableMemory();

        Set<String> puNames = metaData.getPersistenceUnitNames();
        
        final Class entityClass = Applaunchlog.class;
        final String table = metaData.getMetaData(com.idisc.pu.PersistenceNames.PERSISTENCE_UNIT_NAME).getTableName(entityClass);
        System.out.println("Entity class: " + entityClass.getSimpleName() + ", table: " + table);
        
        final Class etityClass = metaData.getMetaData(com.idisc.pu.PersistenceNames.PERSISTENCE_UNIT_NAME).getEntityClass(table);
        System.out.println("Table: " + table + ", entity class: " + entityClass.getSimpleName());
        
        for(String puName:puNames) {
            
            Set<Class> classes = metaData.getEntityClasses(puName);
            
            for(Class aClass:classes) {
System.out.println("Class: "+aClass.getName());   
                this.testGetReference(aClass, "gender", (short)1);
                this.testGetReference(aClass, "userstatus", (short)1);
                this.testGetReference(aClass, "howdidyoufindus", (short)1);
                this.testGetReference(aClass, "country", (short)1);
                this.testGetReference(aClass, "emailstatus", (short)1);
                this.testGetReference(aClass, "sitetype", (short)1);
                this.testGetReference(aClass, "timezone", (short)1);
            }
        }
        
System.out.println("TOTAL, Time: "+(System.currentTimeMillis()-tB4)+", Memory: "+(mb4-com.bc.util.Util.usedMemory(mb4)));
    }

    private void testGetReference(Class aClass, String col, Object val) throws Exception {
        
        Object a = this.getReferenceA(aClass, col, val);
        Object b = this.getReferenceB(aClass, col, val);
        
        final String tableName = this.puContext.getMetaData().getTableName(aClass);
System.out.println(tableName+"."+col+"="+val); 
        if(a != null || b != null) {
System.out.println("getReferenceA: "+a+",   getReferenceB: "+b);
System.out.println();
        }
    }

    private Object getReferenceA(Class aClass, String col, Object val) 
            throws IOException {
        
        if(val == null) {
            return null;
        }
        
        final EntityReference entityReference = puContext.getEntityReference();

        Class [] refTypes = entityReference.getReferenceClasses(aClass);
        System.out.println(aClass.getSimpleName() + ", ref classes: " +
                (refTypes==null?null:Arrays.toString(refTypes)));
        
        if(refTypes == null || refTypes.length == 0) {
            return null;
        }

        Map<String, String> references = entityReference.getReferences(aClass);    
        System.out.println(aClass.getSimpleName() + ", refs: " + references);
        
        final Class valueType = val.getClass();
        
        Object ref = null;
        
        for(Class refType:refTypes) {
            
            if(refType == null) {
                continue;
            }
            
            if(valueType.equals(refType)) {
                
                break;
            }
  
            String crossRefColumn = entityReference.getReferenceColumn(refType, aClass);
            System.out.println("Ref type: " + refType.getSimpleName() +
                    ", refing type: " + aClass.getSimpleName() + 
                    ", cross ref column: " + crossRefColumn);
            
            if(crossRefColumn != null && crossRefColumn.equals(references.get(col))) {

                try{
                    final EntityManager em = puContext.getEntityManager();
                    try{
                        ref = em.getReference(refType, val);
                    }finally{
                        if(em.isOpen()) {
                            em.close();
                        }
                    }

                    if(ref == null) {
                        throw new NullPointerException();
                    }
                }catch(javax.persistence.EntityNotFoundException e) {
                    System.err.println(e);
                }
                
                break;
                
            }else{

            }
        }
        
        return ref;
    }
    
    private Object getReferenceB(Class aClass, String col, Object val) 
            throws IOException {
        
        if(val == null) {
            return null;
        }
        
        final EntityReference entityReference = this.puContext.getEntityReference();

        Class refType = entityReference.getReferenceClass(aClass, col);
        
        if(refType == null) {
            return null;
        }
        
        if(refType.equals(val.getClass())) {
            return null;
        }
        
        String crossRefColumn = entityReference.getReferenceColumn(refType, aClass);

        Object ref;
        
        if(crossRefColumn != null) {

            try{
                final EntityManager em = puContext.getEntityManager();
                try{
                    ref = em.getReference(refType, val);
                }finally{
                    if(em.isOpen()) {
                        em.close();
                    }
                }
                if(ref == null) {
                    throw new NullPointerException();
                }
            }catch(javax.persistence.EntityNotFoundException e) {
                ref = null;
                System.err.println(e);
            }

        }else{

            ref = null;
        }
        
        return ref;
    }
    
    /**
     * Test of getEntityClass method, of class JpaMetaData.
     */
//    @Test
    public void testAll() throws Exception {
        
long tB4 = System.currentTimeMillis();
long mb4 = com.bc.util.Util.availableMemory();

        Set<String> puNames = metaData.getPersistenceUnitNames();
        
        for(String puName : puNames) {
            
            Set<Class> classes = metaData.getEntityClasses(puName);
            
            for(Class aClass:classes) {
                
                this.test(metaData.getMetaData(puName), aClass);
            }
        }
        
System.out.println("TOTAL, Time: "+(System.currentTimeMillis()-tB4)+", Memory: "+(mb4-com.bc.util.Util.usedMemory(mb4)));
    }
    
    private void test(PersistenceUnitMetaData metaData, Class aClass) throws IOException {

        try{

long tB4 = System.currentTimeMillis();
long mb4 = com.bc.util.Util.availableMemory();

            final String puName = metaData.getName();
            
            final String databaseName = metaData.getDatabaseName(aClass);
            
            final String tableName = metaData.getTableName(aClass);

            final String idColumn = metaData.getIdColumnName(aClass);

log("Class:"+aClass+", persistence-units:"+puName+", database:"+databaseName+", table:"+tableName+", idColumn:"+idColumn);            
            Class cls = metaData.getEntityClass(databaseName, null, tableName);

System.out.println("Time: "+(System.currentTimeMillis()-tB4)+", Memory: "+(mb4-com.bc.util.Util.usedMemory(mb4)));
            
this.checkEquality(aClass, "Input class", cls, "Class from metaData");            
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
