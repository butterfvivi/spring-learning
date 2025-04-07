package org.vivi.framework.ureport.store.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.vivi.framework.ureport.store.domain.bo.UreportFileBo;
import org.vivi.framework.ureport.store.domain.vo.UreportFileVo;

import java.util.List;

public interface IUreportService {
    Page getUreportFileList(UreportFileBo bo);

    boolean saveUreportFile(UreportFileBo bo);

    UreportFileVo getUreportFileByFile(UreportFileBo bo);

    boolean isExists(UreportFileBo bo);

    boolean updateUreportFile(UreportFileBo bo);

    List<UreportFileVo> selectUreportFile();

    boolean deleteUreportFile(UreportFileBo bo);



}
