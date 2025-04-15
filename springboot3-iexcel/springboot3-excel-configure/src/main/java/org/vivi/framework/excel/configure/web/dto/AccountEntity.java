package org.vivi.framework.excel.configure.web.dto;

import lombok.Data;
import org.vivi.framework.excel.configure.base.annotations.ExportField;
import org.vivi.framework.excel.configure.base.annotations.ExportTitle;

@ExportTitle(title = "账号导出报表")
@Data
public class AccountEntity {


    @ExportField(index = 0,title = "序号")
    private long id;

    @ExportField(index = 1,title = "账号名称")
    private String accountNumber;

    @ExportField(index = 2,title = "账号密码")
    private String password;

    @ExportField(index = 3,title = "客户端id")
    private long clientId;

}
