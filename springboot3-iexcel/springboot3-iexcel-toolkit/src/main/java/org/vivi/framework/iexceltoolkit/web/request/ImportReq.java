package org.vivi.framework.iexceltoolkit.web.request;

import lombok.Data;

@Data
public class ImportReq {

    //值不可为空，serviceName@methodName
    private String targetParam;

    //指定从第一行开始读
    private Integer headRow;

    //导入的备注，可以是任意消息，原封不懂传给用户
    private String  remark;
    public ImportReq(String targetParam, Integer headRow) {
        this.targetParam = targetParam;
        this.headRow = headRow;
    }
    public ImportReq(String targetParam, Integer headRow, String remark) {
        this.targetParam = targetParam;
        this.headRow = headRow;
        this.remark = remark;
    }
}

