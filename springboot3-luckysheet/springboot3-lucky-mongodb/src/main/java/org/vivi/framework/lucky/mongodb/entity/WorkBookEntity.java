package org.vivi.framework.lucky.mongodb.entity;

import cn.hutool.json.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "workbook")
@Getter
@Setter
public class WorkBookEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String name;

    private JSONObject option;

//    private Date createdTime;
//
//    private Date modifyTime;
//
//    private List<String> userId;
}
