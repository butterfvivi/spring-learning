package org.vivi.framework.fuction;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * 此接口中的抽象方法则是accept，传入一个T参数，执行自定义逻辑将它消费掉，没有返回值
 */
public class ConsumerTest {

    public static void main(String[] args) {
        Consumer<List<String>> consumer = v -> v.forEach(System.out::println);

        testConsumer(Arrays.asList("vivi01","vivi02","vivi03"),consumer);
        testConsumer(Arrays.asList("test01","test02","test03"),consumer);
    }

    public static void testConsumer(List<String> list,Consumer<List<String>> consumer){
        consumer.accept(list);
    }
}
