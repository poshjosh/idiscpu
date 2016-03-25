package com.idisc.pu;

import com.bc.jpa.ControllerFactory;
import com.bc.jpa.fk.AbstractEnumReferences;
import java.util.ArrayList;


/**
 * @(#)References.java   30-Nov-2014 01:48:59
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 * @param <E>
 */
public abstract class References<E> extends AbstractEnumReferences<E> {
    
    public static enum sitetype{rss, web, timeline, trend}
    public static enum gender{Male, Female, Other}
    public static enum country{}
    public static enum howdidyoufindus{Throughafriendorcolleague, Fromtheweb,
    Magazinesorotherprintmedia, Tvorotherelectronicmedia, Throughouragent}
    public static enum emailstatus{unverified,verified,bounced,disabled_or_discontinued,
    unable_to_relay,does_not_exist,could_not_be_delivered_to,black_listed,
    verification_attempted_but_status_unknown,user_opted_out_of_mailinglist,
    registered_user,automated_system_email,restricted,invalid_format}
    
    public References() { }
    
    @Override
    protected abstract ControllerFactory getControllerFactory();

    // This should not be static as it is a compilation of this class' enum classes
    // The key word being [this] class'
    //
    private Class [] enumTypes;
    @Override
    public Class [] getEnumTypes() {
        if(enumTypes == null) {
            enumTypes = this.initEnumTypes();
        }
        return enumTypes;
    }
    private Class [] initEnumTypes() {
        Class [] classes = this.getClass().getClasses();
        ArrayList<Class> list = new ArrayList<Class>();
        for(Class aClass:classes) {
            if(aClass.isEnum()) {
                list.add(aClass);
            }
        }
        return list.toArray(new Class[0]);
    }
}
