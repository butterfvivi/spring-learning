package org.vivi.framework.report.bigdata.paging.paralle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.LongFunction;

/**
 * 平行实效
 * 并发工具类（并发生产数据，串行有序消费数据）
 */
public class ParallelUtil<R> {
    // 默认线程数
    public static final int DEF_PARALLEL_NUM = Runtime.getRuntime().availableProcessors();
    // 生产者并发线程数
    private int parallelNum;
    // 总任务数
    private long totalNum;
    // 消费者函数
    private Consumer<R> resultConsumer;
    // 生产者函数
    private LongFunction<R> producerFunction;
    // 生产者将任务放到此队列，消费者从此队列读数据
    private ArrayBlockingQueue<ParallelResult<R>> queue;
    // 生产者线程池
    private ExecutorService threadPoolExecutor;
    // 默认超时时间
    private long timeout = 60;
    // 默认超时时间单位
    private TimeUnit timeoutTimeUnit = TimeUnit.SECONDS;

    public static <R> ParallelUtil<R> parallel(Class<R> consumerClass, long totalNum) {
        return parallel(consumerClass, DEF_PARALLEL_NUM, totalNum);
    }

    /**
     * 初始化
     *
     * @param consumerClass 消费的类Class
     * @param parallelNum   并发线程数
     * @param totalNum      并发执行总数（触发asyncProducer函数次数）
     * @return {@link ParallelUtil}<{@link R}>
     */
    public static <R> ParallelUtil<R> parallel(Class<R> consumerClass, int parallelNum, long totalNum) {
        ParallelUtil<R> parallelUtil = new ParallelUtil<>();
        parallelUtil.parallelNum = (int) Math.max(1, Math.min(parallelNum, totalNum));
        parallelUtil.totalNum = totalNum;
        return parallelUtil;
    }

    /**
     * 消费者等待获取任务的超时时间（不设置则默认60秒）
     *
     * @param timeout 值
     * @param unit    时间单位
     * @return {@link ParallelUtil}<{@link R}>
     */
    public ParallelUtil<R> timeout(long timeout, TimeUnit unit) {
        this.timeout = timeout;
        this.timeoutTimeUnit = unit;
        return this;
    }

    /**
     * 异步并发生产者
     *
     * @param producerFunction 生产者函数，参数为1~totalNum，返回值为任意类型
     * @return {@link ParallelUtil}<{@link R}>
     */
    public ParallelUtil<R> asyncProducer(LongFunction<R> producerFunction) {
        this.producerFunction = producerFunction;
        return this;
    }

    /**
     * 消费者(串行有序消费生产者返回的数据)
     *
     * @param resultConsumer 因此消费者
     * @return {@link ParallelUtil}<{@link R}>
     */
    public ParallelUtil<R> syncConsumer(Consumer<R> resultConsumer) {
        this.resultConsumer = resultConsumer;
        return this;
    }

    /**
     * 开始执行
     */
    public void start() throws InterruptedException {
        try {
            // 如果无任务则直接返回
            if (totalNum <= 0) {
                return;
            }
            // 如果只有一个任务，则串行执行，生产者生成的数据直接给到消费者
            if (totalNum == 1) {
                resultConsumer.accept(producerFunction.apply(1));
                return;
            }
            // 初始化队列和线程池
            queue = new ArrayBlockingQueue<>(parallelNum);
            threadPoolExecutor = Executors.newVirtualThreadPerTaskExecutor();
            // 生产者开始执行
            Thread producerThread = Thread.ofVirtual().unstarted(() -> {
                try {
                    AtomicLong indexAtomicLong = new AtomicLong(1);
                    List<CompletableFuture<R>> futureList = new ArrayList<>(parallelNum);
                    for (long index = 1; index <= totalNum; index++) {
                        long finalIndex = index;
                        futureList.add(CompletableFuture.supplyAsync(() -> producerFunction.apply(finalIndex), threadPoolExecutor));
                        if (futureList.size() == parallelNum) {
                            for (CompletableFuture<R> future : futureList) {
                                queue.put(new ParallelResult<>(indexAtomicLong.getAndIncrement(), future.join()));
                            }
                            futureList.clear();
                        }
                    }
                    for (CompletableFuture<R> future : futureList) {
                        queue.put(new ParallelResult<>(indexAtomicLong.getAndIncrement(), future.join()));
                    }
                    futureList.clear();
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    // 添加一个空元素，防止queue.poll等待到超时
                    queue.offer(ParallelResult.empty());
                    throw new RuntimeException(e);
                }
            });
            producerThread.setDaemon(true);
            producerThread.start();
            AtomicReference<Throwable> exception = new AtomicReference<>();
            producerThread.setUncaughtExceptionHandler((t, e) -> exception.set(e));
            // 消费者等待消费
            AtomicLong count = new AtomicLong();
            ParallelResult<R> parallelResult;
            // 消费者等待消费
            while ((parallelResult = queue.poll(timeout, timeoutTimeUnit)) != null) {
                // 异常时添加的空元素则直接return
                if (parallelResult.isEmpty()) {
                    break;
                }
                // 消费者消费生产者生产的数据
                resultConsumer.accept(parallelResult.getData());
                count.incrementAndGet();
                // 已最后一条，直接结束，queue.poll等待问题
                if (parallelResult.getIndex() == totalNum) {
                    break;
                }
            }
            if (count.get() != totalNum) {
                throw new RuntimeException(exception.get() == null ? "timeout" : exception.get().getMessage());
            }
        } finally {
            if (threadPoolExecutor != null) {
                threadPoolExecutor.shutdown();
            }
        }
    }
}
