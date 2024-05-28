package org.vivi.framework.iexcel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vivi.framework.iexcel.entity.ExcelRecord;


@Repository
public interface ExcelRecordRepository extends JpaRepository<ExcelRecord, Long> {
}
