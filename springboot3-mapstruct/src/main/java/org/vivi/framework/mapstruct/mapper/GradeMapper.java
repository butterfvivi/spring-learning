package org.vivi.framework.mapstruct.mapper;

import org.mapstruct.Mapper;
import org.vivi.framework.mapstruct.model.entity.Grade;
import org.vivi.framework.mapstruct.model.vo.GradeVO;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Mapper(componentModel = "spring",imports = {LocalDateTime.class, UUID.class})
public interface GradeMapper {

    Map<String, GradeVO> mapConvert(Map<String,Grade> gradeMap);
}
