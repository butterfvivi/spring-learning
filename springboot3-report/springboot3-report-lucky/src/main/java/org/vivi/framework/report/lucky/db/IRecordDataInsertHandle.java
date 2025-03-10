package org.vivi.framework.report.lucky.db;

import com.alibaba.fastjson.JSONObject;
import org.vivi.framework.report.lucky.entity.GridRecordDataModel;

import java.util.List;

/**
 * 插入数据
 */
public interface IRecordDataInsertHandle {
    /**
     * 新增Sheet页,并返回刚刚插入的_id
     * @param pgModel
     * @return
     */
    String insert(GridRecordDataModel pgModel);
    /**
     * 批量添加 添加jsonb
     * @param models
     * @return
     */
    String InsertIntoBatch(List<GridRecordDataModel> models);

    /**
     * 批量添加 添加jsonb
     * @param models
     * @return
     */
    String InsertBatchDb(List<JSONObject> models);
}
