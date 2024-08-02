package org.vivi.framework.iexcelsimple.service;

import org.springframework.stereotype.Service;
import org.vivi.framework.iexcelsimple.toolkit.annotation.IExcelRewrite;
import org.vivi.framework.iexcelsimple.entity.model.UserRequest;

import java.util.List;

@IExcelRewrite(targetParam = "excel")
@Service(value = "mExcelService")
public class MExcelService {

    /***
     * 规上企业excel导入
     * **/
    @IExcelRewrite(targetParam = "import", entityClass = UserRequest.class)
    public Object analysisImport(List<UserRequest> data) {
        return data;
    }
}
