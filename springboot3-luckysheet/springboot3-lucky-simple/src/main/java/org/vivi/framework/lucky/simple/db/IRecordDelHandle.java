package org.vivi.framework.lucky.simple.db;


import org.vivi.framework.lucky.simple.entity.GridRecordDataModel;

import java.util.List;

/**
 * 删除
 */
public interface IRecordDelHandle {
    /**
     * 删除sheet（非物理删除）
     * @param model
     * @return
     */
    boolean updateDataForReDel(GridRecordDataModel model);

    /**
     * 按ID 删除多个文档 （物理删除）
     * @param ids
     * @return
     */
    String delDocuments(List<String> ids);

    /**
     * 按list_id 删除记录 （物理删除）
     * @param listIds
     * @return
     */
    int[] delete(List<String> listIds);




}
