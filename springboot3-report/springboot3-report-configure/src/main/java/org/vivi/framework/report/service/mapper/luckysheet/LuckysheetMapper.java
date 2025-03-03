package org.vivi.framework.report.service.mapper.luckysheet;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.report.service.model.luckysheet.Luckysheet;

@Mapper
public interface LuckysheetMapper extends BaseMapper<Luckysheet>{

   /**
    * @MethodName: getOneButJsonData
    * @Description: 获取一条数据除了json_data字段，如果json_data字段太大查询太慢
    */
   Luckysheet getOneButJsonData(JSONObject jsonObject);


}
