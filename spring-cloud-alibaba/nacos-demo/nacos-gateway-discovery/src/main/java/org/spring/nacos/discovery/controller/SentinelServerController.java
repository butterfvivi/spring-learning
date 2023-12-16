package org.spring.nacos.discovery.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreaker;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.EventObserverRegistry;
import com.alibaba.csp.sentinel.util.TimeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class SentinelServerController {


    @RequestMapping(value = "/test")
    @ResponseBody
    public String test() {
        return "Sentinel server";
    }



    @RequestMapping(value = "/resource1")
    @ResponseBody
    public String resource1() {
        return resource1bySphU();
    }

    /**
     * 通过 SphU 手动定义资源
     * @return
     */
    public String resource1bySphU() {
        Entry entry = null;
        try {
            entry = SphU.entry("resource1bySphU");
            //您的业务逻辑 - 开始
            return "resource1";
            //您的业务逻辑 - 结束
        } catch (BlockException e1) {
            //流控逻辑处理 - 开始
            return "resource1 limit";
            //流控逻辑处理 - 结束
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }

    //
    @RequestMapping(value = "/resource2")
    //blockHandler 限流后走的方法
    @SentinelResource(value="resource2byAnnotation",
            blockHandler = "resource2Limit",fallback = "resource2Fallback")
    @ResponseBody
    public String resource2()  {
        return "resource2";
    }

    public String resource2Limit(BlockException exception){
        return "您点击太快了，稍后重试！";
    }

    @RequestMapping(value = "/resource3/{id}")
    //抛出异常时，提供 fallback 处理逻辑
    //处于熔断开启状态时，原来的主逻辑则暂时不可用，会走fallback的逻辑。
    //在经过一段时间（熔断时长）后，熔断器会进入探测恢复状态（HALF-OPEN），此时 Sentinel 会允许一个请求对原来的主业务逻辑进行调用
    //若请求调用成功，则熔断器进入熔断关闭状态（CLOSED ），服务原来的主业务逻辑恢复，否则重新进入熔断开启状态（OPEN）
    @SentinelResource(value="resource3byAnnotation",fallback = "resource3Fallback")
    @ResponseBody
    public String resource3(@PathVariable("id") int id)  {
        monitor();
        System.out.println("主逻辑");
        //这里模拟服务报错
        int defaultId = 5;
        if(id < defaultId){
            throw new RuntimeException ("服务异常");
        }
        return "resource3";
    }

    //注意fallback返回值类型必须与原函数一致；方法参数列表需要和原函数一致，或者可以额外多一个 Throwable 类型的参数用于接收对应的异常；
    //fallback 函数默认需要和原方法在同一个类中，若希望使用其他类的函数，则可以指定 fallbackClass 为对应的类的 Class 对象，注意对应的函数必需为 static 函数，否则无法解析
    public String resource3Fallback (int id){
        return "服务出错，请您先访问这里！";
    }

    /**
     * 自定义事件监听器，监听熔断器状态转换
     */
    public void monitor() {
        EventObserverRegistry.getInstance().addStateChangeObserver("logging",
                (prevState, newState, rule, snapshotValue) -> {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if (newState == CircuitBreaker.State.OPEN) {
                        // 变换至 OPEN state 时会携带触发时的值
                        System.err.println(String.format("%s -> OPEN at %s, 发送请求次数=%.2f", prevState.name(),
                                format.format(new Date(TimeUtil.currentTimeMillis())), snapshotValue));
                    } else {
                        System.err.println(String.format("%s -> %s at %s", prevState.name(), newState.name(),
                                format.format(new Date(TimeUtil.currentTimeMillis()))));
                    }
                });
    }


}






