
package org.vivi.framework.report.simple.modules.dashboard.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.report.simple.modules.dashboard.entity.ReportDashboard;
import org.vivi.framework.report.simple.web.dto.ChartDto;
import org.vivi.framework.report.simple.web.dto.ReportDashboardObjectDto;

/**
* @desc ReportDashboard 大屏设计服务接口
**/
public interface ReportDashboardService extends IService<ReportDashboard> {

    /***
     * 查询详情
     *
     * @param reportCode
     */
    ReportDashboardObjectDto getDetail(String reportCode);

    /***
     * 保存大屏设计
     *
     * @param dto
     */
    void insertDashboard(ReportDashboardObjectDto dto);


    /**
     * 获取单个图表数据
     * @param dto
     * @return
     */
    Object getChartData(ChartDto dto);


    /**
     * 导出大屏，zip文件
     * @param request
     * @param response
     * @param reportCode
     * @return
     */
    ResponseEntity<byte[]> exportDashboard(HttpServletRequest request, HttpServletResponse response, String reportCode, Integer showDataSet);

    /**
     * 导入大屏zip
     * @param file
     * @param reportCode
     * @return
     */
    void importDashboard(MultipartFile file, String reportCode);
}
