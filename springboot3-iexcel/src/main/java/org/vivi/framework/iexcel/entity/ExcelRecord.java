package org.vivi.framework.iexcel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.vivi.framework.iexcel.entity.constant.EXCEL_BIZ_TYPE;


@Getter
@Setter
@ToString
@Entity
@Table(name = "excel_records")
public class ExcelRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "biz_type", nullable = false, length = 45)
    private EXCEL_BIZ_TYPE excelBizType;

    @Column(name = "op_uname", nullable = false, length = 45)
    private String opUname;
}
