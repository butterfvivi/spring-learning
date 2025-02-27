package org.vivi.framework.report.simple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.report.simple.entity.file.IFile;

@Mapper
public interface FileMapper extends BaseMapper<IFile> {


}
