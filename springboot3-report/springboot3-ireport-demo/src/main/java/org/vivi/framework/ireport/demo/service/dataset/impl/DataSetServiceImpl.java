package org.vivi.framework.ireport.demo.service.dataset.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.common.enums.YesNoEnum;
import org.vivi.framework.ireport.demo.common.utils.JdbcUtils;
import org.vivi.framework.ireport.demo.mapper.ISqlMapper;
import org.vivi.framework.ireport.demo.mapper.ReportDataSetMapper;
import org.vivi.framework.ireport.demo.model.PageEntity;
import org.vivi.framework.ireport.demo.model.dataset.ReportDataSet;
import org.vivi.framework.ireport.demo.service.dataset.DataSetService;
import org.vivi.framework.ireport.demo.web.dto.DataSearchDto;
import org.vivi.framework.ireport.demo.web.dto.ReportPageDto;

import java.util.List;
import java.util.Map;

@Service
public class DataSetServiceImpl extends ServiceImpl<ReportDataSetMapper, ReportDataSet> implements DataSetService {

    @Autowired
    private ISqlMapper sqlMapper;

    @Override
    public List<ReportDataSet> getAllReportSet(Long reportId) {
        return this.list(lambdaQuery().eq(ReportDataSet::getRtId, reportId));
    }

    public PageEntity getPageData(ReportPageDto reportPageDto) {
        //get report config info
        ReportDataSet reportDataSet = this.getById(reportPageDto.getRtId());
        //get param sql
        String sql = reportDataSet.getRtSql();
        //get count sql
        String countSql = JdbcUtils.getCountSql(sql);
        Map<String, Object> searchInfo  = reportPageDto.getSearchData();

        //handle sql params to prepare sql
        sql = JdbcUtils.processSqlParams(sql,searchInfo);
        //handle pagination
        if(reportPageDto.isPatination() )
        {
            countSql = JdbcUtils.getCountSql(sql);
            if(YesNoEnum.YES.getCode().intValue() == reportPageDto.getIsCustomerPage().intValue())
            {
                sql = JdbcUtils.getPaginationSql(sql, 0, Integer.valueOf(String.valueOf(reportPageDto.getPagination().get("pageCount"))), reportPageDto.getStartPage(), reportPageDto.getEndPage());
            }else {
                sql = JdbcUtils.getPaginationSql(sql, 0, Integer.valueOf(String.valueOf(reportPageDto.getPagination().get("pageCount"))), Integer.valueOf(String.valueOf(reportPageDto.getPagination().get("currentPage"))));
            }
        }

        PageEntity pageEntity = new PageEntity();

        pageEntity.setCurrentPage(reportPageDto.getPagination().get("currentPage"));
        if(StringUtils.isNotEmpty(countSql))
        {
            long count = sqlMapper.getCount( countSql);
            Long totalCount = pageEntity.getTotal();
            Integer pageCount =  pageEntity.getPageSize();
            Integer paramsPageCount =  reportPageDto.getPagination().get("pageCount");
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

    @Override
    public List<Map<String, Object>> getAllMapData(DataSearchDto dataSearchDto) {
        //get report config info
        ReportDataSet reportDataSet = this.getById(dataSearchDto.getSetId());
        //get param sql
        String sql = reportDataSet.getRtSql();
        //get count sql
        Map<String, Object> searchInfo  = dataSearchDto.getParams();

        //handle sql params to prepare sql
        sql = JdbcUtils.processSqlParams(sql,searchInfo);
        return sqlMapper.selectList(sql);
    }

    public List<Map<String, Object>> getColumnInfos(ReportPageDto reportPageDto) {
        //get report config info
        ReportDataSet reportDataSet = this.getById(reportPageDto.getRtId());
        //get param sql
        String sql = reportDataSet.getRtSql();
        Map<String, Object> params  = reportPageDto.getSearchData();;
        List<Map<String, Object>> dataSetColumns = JdbcUtils.parseMetaDataColumns( sql,1,null);
        return dataSetColumns;
    }

}