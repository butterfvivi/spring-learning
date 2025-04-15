package org.vivi.framework.report.bigdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.report.bigdata.entity.Demo;


/**
 * 演示映射器
 */
@Mapper
public interface DemoMapper extends BaseMapper<Demo> {
}
