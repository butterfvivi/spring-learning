package org.vivi.framework.mapstruct.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Grade {


    private Integer id;

    private String gradeName;

    private List<Student> students;
}
