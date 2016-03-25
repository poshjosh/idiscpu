package com.idisc.pu;

import com.bc.jpa.ControllerFactory;
import com.bc.jpa.DefaultControllerFactory;
import com.bc.jpa.ParametersFormatter;
import com.bc.jpa.PersistenceMetaData;
import com.bc.jpa.fk.EnumReferences;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * @(#)IdiscControllerFactory.java   22-Aug-2014 14:14:23
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
public class IdiscControllerFactory extends DefaultControllerFactory {
    
    public IdiscControllerFactory() throws IOException { 
        super();
        this.init();
    }

    public IdiscControllerFactory(String persistenceFile, ParametersFormatter paramFmt) throws IOException {
        super(persistenceFile, paramFmt);
        this.init();
    }
    
    public IdiscControllerFactory(URI persistenceFile, ParametersFormatter paramFmt) throws IOException { 
        super(persistenceFile, paramFmt);
        this.init();
    }

    public IdiscControllerFactory(File persistenceFile, ParametersFormatter paramFmt) throws IOException { 
        super(persistenceFile, paramFmt);
        this.init();
    }    
    
    public IdiscControllerFactory(PersistenceMetaData metaData, ParametersFormatter paramFmt) { 
        super(metaData, paramFmt);
        this.init();
    }
    
    private void init() {
        // Instantiating this automatically validates it and also pre-loads some pretty important stuff
        //
        EnumReferences refs = IdiscControllerFactory.this.getEnumReferences();
    }
    
    @Override
    public boolean acceptPersistenceFile(URI uri) {
        return uri.toString().contains("idisc");
    }

    @Override
    public EnumReferences getEnumReferences() {
        return new References() {
            @Override
            protected ControllerFactory getControllerFactory() {
                return IdiscControllerFactory.this;
            }
        };
    }
}
