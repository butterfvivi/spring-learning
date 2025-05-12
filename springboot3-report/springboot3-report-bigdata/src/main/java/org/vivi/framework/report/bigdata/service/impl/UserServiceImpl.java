package org.vivi.framework.report.bigdata.service.impl;

import org.springframework.stereotype.Service;
import org.vivi.framework.report.bigdata.entity.ItemUser;
import org.vivi.framework.report.bigdata.mapper.ItemUserMapper;
import org.vivi.framework.report.bigdata.paging.service.BaseServiceImpl;
import org.vivi.framework.report.bigdata.service.UserService;

@Service
public class UserServiceImpl extends BaseServiceImpl<ItemUserMapper, ItemUser> implements UserService {
}
