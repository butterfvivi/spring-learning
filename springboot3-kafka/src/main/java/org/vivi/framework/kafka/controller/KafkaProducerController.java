package org.vivi.framework.kafka.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.kafka.productor.KafkaProducerStart;

import java.util.Map;

@Slf4j
@RestController
public class KafkaProducerController {

    @Resource
    private KafkaProducerStart kafkaProducerStart;


    /**
     * 向指定主题(topic)发送消息：http://localhost:8080/kafka/sendMsg?topic=car-infos
     * <p>
     * 1、send(String topic, @Nullable V data)：向指定主题发送消息，如果 topic 不存在，则自动创建，
     * 但是创建的主题默认只有一个分区 - PartitionCount: 1、分区也没有副本 - ReplicationFactor: 1，1表示自身。
     * 2、send 方法默认是异步的，主线程会直接继续向后运行，想要获取发送结果是否成功，请添加回调方法 addCallback。
     * [WARN ][org.apache.kafka.common.utils.LogContext$KafkaLogger.warn(LogContext.java:241)]:[Producer clientId=producer-1] Connection to node -1 could not be established. Broker may not be available.
     * [ERROR][org.springframework.kafka.support.LoggingProducerListener.onError(LoggingProducerListener.java:76)]:Exception thrown when sending a message with key='xxx' and payload='xxx' to topic bgt.basic.agency.frame.topic:
     * 3、send().get() 可以同步阻塞主线程直到获取执行结果，或者执行超时抛出异常.
     * java.util.concurrent.ExecutionException: org.springframework.kafka.core.KafkaProducerException:
     * Failed to send; nested exception is org.apache.kafka.common.errors.TimeoutException: Failed to update metadata after 60000 ms.
     *
     * 主题名称，不存在时自动创建，默认1个分区，无副本。主题名称也可以通过配置文件配置，这里直接通过参数传入。
     * 待发送的消息，如：{"version":1,"text":"后日凌晨三点执行任务"}
     */
    @PostMapping("kafka/sendMsg")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> sendMessage(@RequestParam("topic") String topic,
                                           @RequestBody Map<String, Object> message) {
        return kafkaProducerStart.sendMessage(topic, message);
    }

    /**
     * 向默认主题(default-topic)发送消息：http://localhost:8080/kafka/sendMsgDefault
     * 默认主题由 spring.kafka.template.default-topic 选项进行配置
     * @param message ：待发送的消息，如：{"version":2,"text":"后日凌晨三点执行任务，不得有误"}
     */
    @PostMapping("kafka/sendMsgDefault")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> sendMsgDefault(@RequestBody Map<String, Object> message) {
        return kafkaProducerStart.sendMsgDefault(message);
    }

    /**
     * 异步回调写法 1
     * 发送信息，并添加异步回调方法，用于监控消息发送成功或者失败。发送成功可以记录日志，发送失败则应该有相应的措施，比如延期再发送等。
     * http://localhost:8080/kafka/sendMsgCallback?topic=car-infos
     * 1、addCallback 方法用于获取 send 发送的结果，成功或者失败，此时 send 方法不再阻塞线程。
     * @param topic   ：car-infos
     * @param message ：{"version":223,"text":"后日凌晨三点执行任务，不得有误"}
     */
    @PostMapping("kafka/sendMsgCallback")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> sendMessageCallback(@RequestParam("topic") String topic,
                                                   @RequestBody Map<String, Object> message) {
        return kafkaProducerStart.sendMessageCallback(topic, message);
    }

    /**
     * 异步回调写法 2
     * 发送信息，并添加异步回调方法，用于监控消息发送成功或者失败。发送成功可以记录日志，发送失败则应该有相应的措施，比如延期再发送等。
     * http://localhost:8080/kafka/sendMsgCallback2?topic=helloWorld
     * 1、addCallback 方法用于获取 send 发送的结果，成功或者失败，此时 send 方法不再阻塞线程，主线程会直接运行过去。
     *
     * @param topic   ：helloWorld
     * @param message ：{"version":223,"text":"后日凌晨三点执行任务，不得有误"}
     * @return
     */
    @PostMapping("kafka/sendMsgCallback2")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> sendMessageCallback2(@RequestParam("topic") String topic,
                                                    @RequestBody Map<String, Object> message) {
       return kafkaProducerStart.sendMessageCallback2(topic, message);
    }

    /**
     * 向指定主题(topic)发送消息：http://localhost:8080/kafka/sendMsgTransactional1?topic=car-infos
     * 与 springframework 框架的事务整合到一起，此时异常处理完全和平时一样.
     *
     * @param topic   ：主题名称，不存在时自动创建，默认1个分区，无副本。主题名称也可以通过配置文件配置，这里直接通过参数传入。
     * @param message ：待发送的消息，如：{"version":1,"text":"后日凌晨三点执行任务"}
     * @return
     */
    @PostMapping("kafka/sendMsgTransactional1")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> sendMessageTransactional1(@RequestParam("topic") String topic,
                                                         @RequestBody Map<String, Object> message) {
        return kafkaProducerStart.sendMessageTransactional1(topic, message);
    }

    /**
     * http://localhost:8080/kafka/sendMsgTransactional2?topic=car-infos
     * 生成者发送消息事务管理方式2：使用 executeInTransaction(OperationsCallback<K, V, T> callback)
     * executeInTransaction：表示执行本地事务，不参与全局事务(如果存在)，即方法内部和外部是分离的，只要内部不
     * 发生异常，消息就会发送，与外部无关，即使外部有 @Transactional 注解也不影响消息发送，此时外围有没有 @Transactional 都一样。
     *
     * @param topic
     * @param message
     * @return
     */
    @PostMapping("kafka/sendMsgTransactional2")
    public Map<String, Object> sendMessageTransactional2(@RequestParam("topic") String topic,
                                                         @RequestBody Map<String, Object> message) {
        return kafkaProducerStart.sendMessageTransactional2(topic, message);
    }

}
