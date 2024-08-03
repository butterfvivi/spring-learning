package org.vivi.framework.iexcelbatch.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.iexcelbatch.common.enums.Constant;
import org.vivi.framework.iexcelbatch.entity.model.Item;
import org.vivi.framework.iexcelbatch.mapper.ItemMapper;
import org.vivi.framework.iexcelbatch.service.BatchDataService;

import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
public class BatchDataServiceImpl implements BatchDataService {

    private static final BlockingQueue BLOCKING_QUEUE = new ArrayBlockingQueue(8);

    private static final ThreadPoolExecutor.CallerRunsPolicy POLICY = new ThreadPoolExecutor.CallerRunsPolicy();

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(4, 8, 60, TimeUnit.SECONDS, BLOCKING_QUEUE, POLICY);

    private static final Snowflake SNOWFLAKE = new Snowflake(2, 3);

    @Autowired
    private ItemMapper itemMapper;
    /**
     * 批量预生成并插入数据-多线程
     *
     * @param times
     * @throws Exception
     */
    public void batchInitItem(Integer times) throws Exception {
        List<BatchCallable> list = Lists.newLinkedList();
        for (int i = 1; i <= times; i++) {
            list.add(new BatchCallable(Constant.batchDataSize));
        }
        EXECUTOR.invokeAll(list);
    }

    class BatchCallable implements Callable<Boolean> {
        private Long total;

        public BatchCallable(Long total) {
            this.total = total;
        }

        /**
         * 真正的实现批量插入数据的代码逻辑
         *
         * @return
         * @throws Exception
         */
        @Override
        public Boolean call() throws Exception {
            try {
                List<Item> items = Lists.newLinkedList();
                Long i = 0L;
                for (; i < total; i++) {
                    items.add(new Item(SNOWFLAKE.nextIdStr(), SNOWFLAKE.nextIdStr()));
                }
                itemMapper.insertBatch(items);
            } catch (Exception e) {
                log.error("异常：", e);
            }
            return true;
        }
    }


}
