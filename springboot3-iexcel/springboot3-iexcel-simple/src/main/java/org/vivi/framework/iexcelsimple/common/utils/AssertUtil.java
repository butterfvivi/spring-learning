package org.vivi.framework.iexcelsimple.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.vivi.framework.iexcelsimple.toolkit.inter.IAssert;

/**
 * 断言, 简化判空操作。
 */
@Slf4j
public class AssertUtil {

    //缓存异常类名称
    private static String beanNameCache = "";
    //缓存异常接口实现类
    private static IAssert iMsAssert = null;
    public static final String MSG = "参数值缺失，请联系管理员";

    /**
     * 检查所有类型是否为null，
     * 检查String！="",List、Map长度！=0，Boolean！=false
     *
     * @param o   任意类型的参数
     * @param msg 异常信息
     **/
    public static void objIsNull(Object o, String msg) {
        check(o, msg);
    }

    /**
     * 如果不是true，抛出异常
     * **/
    public static void isNotTrue(Boolean o, String msg) {
        if (!o) throwInnerException(msg);
    }

    /**
     * 检验多个参数值是否为空。如果存在一个为空，则提示异常
     **/
    public static void objIsNulls(Object... o) {
        for (Object o1 : o) {
            check(o1, MSG);
        }
    }

    private static void check(Object o, String msg) {
        //传入对象为null抛异常
        if (ConvertDataUtils.checkObjValIsEmpty(o)) {
            throwException(msg);
        }
    }

    /**
     * 一般来说项目使用全局捕获异常
     *
     * @param msg
     * @return
     * @author mashuai
     */
    public static void throwException(String msg) {
        throwMsgType(msg, "self");
    }

    /**
     * 内部异常，不对前台提示，只抛在控制台
     *
     * @param msg
     * @return
     * @author mashuai
     */
    public static void throwInnerException(String msg) {
        throwMsgType(msg, "inner");
    }

    private static void throwMsgType(String msg, String type) {
        log.info("异常信息：" + msg);
        if (EmptyUtils.isEmpty(beanNameCache)) {
            String[] beanNames = IocUtil.getBeanNames(IAssert.class);
            beanNameCache = beanNames[0];
        }
        if (iMsAssert == null) {
            iMsAssert = (IAssert) IocUtil.getBean(beanNameCache, IAssert.class);
        }
        if ("inner".equals(type)) {
            iMsAssert.throwInnerMsg(msg);
        } else {
            iMsAssert.throwMsg(msg);
        }

    }
}
