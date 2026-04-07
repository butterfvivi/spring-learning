package org.vivi.framework.drools.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.drools.demo.model.DroolsRule;

@Mapper
public interface RulesMapper extends BaseMapper<DroolsRule> {
}
