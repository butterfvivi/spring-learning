package org.vivi.framework.ireport.demo.service.datatransform;

import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;

import java.util.List;

public interface IReportDataStrategy<T> {


    /**
     * 类型
     */
    String type();

    /**
     * data transform
     */
    <T> List<T> transform(GenerateReportDto previewDto);
}
