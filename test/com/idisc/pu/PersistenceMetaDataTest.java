package com.idisc.pu;

import com.idisc.pu.entities.Feed;
import com.bc.jpa.ControllerFactory;
import com.idisc.pu.entities.Emailstatus;
import java.io.IOException;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.bc.jpa.PersistenceMetaData;


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
    
    private final ControllerFactory factory;
    
    private final PersistenceMetaData metaData;
    
    public PersistenceMetaDataTest() throws IOException { 
        this.factory = new IdiscControllerFactory();
        this.metaData = factory.getMetaData();
        factory.getEntityManager(Feed.class);
        factory.getEntityManager(Emailstatus.class);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {  }

    @AfterClass
    public static void tearDownClass() throws Exception { }
    
    @Test
    public void testGetReference() throws Exception {
long tB4 = System.currentTimeMillis();
long mB4 = Runtime.getRuntime().freeMemory();

        String [] puNames = metaData.getPersistenceUnitNames();
        
        for(String puName:puNames) {
            
            Class [] classes = metaData.getEntityClasses(puName);
            
            for(Class aClass:classes) {
System.out.println("Class: "+aClass.getName());                
                this.testGetReference(aClass, "gender", (short)2);
                this.testGetReference(aClass, "userstatus", (short)4);
//                this.testGetReference(aClass, "howdidyoufindus", (short)5);
            }
        }
        
System.out.println("TOTAL, Time: "+(System.currentTimeMillis()-tB4)+", Memory: "+(mB4-Runtime.getRuntime().freeMemory()));
    }

    private void testGetReference(Class aClass, String col, Object val) throws Exception {
        
        Object a = this.getReferenceA(aClass, col, val);
        Object b = this.getReferenceB(aClass, col, val);
        
System.out.println(metaData.getTableName(aClass)+"."+col+"="+val);        
System.out.println("getReferenceA: "+a+",   getReferenceB: "+b);
System.out.println();
    }

    private Object getReferenceA(Class aClass, String col, Object val) 
            throws IOException {
        
        if(val == null) {
            return null;
        }

        Class [] refTypes = metaData.getReferenceClasses(aClass);
        
        if(refTypes == null || refTypes.length == 0) {
            return null;
        }

        Map<String, String> references = metaData.getReferences(aClass);    
        
        final Class valueType = val.getClass();
        
        Object ref = null;
        
        for(Class refType:refTypes) {
            
            if(refType == null) {
                continue;
            }
            
            if(valueType.equals(refType)) {
                
                break;
            }
  
            String crossRefColumn = metaData.getReferenceColumn(refType, aClass);
            
            if(crossRefColumn != null && crossRefColumn.equals(references.get(col))) {

                ref = factory.getEntityManager(refType).getReference(refType, val);

                if(ref == null) {
                    throw new NullPointerException();
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

        Class refType = metaData.getReferenceClass(aClass, col);
        
        if(refType == null) {
            return null;
        }
        
        if(refType.equals(val.getClass())) {
            return null;
        }
        
        String crossRefColumn = metaData.getReferenceColumn(refType, aClass);

        Object ref;
        
        if(crossRefColumn != null) {

            ref = factory.getEntityManager(refType).getReference(refType, val);

            if(ref == null) {
                throw new NullPointerException();
            }

        }else{

            ref = null;
        }
        
        return ref;
    }
    
    /**
     * Test of getEntityClass method, of class PersistenceMetaData.
     */
//    @Test
    public void testAll() throws Exception {
        
long tB4 = System.currentTimeMillis();
long mB4 = Runtime.getRuntime().freeMemory();

        String [] puNames = metaData.getPersistenceUnitNames();
        
        for(String puName:puNames) {
            
            Class [] classes = metaData.getEntityClasses(puName);
            
            for(Class aClass:classes) {
                
                this.test(metaData, aClass);
            }
        }
        
System.out.println("TOTAL, Time: "+(System.currentTimeMillis()-tB4)+", Memory: "+(mB4-Runtime.getRuntime().freeMemory()));
    }
    
    private void test(PersistenceMetaData metaData, Class aClass) throws IOException {

        try{

long tB4 = System.currentTimeMillis();
long mB4 = Runtime.getRuntime().freeMemory();

            String puName = metaData.getPersistenceUnitName(aClass);
            
            String databaseName = metaData.getDatabaseName(aClass);
            
            String tableName = metaData.getTableName(aClass);

            String idColumn = metaData.getIdColumnName(aClass);

log("Class:"+aClass+", persistence unit:"+puName+", database:"+databaseName+", table:"+tableName+", idColumn:"+idColumn);            
            Class cls = metaData.getEntityClass(databaseName, tableName);

System.out.println("Time: "+(System.currentTimeMillis()-tB4)+", Memory: "+(mB4-Runtime.getRuntime().freeMemory()));
            
this.checkEquality(aClass, "Input class", cls, "Class from metaData");            
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
