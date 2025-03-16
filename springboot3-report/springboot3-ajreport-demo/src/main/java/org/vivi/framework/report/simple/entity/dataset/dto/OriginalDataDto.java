package org.vivi.framework.report.simple.entity.dataset.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class OriginalDataDto {

    /**总数*/
    private long total;

    /**获取的数据详情*/
    private List<JSONObject> data;

    public OriginalDataDto(List<JSONObject> data) {
        this.data = data;
    }

    public OriginalDataDto(long total, List<JSONObject> data) {
        this.total = total;
        this.data = data;
    }

    public OriginalDataDto() {
    }
}
