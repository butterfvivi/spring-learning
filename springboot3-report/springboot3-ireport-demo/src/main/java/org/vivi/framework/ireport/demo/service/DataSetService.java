package org.vivi.framework.ireport.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.common.enums.YesNoEnum;
import org.vivi.framework.ireport.demo.common.utils.ParamUtils;
import org.vivi.framework.ireport.demo.mapper.ISqlMapper;
import org.vivi.framework.ireport.demo.mapper.ReportSettingMapper;
import org.vivi.framework.ireport.demo.model.PageEntity;
import org.vivi.framework.ireport.demo.model.report.ReportSetting;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;
import org.vivi.framework.ireport.demo.process.JdbcProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataSetService extends ServiceImpl<ReportSettingMapper, ReportSetting> {

    @Autowired
    private ISqlMapper sqlMapper;

    public PageEntity getDatas(GenerateReportDto reportDto) {
        //get report config info
        ReportSetting reportSetting = this.getById(reportDto.getId());
        //get param sql
        String sql = reportSetting.getDynSentence();
        //get count sql
        String countSql = JdbcProcessor.getCountSql(sql);
        Map<String, Object> searchInfo  = reportDto.getSearchData();

        //handle sql params to prepare sql
        sql = JdbcProcessor.processSqlParams(sql,searchInfo);
        //handle pagination
        if(reportDto.isPatination() )
        {
            countSql = JdbcProcessor.getCountSql(sql);
            if(YesNoEnum.YES.getCode().intValue() == reportDto.getIsCustomerPage().intValue())
            {
                sql = JdbcProcessor.getPaginationSql(sql, 0, Integer.valueOf(String.valueOf(reportDto.getPagination().get("pageCount"))), reportDto.getStartPage(),reportDto.getEndPage());
            }else {
                sql = JdbcProcessor.getPaginationSql(sql, 0, Integer.valueOf(String.valueOf(reportDto.getPagination().get("pageCount"))), Integer.valueOf(String.valueOf(reportDto.getPagination().get("currentPage"))));
            }
        }

        Map<String, Object> mergePagination = new HashMap();
        mergePagination.put("currentPage", reportDto.getPagination().get("currentPage"));
        if(StringUtils.isNotEmpty(countSql))
        {
            long count = sqlMapper.getCount( countSql);
            Long totalCount = (Long) mergePagination.get("totalCount");
            Integer pageCount = (Integer) mergePagination.get("pageCount");
            Integer paramsPageCount = Integer.valueOf(String.valueOf(reportDto.getPagination().get("pageCount")));
            if(totalCount == null)
            {
                mergePagination.put("totalCount", count);
            }else {
                if(count > totalCount)
                {
                    mergePagination.put("totalCount", count);
                }
            }
            if(pageCount == null)
            {
                mergePagination.put("pageCount", paramsPageCount);
            }else {
                if(paramsPageCount < pageCount)
                {
                    mergePagination.put("pageCount", paramsPageCount);
                }
            }
        }

        Integer currentPage = Integer.valueOf(String.valueOf(mergePagination.get("currentPage")));
        Integer pageCount = Integer.valueOf(String.valueOf(mergePagination.get("pageCount")));
        Long totalCount = mergePagination.get("totalCount") == null ? 0 : Long.valueOf(String.valueOf(mergePagination.get("totalCount")));

        sql = JdbcProcessor.processSqlPage(sql,pageCount,currentPage);
        List<Map<String, Object>> mapList = sqlMapper.selectList(sql);

        return PageEntity
                .builder()
                .total(totalCount)
                .pageSize(pageCount)
                .currentPage(currentPage)
                .data(mapList)
                .build();
    }

    public List<Map<String, Object>>  getAllData(GenerateReportDto reportDto) {
        //get report config info
        ReportSetting reportSetting = this.getById(reportDto.getId());
        //get param sql
        String sql = reportSetting.getDynSentence();
        //get count sql
        Map<String, Object> searchInfo  = reportDto.getSearchData();

        //handle sql params to prepare sql
        sql = JdbcProcessor.processSqlParams(sql,searchInfo);
        return sqlMapper.selectList(sql);
    }

    public List<Map<String, Object>> getColumnInfos(GenerateReportDto reportDto) {
        //get report config info
        ReportSetting reportSetting = this.getById(reportDto.getId());
        //get param sql
        String sql = reportSetting.getDynSentence();
        Map<String, Object> params  = reportDto.getSearchData();;
        List<Map<String, Object>> dataSetColumns = JdbcProcessor.parseMetaDataColumns( sql,1,null);
        return dataSetColumns;
    }
}
