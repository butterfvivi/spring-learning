package org.vivi.framework.iexcelbatch.common.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public interface SpiceBaseMapper<T> extends BaseMapper<T> {
    /**
     *  批量插入
     * @param entityList
     * @return
     */
    int insertBatchSomeColumn(List<T> entityList);
}
