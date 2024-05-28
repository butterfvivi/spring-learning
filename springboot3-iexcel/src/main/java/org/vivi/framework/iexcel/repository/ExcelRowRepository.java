package org.vivi.framework.iexcel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vivi.framework.iexcel.entity.ExcelRow;
import org.vivi.framework.iexcel.entity.constant.EXCEL_ROW_STATUS;

import java.util.List;


@Repository
public interface ExcelRowRepository extends JpaRepository<ExcelRow, Long> {

    List<ExcelRow> findByExcelRecordIdAndStatus(Long excelRecordId, EXCEL_ROW_STATUS rowStatus);

    Page<ExcelRow> findByExcelRecordIdAndStatus(Long excelRecordId, EXCEL_ROW_STATUS rowStatus, Pageable pageable);

    //Object findByExcelRecordIdAndStatus(Long excelRecordId, EXCEL_ROW_STATUS excelRowStatus, Object pageable);
}
