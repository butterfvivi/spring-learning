package org.vivi.framework.ureport.store.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.vivi.framework.ureport.store.core.provider.report.mysql.MysqlReportProvider;
import org.vivi.framework.ureport.store.domain.UreportFile;
import org.vivi.framework.ureport.store.domain.bo.UreportFileBo;
import org.vivi.framework.ureport.store.domain.vo.UreportFileVo;
import org.vivi.framework.ureport.store.mapper.UreportFileMapper;
import org.vivi.framework.ureport.store.service.IUreportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UreportFileServiceImpl implements IUreportService {

    @Autowired
    private UreportFileMapper ureportFileMapper;

    @Autowired
    private MysqlReportProvider mysqlReportProvider;

    @Override
    public Page getUreportFileList(UreportFileBo bo) {

        //IPage<T>  Page<T>   PageDTO<T>
        Page<UreportFile> page=new Page<>();
        page.setCurrent(bo.getPage());
        page.setSize(bo.getLimit());
        bo.setPrefix(mysqlReportProvider.getPrefix());
        Page<UreportFileVo> ureportFileVoPage = ureportFileMapper.selectUreportFilePageVo(page,bo);

        return ureportFileVoPage;
    }

    @Override
    public boolean saveUreportFile(UreportFileBo bo) {
        UreportFile ureportFile=new UreportFile();
        BeanUtil.copyProperties(bo, ureportFile);
        return ureportFileMapper.insert(ureportFile)>0?true:false;
    }

    @Override
    public UreportFileVo getUreportFileByFile(UreportFileBo bo) {


        QueryWrapper<UreportFile> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("name",bo.getName());

        UreportFile ureportFile = ureportFileMapper.selectOne(queryWrapper);

        UreportFileVo vo=new UreportFileVo();

        BeanUtil.copyProperties(ureportFile, vo);

        return vo;
    }

    @Override
    public boolean isExists(UreportFileBo bo) {


        QueryWrapper<UreportFile> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",bo.getName());

        return ureportFileMapper.selectCount(queryWrapper)>0?true:false;
    }

    @Override
    public boolean updateUreportFile(UreportFileBo bo) {

        UpdateWrapper<UreportFile> updateWrapper=new UpdateWrapper<>();

        updateWrapper.eq("name",bo.getName());

        UreportFile ureportFile=new UreportFile();

        BeanUtil.copyProperties(bo, ureportFile);

        return ureportFileMapper.update(ureportFile,updateWrapper)>0?true:false;
    }

    @Override
    public List<UreportFileVo> selectUreportFile() {

        List<UreportFileVo> ureportFileVos = ureportFileMapper.selectUreportFile();

        return ureportFileVos;
    }

    @Override
    public boolean deleteUreportFile(UreportFileBo bo) {

        QueryWrapper<UreportFile> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",bo.getName());
        return ureportFileMapper.delete(queryWrapper)>0?true:false;


    }
}
