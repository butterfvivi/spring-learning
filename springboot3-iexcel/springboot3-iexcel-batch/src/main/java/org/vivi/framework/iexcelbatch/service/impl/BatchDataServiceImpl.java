package org.vivi.framework.iexcelbatch.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.iexcelbatch.common.enums.Constant;
import org.vivi.framework.iexcelbatch.entity.model.Item;
import org.vivi.framework.iexcelbatch.entity.model.User;
import org.vivi.framework.iexcelbatch.mapper.ItemMapper;
import org.vivi.framework.iexcelbatch.mapper.UserMapper;
import org.vivi.framework.iexcelbatch.service.BatchDataService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
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

    @Autowired
    private UserMapper userMapper;


    /**
     * 批量预生成并插入数据-多线程
     *
     * @param times
     * @throws Exception
     */
    @Override
    public void batchInitItem(Integer times) throws Exception {
        List<ItemBatchCallable> list = Lists.newLinkedList();
        for (int i = 1; i <= times; i++) {
            list.add(new ItemBatchCallable(Constant.batchDataSize));
        }
        EXECUTOR.invokeAll(list);
    }

    @Override
    public void batchUsers(Integer times) throws Exception {
        List<UserBatchCallable> list = Lists.newLinkedList();
        for (int i = 1; i <= times; i++) {
            list.add(new UserBatchCallable(Constant.batchDataSize));
        }
        EXECUTOR.invokeAll(list);
    }

    class ItemBatchCallable implements Callable<Boolean> {
        private Long total;

        public ItemBatchCallable(Long total) {
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

    class UserBatchCallable implements Callable<Boolean> {
        private Long total;

        public UserBatchCallable(Long total) {
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
                List<User> user = Lists.newLinkedList();
                Long i = 0L;
                for (; i < total; i++) {
                    user.add(new User(i, "张三" + i,1,30, LocalDate.now(),new BigDecimal(100),LocalDateTime.now(),
                            "filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,
                            "filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,
                            "filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,
                            "filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,
                            "filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,
                            "filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,"filed"+i,
                            "filed"+i,"filed"+i));
                }
                userMapper.insertBatchSomeColumn(user);
            } catch (Exception e) {
                log.error("异常：", e);
            }
            return true;
        }
    }

}
