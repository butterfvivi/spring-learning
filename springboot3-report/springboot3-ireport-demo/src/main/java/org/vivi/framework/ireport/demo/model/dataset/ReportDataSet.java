package org.vivi.framework.ireport.demo.model.dataset;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("report_dataset")
public class ReportDataSet {

    private Long id;

    private Integer rtId;

    private Integer sheetIndex;

    private String rtSql;

    private String setParams;

    private boolean isPagination;

    private Integer dataType;
}
