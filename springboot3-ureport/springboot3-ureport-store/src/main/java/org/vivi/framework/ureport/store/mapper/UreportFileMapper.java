package org.vivi.framework.ureport.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.vivi.framework.ureport.store.domain.UreportFile;
import org.vivi.framework.ureport.store.domain.bo.UreportFileBo;
import org.vivi.framework.ureport.store.domain.vo.UreportFileVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UreportFileMapper extends BaseMapper<UreportFile> {
    Page<UreportFileVo> selectUreportFilePageVo(@Param("page") Page<UreportFile> page,@Param("bo") UreportFileBo bo );

    List<UreportFileVo> selectUreportFile();

}
