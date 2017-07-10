package com.gdzie.znajde.server.type;
import java.lang.annotation.Annotation;

import com.sun.jna.platform.win32.jnacom.IID;;

public class DynamicIID implements IID {
    private String value;
 
    public DynamicIID(String value) {
        this.value = value;
    }
 
    public String value() {
        return value;
    }
 
    public Class<? extends Annotation> annotationType() {
        return DynamicIID.class;
    }
}
