package org.vivi.framework.ireport.demo.core.inter;

public interface IAssert {

    /**
     * give a default exception message, if the user implements the interface,
     * the user's message is preferred, generally combined with the globally defined exception class
     * @param msg
     */
    default void throwMsg(String msg){
        throw new RuntimeException(msg);
    }

    /**
     * Internal exception thrown, if you need to return to the front end
     * you can override the throwing method, otherwise the error is thrown on the console by default
     * @param msg
     */
    default void throwInnerMsg(String msg){
        throw new RuntimeException(msg);
    }
}
