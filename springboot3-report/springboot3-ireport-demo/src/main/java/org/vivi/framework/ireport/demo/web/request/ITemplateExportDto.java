package org.vivi.framework.ireport.demo.web.request;

import lombok.Data;
import org.vivi.framework.ireport.demo.core.dto.IExportConfig;

import java.util.List;
import java.util.Map;

@Data
public class ITemplateExportDto {

    // template path, if it is a template export, the path of the template file must be specified
    private String templatePath;

    private List dataList;

    // header data, can also include some query conditions and other data
    private Map<String, Object> otherVal;

    private IExportConfig config;
}
