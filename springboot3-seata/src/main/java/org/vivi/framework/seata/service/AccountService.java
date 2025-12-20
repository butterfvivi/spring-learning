package org.vivi.framework.seata.service;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.vivi.framework.seata.mapper.AccountMapper;
import org.vivi.framework.seata.model.Account;

@Service
public class AccountService {

    private AccountMapper accountMapper;

  @GlobalTransactional
  public int save(Account account) {
    return accountMapper.insert(account);
   }
   public int add(Account account) {
    return accountMapper.add(account);
   }


}
