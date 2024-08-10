package org.vivi.framework.iexcelbatch.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(name = "管理后台 - 数据导入 Response VO")
@Data
@Builder
public class DataExcelImportDto {

    @Schema(name =  "导入成功的数量")
    private Integer successCount;

    @Schema(name =  "导入失败的数量")
    private Integer failCount;

    @Schema(name =  "导入用时")
    private Long time;
}
