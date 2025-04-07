package org.vivi.framework.ureport.store.service;

import org.vivi.framework.ureport.store.domain.User;
import org.vivi.framework.ureport.store.domain.bo.UserBo;

public interface IUserService {
    User selectUserByUserName(UserBo bo);




}
