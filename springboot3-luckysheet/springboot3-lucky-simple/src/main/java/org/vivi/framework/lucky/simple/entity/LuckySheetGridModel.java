package org.vivi.framework.lucky.simple.entity;


import lombok.Data;


/**
 * To change this template use File | Settings | File Templates.
 */
@Data
public class LuckySheetGridModel implements BaseModel {
    private String list_id;

    /**
     * 表格名称
     */
    private String grid_name;
    /**
     *  '缩略图'
     */
    private byte[] grid_thumb;

}
