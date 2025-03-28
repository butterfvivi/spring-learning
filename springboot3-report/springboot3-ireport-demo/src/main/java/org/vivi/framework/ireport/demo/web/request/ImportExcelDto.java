package org.vivi.framework.ireport.demo.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImportExcelDto {

    //value cannot be empty, serviceName@methodName
    private String targetParam;

    //specify from the first row
    private Integer headRow;

    //import of any message, original pass to the user
    private String  remark;
}
