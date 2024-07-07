package org.vivi.framework.mapstruct.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.vivi.framework.mapstruct.model.entity.Student;
import org.vivi.framework.mapstruct.model.vo.StudentVO;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    Student toModel(StudentVO studentVO);

}
