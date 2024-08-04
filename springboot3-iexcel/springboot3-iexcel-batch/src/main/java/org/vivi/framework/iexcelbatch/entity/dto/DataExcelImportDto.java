package org.vivi.framework.iexcelbatch.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel("管理后台 - 数据导入 Response VO")
@Data
@Builder
public class DataExcelImportDto {

    @ApiModelProperty(value = "导入成功的数量")
    private Integer successCount;

    @ApiModelProperty(value = "导入失败的数量")
    private Integer failCount;

    @ApiModelProperty(value = "导入用时")
    private Long time;
}
