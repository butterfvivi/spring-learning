package org.vivi.framework.excel.configure.common.exceptions;

public class DuplicatedIndexException extends Exception {

    /**
     * 无参构造函数
     */
    public DuplicatedIndexException(){
        super();
    }

    /**
     * 用详细信息指定一个异常
     * @param message
     */
    public DuplicatedIndexException(String message){
        super(message);
    }

}
