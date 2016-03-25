package com.idisc.pu;

import com.bc.jpa.ControllerFactory;
import com.bc.jpa.DefaultControllerFactory;
import com.bc.jpa.ParametersFormatter;
import com.bc.jpa.PersistenceMetaData;
import com.bc.jpa.fk.EnumReferences;
import java.io.File;
import java.io.IOException;
import java.net.URI;











public class IdiscControllerFactory
  extends DefaultControllerFactory
{
  public IdiscControllerFactory()
    throws IOException
  {
    init();
  }
  
  public IdiscControllerFactory(String persistenceFile, ParametersFormatter paramFmt) throws IOException {
    super(persistenceFile, paramFmt);
    init();
  }
  
  public IdiscControllerFactory(URI persistenceFile, ParametersFormatter paramFmt) throws IOException {
    super(persistenceFile, paramFmt);
    init();
  }
  
  public IdiscControllerFactory(File persistenceFile, ParametersFormatter paramFmt) throws IOException {
    super(persistenceFile, paramFmt);
    init();
  }
  
  public IdiscControllerFactory(PersistenceMetaData metaData, ParametersFormatter paramFmt) {
    super(metaData, paramFmt);
    init();
  }
  

  private void init()
  {
    EnumReferences refs = getEnumReferences();
  }
  
  @Override
  public boolean acceptPersistenceFile(URI uri)
  {
    return uri.toString().contains("idisc");
  }
  
  @Override
  public EnumReferences getEnumReferences()
  {
    return new References()
    {
      @Override
      protected ControllerFactory getControllerFactory() {
        return IdiscControllerFactory.this;
      }
    };
  }
}
