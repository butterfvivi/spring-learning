package org.vivi.framework.iexcelsimple.toolkit.inter;

public interface IAssert {

    /**
     * 给予一个默认异常信息，如果用户实现该接口，优先使用用户的，一般来说是与定义的全局捕获异常类结合
     * @param msg  异常信息内容
     * @author mashuai
     */
    default void throwMsg(String msg){
        throw new RuntimeException(msg);
    }

    /**
     * 内部抛出的异常，如果需要返回给前台，可以重写抛出方式，不然默认把错误抛在控制台
     *  @param msg  异常信息内容
     * @author mashuai
     */
    default void throwInnerMsg(String msg){
        throw new RuntimeException(msg);
    }
}
