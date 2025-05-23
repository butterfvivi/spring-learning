package org.vivi.framework.iasyncexcel.core.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("excel_task")
public class ExcelTask {

    private Long id;

    /**
     * 类型：1-导入,2-导出
     */
    private Integer type;

    /**
     * 状态：0-初始,1-进行中,2-完成,3-失败
     */
    private Integer status;

    /**
     * 源文件
     */
    private String sourceFile;

    /**
     * 预估记录数 可能包含空行数据不准确，但是大部分情况时准确的
     */
    private Long estimateCount=0L;

    /**
     * 实际总记录数 为成功记录数+失败记录数
     */
    private Long totalCount;

    /**
     * 成功记录数
     */
    private Long successCount;

    /**
     * 失败记录数
     */
    private Long failedCount;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 成功文件路径
     */
    private String fileUrl;

    /**
     * 失败文件路径
     */
    private String failedFileUrl;

    /**
     * 失败消息
     */
    private String failedMessage;

    /**
     * 导入开始时间
     */
    private LocalDateTime startTime;

    /**
     * 导入结束时间
     */
    private LocalDateTime endTime;
}
