package org.vivi.framework.iasyncexcel.common.configuration;

import lombok.Data;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Data
public class ThreadPoolConfig {

    private static BlockingQueue BLOCKING_QUEUE = new ArrayBlockingQueue(8);

    private static ThreadPoolExecutor.CallerRunsPolicy POLICY = new ThreadPoolExecutor.CallerRunsPolicy();

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 8, 60, TimeUnit.SECONDS, BLOCKING_QUEUE, POLICY);

}
