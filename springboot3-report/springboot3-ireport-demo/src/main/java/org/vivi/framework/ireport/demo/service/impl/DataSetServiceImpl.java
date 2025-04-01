package org.vivi.framework.ireport.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.common.enums.YesNoEnum;
import org.vivi.framework.ireport.demo.common.utils.JdbcUtils;
import org.vivi.framework.ireport.demo.mapper.ISqlMapper;
import org.vivi.framework.ireport.demo.mapper.ReportSettingMapper;
import org.vivi.framework.ireport.demo.model.PageEntity;
import org.vivi.framework.ireport.demo.model.report.ReportSetting;
import org.vivi.framework.ireport.demo.service.DataSetService;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataSetServiceImpl extends ServiceImpl<ReportSettingMapper, ReportSetting> implements DataSetService {

    @Autowired
    private ISqlMapper sqlMapper;

    public PageEntity getDatas(GenerateReportDto reportDto) {
        //get report config info
        ReportSetting reportSetting = this.getById(reportDto.getId());
        //get param sql
        String sql = reportSetting.getDynSentence();
        //get count sql
        String countSql = JdbcUtils.getCountSql(sql);
        Map<String, Object> searchInfo  = reportDto.getSearchData();

        //handle sql params to prepare sql
        sql = JdbcUtils.processSqlParams(sql,searchInfo);
        //handle pagination
        if(reportDto.isPatination() )
        {
            countSql = JdbcUtils.getCountSql(sql);
            if(YesNoEnum.YES.getCode().intValue() == reportDto.getIsCustomerPage().intValue())
            {
                sql = JdbcUtils.getPaginationSql(sql, 0, Integer.valueOf(String.valueOf(reportDto.getPagination().get("pageCount"))), reportDto.getStartPage(),reportDto.getEndPage());
            }else {
                sql = JdbcUtils.getPaginationSql(sql, 0, Integer.valueOf(String.valueOf(reportDto.getPagination().get("pageCount"))), Integer.valueOf(String.valueOf(reportDto.getPagination().get("currentPage"))));
            }
        }

        PageEntity pageEntity = new PageEntity();

        pageEntity.setCurrentPage(reportDto.getPagination().get("currentPage"));
        if(StringUtils.isNotEmpty(countSql))
        {
            long count = sqlMapper.getCount( countSql);
            Long totalCount = pageEntity.getTotal();
            Integer pageCount =  pageEntity.getPageSize();
            Integer paramsPageCount =  reportDto.getPagination().get("pageCount");
            if(totalCount == null)
            {
                pageEntity.setTotal(count);
            }else {
                if(count > totalCount)
                {
                    pageEntity.setTotal(count);
                }
            }
            if(pageCount == null)
            {
                pageEntity.setPageSize(paramsPageCount);
            }else {
                if(paramsPageCount < pageCount)
                {
                    pageEntity.setPageSize( paramsPageCount);
                }
            }
        }

        Integer currentPage = pageEntity.getCurrentPage();
        Integer pageCount = pageEntity.getPageSize();

        sql = JdbcUtils.processSqlPage(sql,pageCount,currentPage);
        List<Map<String, Object>> mapList = sqlMapper.selectList(sql);
        pageEntity.setData(mapList);
        return pageEntity;
    }

    public List<Map<String, Object>>  getAllData(GenerateReportDto reportDto) {
        //get report config info
        ReportSetting reportSetting = this.getById(reportDto.getId());
        //get param sql
        String sql = reportSetting.getDynSentence();
        //get count sql
        Map<String, Object> searchInfo  = reportDto.getSearchData();

        //handle sql params to prepare sql
        sql = JdbcUtils.processSqlParams(sql,searchInfo);
        return sqlMapper.selectList(sql);
    }

    public List<Map<String, Object>> getColumnInfos(GenerateReportDto reportDto) {
        //get report config info
        ReportSetting reportSetting = this.getById(reportDto.getId());
        //get param sql
        String sql = reportSetting.getDynSentence();
        Map<String, Object> params  = reportDto.getSearchData();;
        List<Map<String, Object>> dataSetColumns = JdbcUtils.parseMetaDataColumns( sql,1,null);
        return dataSetColumns;
    }
}