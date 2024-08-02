package org.vivi.framework.iexcelsimple.toolkit.core;

import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelsimple.common.utils.IocUtil;
import org.vivi.framework.iexcelsimple.entity.model.ImportDto;
import org.vivi.framework.iexcelsimple.toolkit.achieve.ExcelInvokeCore;

public class ExcelInvoke {

    private static ExcelInvokeCore excelInvokeCore = IocUtil.getBean(ExcelInvokeCore.class);

    /**
     * 导入数据解析，要求必须有一个实体类，目的是为了便于后期人员维护，和解析数据方便
     *
     * @param file
     * @param dto  {"targetParam":"","headRow":头部占几行}
     * @return 返回数据取决于用户自己定义
     * @author mashuai
     */

    public static Object importExcel(MultipartFile file, ImportDto dto) throws Exception {
        return excelInvokeCore.importExcel(file, dto);
    }
}
