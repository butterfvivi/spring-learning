package org.vivi.framework.report.service.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.vivi.framework.report.service.handler.ILuckysheetService;
import org.vivi.framework.report.service.mapper.luckysheet.LuckysheetMapper;
import org.vivi.framework.report.service.model.luckysheet.Luckysheet;

import java.util.List;

/**
* @Description: Luckysheet服务实现
*/
@Service
public class LuckysheetServiceImpl extends ServiceImpl<LuckysheetMapper, Luckysheet> implements ILuckysheetService {

   /**
    * @MethodName: saveBatch
    * @Description: 批量保存
    */
   @Override
   @Async
   public void saveBatchDatas(List<Luckysheet> models) {
       this.saveBatch(models);
   }

   /**
    * @MethodName: getOneButJsonData
    * @Description: 获取一条数据除了json_data字段，如果json_data字段太大查询太慢
    */
   @Override
   public Luckysheet getOneButJsonData(JSONObject jsonObject) {
       Luckysheet result = this.baseMapper.getOneButJsonData(jsonObject);
       return result;
   }



}