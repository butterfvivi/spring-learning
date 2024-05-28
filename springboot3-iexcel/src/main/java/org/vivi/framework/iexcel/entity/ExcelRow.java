package org.vivi.framework.iexcel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.vivi.framework.iexcel.entity.constant.EXCEL_ROW_STATUS;


@Getter
@Setter
@ToString
@Entity
@Table(name = "excel_rows")
public class ExcelRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "excel_record_id", nullable = false)
    private Long excelRecordId;

    @Column(name = "row_data", columnDefinition = "json")
    private String rowData;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 45)
    private EXCEL_ROW_STATUS status;

    @Column(name = "msg")
    private String msg;
}
