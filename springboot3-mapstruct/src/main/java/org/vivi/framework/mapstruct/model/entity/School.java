package org.vivi.framework.mapstruct.model.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class School {

    private List<Grade> grades;
}
