package org.vivi.framework.lucky.mongodb.entity;

import cn.hutool.json.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Document(collection = "worksheet")
@Getter
@Setter
public class WorkSheetEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String wbId;

    private JSONObject data;

    /**
     * 删除标记,0是未删除，1是删除
     */
    private int deleteStatus;

}
