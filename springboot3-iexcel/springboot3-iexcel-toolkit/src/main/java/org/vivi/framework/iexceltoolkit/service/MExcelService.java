package org.vivi.framework.iexceltoolkit.service;

import org.springframework.stereotype.Service;
import org.vivi.framework.iexceltoolkit.entity.model.UserRequest;
import org.vivi.framework.iexceltoolkit.toolkit.annotation.IExcelRewrite;

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
