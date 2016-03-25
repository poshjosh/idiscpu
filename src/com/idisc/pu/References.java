package com.idisc.pu;

import com.bc.jpa.ControllerFactory;
import com.bc.jpa.fk.AbstractEnumReferences;
import java.util.ArrayList;

public abstract class References<E>
  extends AbstractEnumReferences<E>
{
  private Class[] enumTypes;
  @Override
  protected abstract ControllerFactory getControllerFactory();
  
  public static enum sitetype {
    rss,  web,  timeline,  trend;
    private sitetype() {} 
  } 
  public static enum gender { Male,  Female,  Other;
    private gender() {} 
  }
  public static enum country { ;
      private country() {} 
  } 
  public static enum howdidyoufindus { Throughafriendorcolleague,  Fromtheweb, 
    Magazinesorotherprintmedia,  Tvorotherelectronicmedia,  Throughouragent;
    private howdidyoufindus() {} 
  } 
  public static enum emailstatus { unverified,  verified,  bounced,  disabled_or_discontinued, 
    unable_to_relay,  does_not_exist,  could_not_be_delivered_to,  black_listed, 
    verification_attempted_but_status_unknown,  user_opted_out_of_mailinglist, 
    registered_user,  automated_system_email,  restricted,  invalid_format;
    private emailstatus() {}
  }

  @Override
  public Class[] getEnumTypes()
  {
    if (this.enumTypes == null) {
      this.enumTypes = initEnumTypes();
    }
    return this.enumTypes;
  }
  
  private Class[] initEnumTypes() { Class[] classes = getClass().getClasses();
    ArrayList<Class> list = new ArrayList();
    for (Class aClass : classes) {
      if (aClass.isEnum()) {
        list.add(aClass);
      }
    }
    return (Class[])list.toArray(new Class[0]);
  }
}
