package org.vivi.framework.iasyncexcel.common.utils;

public class ExceptionUtil {

    public static RuntimeException wrap2Runtime(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException(e);
    }

    public static Throwable getOriginal(Throwable e){
        Throwable ex = e.getCause();
        if(ex != null){
            return getOriginal(ex);
        }else{
            return e;
        }
    }
}
