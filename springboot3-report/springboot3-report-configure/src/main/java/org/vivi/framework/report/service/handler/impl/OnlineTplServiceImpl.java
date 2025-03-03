package org.vivi.framework.report.service.handler.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.report.service.handler.IOnlineTplService;
import org.vivi.framework.report.service.mapper.onlinetpl.OnlineTplMapper;
import org.vivi.framework.report.service.model.onlinetpl.OnlineTpl;

@Service
public class OnlineTplServiceImpl  extends ServiceImpl<OnlineTplMapper, OnlineTpl> implements IOnlineTplService {
}
