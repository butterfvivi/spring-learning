package org.vivi.framework.report.service.handler;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.vivi.framework.report.service.model.luckysheet.Luckysheet;

import java.util.List;

public interface ILuckysheetService extends IService<Luckysheet> {

   /**
    * @MethodName: saveBatch
    * @Description: 批量保存
    */
   void saveBatchDatas(List<Luckysheet> models);

   /**
    * @MethodName: getOneButJsonData
    * @Description: 获取一条数据除了json_data字段，如果json_data字段太大查询太慢
    */
   Luckysheet getOneButJsonData(JSONObject jsonObject);
}
