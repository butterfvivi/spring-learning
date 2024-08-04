package org.vivi.framework.iexceltoolkit.toolkit.dto;

import com.alibaba.excel.write.handler.WriteHandler;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class IExportConfig {

    //重写标识。比如传入的targetParam="c1@m1",那么首先在类上加上@MsAsync(targetParam="c1"),方法上加上@MsAsync(targetParam="m1")
    private String targetParam;

    //水印
    private String watermark;

    //合并的列，比如合并1，2两列，传入[0,1]
    private Set<Integer> mergeColIndex;

    //合并列的行界限,从第N行开始合并列，如果不指定，会跳过头部开始合并
    private Integer mergerRowIndexLimit;

    //合并列界限，以mergerColIndex[0]开始计算。逻辑如果列值为：col1,col1,col1.clo2,col3,那么界限第一次是2，也就是col1与clo2之间的
    private Integer mergerColIndexLimit;

    //表头最大显示多少字。后面所有表头的宽度都会和他一样长
    private Integer headerWord;

    //扩展：自定义excel的拦截处理器，一般不需。
    private List<WriteHandler> WriteHandlers;

    //模板导出时候，如果需要列合并，非表头，出现{}变量时候，需要排除当前行，否则可能进行合并
    private Set<Integer> excludeRowIndex;

    //模板导出时候，如果需要列合并，排除最后一行
    private Boolean excludeTillRow;

    // 合并时是否依赖左侧要要依赖的行
    private Boolean isNeedLeftConditionMerge;
}
