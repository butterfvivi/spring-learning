package org.vivi.framework.report.simple.modules.file.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.report.simple.modules.file.entity.IFile;

@Mapper
public interface FileMapper extends BaseMapper<IFile> {


}
