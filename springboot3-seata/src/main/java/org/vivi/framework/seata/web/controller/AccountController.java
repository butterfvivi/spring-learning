package org.vivi.framework.seata.web.controller;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.seata.model.Account;
import org.vivi.framework.seata.service.AccountService;
import org.vivi.framework.seata.web.vo.TransferVo;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @RequestMapping("/save")
    public int save(@RequestBody Account accountVo) {
        return accountService.save(accountVo);
    }
    @RequestMapping("/transfer")
    @GlobalTransactional
    public boolean transfer(@RequestBody TransferVo transferVo) {
        // 转出方
        accountService.add(new Account(transferVo.getFrom(), -1 * transferVo.getAccount()));
        // 转入方
        accountService.add(new Account(transferVo.getTo(), transferVo.getAccount()));
        // int i = 1 / 0; // 人为制造异常
        return true;
   }
}
