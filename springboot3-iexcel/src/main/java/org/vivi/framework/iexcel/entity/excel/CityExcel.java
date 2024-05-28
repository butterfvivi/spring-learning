package org.vivi.framework.iexcel.entity.excel;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.vivi.framework.iexcel.common.read.BasedExcelReadModel;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ExcelIgnoreUnannotated
public class CityExcel extends BasedExcelReadModel {

    @NotBlank(message = "城市编码不能为空")
    @ExcelProperty("城市编码")
    private String code;

    @NotBlank(message = "城市名称不能为空")
    @ExcelProperty("城市名称")
    private String name;

    @NotBlank(message = "省份编码不能为空")
    @ExcelProperty("省份编码")
    private String provinceCode;
}
