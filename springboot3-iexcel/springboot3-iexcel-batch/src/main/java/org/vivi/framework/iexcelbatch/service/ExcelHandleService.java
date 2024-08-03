package org.vivi.framework.iexcelbatch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelbatch.entity.model.User;
import org.vivi.framework.iexcelbatch.entity.query.UserQuery;

public interface ExcelHandleService {

    Boolean importExcel(HttpServletRequest request, HttpServletResponse response, MultipartFile file);


    /**
     * 分批导出
     *
     * @param request
     * @param response
     * @param query
     */
    void exportExcel(HttpServletRequest request, HttpServletResponse response, UserQuery query);

    /**
     * 模板导出
     *
     * @param request
     * @param response
     * @return
     */
    void exportTemplate(HttpServletRequest request, HttpServletResponse response, UserQuery query);

    /**
     * 动态导出
     * @param request
     * @param response
     * @param query
     */
    void exportDynamic(HttpServletRequest request, HttpServletResponse response, UserQuery query);

    /**
     * 模板导入
     * @param request
     * @param response
     * @param file
     * @return
     */
    Boolean importTemplate(HttpServletRequest request, HttpServletResponse response, MultipartFile file);
}
