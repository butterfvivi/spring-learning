package org.vivi.framework.iasyncexcel.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelContext {

    private ExcelTask task;

    private Long totalCount=0L;

    private Long failCount=0L;

    private Long successCount=0L;

    public void record(int dataSize){
        record(dataSize,0);
    }

    public void record(int dataSize,int errorSize){
        this.totalCount=this.totalCount+dataSize;
        this.successCount=this.successCount+dataSize-errorSize;
        this.failCount=this.failCount+errorSize;
    }
}
