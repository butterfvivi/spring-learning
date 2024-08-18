package org.vivi.framework.iasyncexcel.core.handler;

import org.vivi.framework.iasyncexcel.core.model.DataParam;
import org.vivi.framework.iasyncexcel.core.model.ExcelContext;

public interface Handler {

    /**
     * 整个生命周期中只会执行一次该方法，可以用于初始化处理类的一些全局属性
     * @param ctx
     */
    default void init(ExcelContext ctx, DataParam param) {
        //do some init operation
    }

    /** 整个生命周期完成后回调处理
     * @param ctx
     * @param param
     */
    default void callBack(ExcelContext ctx, DataParam param){
        //do something callBack
    }
}
