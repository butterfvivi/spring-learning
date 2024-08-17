package org.vivi.framework.iasyncexcel.starter.context.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.iasyncexcel.core.service.TaskService;
import org.vivi.framework.iasyncexcel.starter.context.mapper.ExcelTaskMapper;
import org.vivi.framework.iasyncexcel.core.model.ExcelTask;

@Service
public class TaskManageService extends ServiceImpl<ExcelTaskMapper, ExcelTask> implements TaskService {

    @Override
    public boolean save(ExcelTask entity) {
        return super.save(entity);
    }

    @Override
    public boolean updateById(ExcelTask entity) {
        return super.updateById(entity);
    }
}
