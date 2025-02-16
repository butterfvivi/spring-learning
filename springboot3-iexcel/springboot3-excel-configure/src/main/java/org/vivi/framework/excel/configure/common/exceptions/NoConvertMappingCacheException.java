package org.vivi.framework.excel.configure.common.exceptions;

public class NoConvertMappingCacheException extends Exception {

    /**
     * 无参构造函数
     */
    public NoConvertMappingCacheException(){
        super();
    }

    /**
     * 用详细信息指定一个异常
     * @param message
     */
    public NoConvertMappingCacheException(String message){
        super(message);
    }

}
