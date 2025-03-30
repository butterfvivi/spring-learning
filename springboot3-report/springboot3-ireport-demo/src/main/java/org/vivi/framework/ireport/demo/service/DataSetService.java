package org.vivi.framework.ireport.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.common.enums.YesNoEnum;
import org.vivi.framework.ireport.demo.common.utils.ParamUtils;
import org.vivi.framework.ireport.demo.mapper.ISqlMapper;
import org.vivi.framework.ireport.demo.mapper.ReportDataSetMapper;
import org.vivi.framework.ireport.demo.web.dto.DataExecSqlDto;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;
import org.vivi.framework.ireport.demo.process.JdbcProcessor;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataSetService {

    @Autowired
    private ISqlMapper sqlMapper;

    @Autowired
    private ReportDataSetMapper reportDataSetMapper;

    public List<Map<String, Object>> getDatas(GenerateReportDto reportDto) {
        //get report config info
        DataExecSqlDto dataExecSqlDto = reportDataSetMapper.selectById(reportDto.getId());
        //get param sql
        String sql = dataExecSqlDto.getReportSql();
        //get count sql
        String countSql = JdbcProcessor.getCountSql(sql);
        Map<String, Object> searchInfo  = reportDto.getSearchData();;
        Map<String, Object> params = null;
        //handle  params
        if(searchInfo != null)
        {
            JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(searchInfo.get("params")));
            params = ParamUtils.getViewParams(jsonArray);
        }

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

        //handle sql params to prepare sql
        sql = JdbcProcessor.processSqlParams(sql,params);

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


        return sqlMapper.selectList(sql);
    }
}
