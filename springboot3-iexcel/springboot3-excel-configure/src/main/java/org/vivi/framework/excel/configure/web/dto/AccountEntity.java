package org.vivi.framework.excel.configure.web.dto;

import org.vivi.framework.excel.configure.base.annotations.ExportField;
import org.vivi.framework.excel.configure.base.annotations.ExportTitle;

@ExportTitle(title = "账号导出报表")
public class AccountEntity {


    @ExportField(index = 0,title = "序号")
    private long id;

    @ExportField(index = 1,title = "账号名称")
    private String accountNumber;

    @ExportField(index = 2,title = "账号密码")
    private String PASSWORD;

    @ExportField(index = 3,title = "客户端id")
    private long clientId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
}
