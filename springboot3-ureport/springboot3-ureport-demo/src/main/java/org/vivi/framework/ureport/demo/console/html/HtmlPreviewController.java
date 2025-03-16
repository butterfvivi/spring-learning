package org.vivi.framework.ureport.demo.console.html;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vivi.framework.ureport.demo.console.AbstractReportBasicController;
import org.vivi.framework.ureport.demo.console.MobileUtils;
import org.vivi.framework.ureport.demo.console.common.R;
import org.vivi.framework.ureport.demo.console.exception.ReportDesignException;
import org.vivi.framework.ureport.demo.core.build.Context;
import org.vivi.framework.ureport.demo.core.build.ReportBuilder;
import org.vivi.framework.ureport.demo.core.build.paging.Page;
import org.vivi.framework.ureport.demo.core.cache.CacheUtils;
import org.vivi.framework.ureport.demo.core.chart.ChartData;
import org.vivi.framework.ureport.demo.core.definition.Paper;
import org.vivi.framework.ureport.demo.core.definition.ReportDefinition;
import org.vivi.framework.ureport.demo.core.definition.searchform.FormPosition;
import org.vivi.framework.ureport.demo.core.exception.ReportComputeException;
import org.vivi.framework.ureport.demo.core.export.*;
import org.vivi.framework.ureport.demo.core.export.html.HtmlProducer;
import org.vivi.framework.ureport.demo.core.export.html.HtmlReport;
import org.vivi.framework.ureport.demo.core.export.html.SearchFormData;
import org.vivi.framework.ureport.demo.core.model.Report;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * Description: html预览控制器
 */
@Controller
@RequestMapping(value = "/html")
public class HtmlPreviewController extends AbstractReportBasicController {

    @Autowired
    private ExportManager exportManager;

    @Autowired
    private ReportBuilder reportBuilder;

    @Autowired
    private ReportRender reportRender;

    private HtmlProducer htmlProducer = new HtmlProducer();

    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    public String preview(HttpServletRequest request, HttpServletResponse response, Model model) {
        HtmlReport htmlReport = null;
        String errorMsg = null;
        try {
            htmlReport = loadReport(request);
        } catch (Exception ex) {
            if (!(ex instanceof ReportDesignException)) {
                ex.printStackTrace();
            }
            errorMsg = buildExceptionMessage(ex);
        }
        String file = request.getParameter("_u");
        file = decode(file);
        String title = null;
        if (PREVIEW_KEY.equals(file)) {
            title = "报表预览";
        } else {
            title = file.split(":")[1].replace(".ureport.xml", "");
        }
        model.addAttribute("title", title);
        if (htmlReport == null) {
            model.addAttribute("content", "<div style='color:red'><strong>报表计算出错，错误信息如下：</strong><br><div style=\"margin:10px\">" + errorMsg + "</div></div>");
            model.addAttribute("error", true);
            model.addAttribute("searchFormJs", "");
            model.addAttribute("downSearchFormHtml", "");
            model.addAttribute("upSearchFormHtml", "");
        } else {
            SearchFormData formData = htmlReport.getSearchFormData();
            if (formData != null) {
                model.addAttribute("searchFormJs", formData.getJs());
                if (formData.getFormPosition().equals(FormPosition.up)) {
                    model.addAttribute("upSearchFormHtml", formData.getHtml());
                    model.addAttribute("downSearchFormHtml", "");
                } else {
                    model.addAttribute("downSearchFormHtml", formData.getHtml());
                    model.addAttribute("upSearchFormHtml", "");
                }
            } else {
                model.addAttribute("searchFormJs", "");
                model.addAttribute("downSearchFormHtml", "");
                model.addAttribute("upSearchFormHtml", "");
            }
            model.addAttribute("content", htmlReport.getContent());
            model.addAttribute("style", htmlReport.getStyle());
            model.addAttribute("reportAlign", htmlReport.getReportAlign());
            model.addAttribute("totalPage", htmlReport.getTotalPage());
            model.addAttribute("totalPageWithCol", htmlReport.getTotalPageWithCol());
            model.addAttribute("pageIndex", htmlReport.getPageIndex());
            model.addAttribute("chartDatas", convertJson(htmlReport.getChartDatas()));
            model.addAttribute("error", false);
            model.addAttribute("file", request.getParameter("_u"));
            model.addAttribute("intervalRefreshValue", htmlReport.getHtmlIntervalRefreshValue());
            String customParameters = buildCustomParameters(request);
            model.addAttribute("customParameters", customParameters);
            model.addAttribute("_t", "");
            Tools tools = null;
            if (MobileUtils.isMobile(request)) {
                tools = new Tools(false);
                tools.setShow(false);
            } else {
                String toolsInfo = request.getParameter("_t");
                if (StringUtils.isNotBlank(toolsInfo)) {
                    tools = new Tools(false);
                    if (toolsInfo.equals("0")) {
                        tools.setShow(false);
                    } else {
                        String[] infos = toolsInfo.split(",");
                        for (String name : infos) {
                            tools.doInit(name);
                        }
                    }
                    model.addAttribute("_t", toolsInfo);
                    model.addAttribute("hasTools", true);
                } else {
                    tools = new Tools(true);
                }
            }
            model.addAttribute("tools", tools);
        }
        return "html-preview.html";
    }

    @RequestMapping(value = "/loadPrintPages", method = RequestMethod.POST)
    @ResponseBody
    public R loadPrintPages(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String file = req.getParameter("_u");
        file = decode(file);
        if (StringUtils.isBlank(file)) {
            throw new ReportComputeException("Report file can not be null.");
        }
        Map<String, Object> parameters = buildParameters(req);
        ReportDefinition reportDefinition = null;
        if (file.equals(PREVIEW_KEY)) {
            reportDefinition = CacheUtils.getReportDefinition(PREVIEW_KEY);
            if (reportDefinition == null) {
                throw new ReportDesignException("Report data has expired,can not do export excel.");
            }
        } else {
            reportDefinition = reportRender.getReportDefinition(file);
        }
        Report report = reportBuilder.buildReport(reportDefinition, parameters);
        Map<String, ChartData> chartMap = report.getContext().getChartDataMap();
        if (chartMap.size() > 0) {
            CacheUtils.storeChartDataMap(chartMap);
        }
        FullPageData pageData = PageBuilder.buildFullPageData(report);
        StringBuilder sb = new StringBuilder();
        List<List<Page>> list = pageData.getPageList();
        Context context = report.getContext();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                List<Page> columnPages = list.get(i);
                if (i == 0) {
                    String html = htmlProducer.produce(context, columnPages, pageData.getColumnMargin(), false);
                    sb.append(html);
                } else {
                    String html = htmlProducer.produce(context, columnPages, pageData.getColumnMargin(), false);
                    sb.append(html);
                }
            }
        } else {
            List<Page> pages = report.getPages();
            for (int i = 0; i < pages.size(); i++) {
                Page page = pages.get(i);
                if (i == 0) {
                    String html = htmlProducer.produce(context, page, false);
                    sb.append(html);
                } else {
                    String html = htmlProducer.produce(context, page, true);
                    sb.append(html);
                }
            }
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("html", sb.toString());

        return R.ok().success(map);
    }

    @RequestMapping(value = "/loadPagePaper", method = RequestMethod.GET)
    @ResponseBody
    public R loadPagePaper(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String file = req.getParameter("_u");
        file = decode(file);
        if (StringUtils.isBlank(file)) {
            throw new ReportComputeException("Report file can not be null.");
        }
        ReportDefinition report = null;
        if (file.equals(PREVIEW_KEY)) {
            report = CacheUtils.getReportDefinition(PREVIEW_KEY);
            if (report == null) {
                throw new ReportDesignException("Report data has expired.");
            }
        } else {
            report = reportRender.getReportDefinition(file);
        }
        Paper paper = report.getPaper();

        return R.ok().success(paper);
    }

    @RequestMapping(value = "/loadData")
    @ResponseBody
    public R loadData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HtmlReport htmlReport = loadReport(req);

        return R.ok().success(htmlReport);
    }

    /**
     * 获取打印内容接口
     *
     * @param req
     * @return
     * @author WuXiaoChang
     * @createTime 2020年6月4日 下午1:47:04
     */
    @GetMapping("/loadPrintData")
    @ResponseBody
    public R loadPrintData(HttpServletRequest req) {
        String file = req.getParameter("_u");
        file = decode(file);
        if (StringUtils.isBlank(file)) {
            throw new ReportComputeException("Report file can not be null.");
        }
        Map<String, Object> parameters = buildParameters(req);
        HtmlReport htmlReport = exportManager.exportHtml(file, req.getContextPath(), parameters);
        Map<String, Object> data = new HashMap<>();
        data.put("style", htmlReport.getStyle());
        data.put("content", htmlReport.getContent());
        ReportDefinition report = reportRender.getReportDefinition(file);
        data.put("paper", report.getPaper());
        return new R().success(data);
    }

    private HtmlReport loadReport(HttpServletRequest req) {
        Map<String, Object> parameters = buildParameters(req);
        HtmlReport htmlReport = null;
        String file = req.getParameter("_u");
        file = decode(file);
        String pageIndex = req.getParameter("_i");
        if (StringUtils.isBlank(file)) {
            throw new ReportComputeException("Report file can not be null.");
        }
        if (file.equals(PREVIEW_KEY)) {
            ReportDefinition reportDefinition = CacheUtils.getReportDefinition(PREVIEW_KEY);
            if (reportDefinition == null) {
                throw new ReportDesignException("Report data has expired,can not do preview.");
            }
            Report report = reportBuilder.buildReport(reportDefinition, parameters);
            Map<String, ChartData> chartMap = report.getContext().getChartDataMap();
            if (chartMap.size() > 0) {
                CacheUtils.storeChartDataMap(chartMap);
            }
            htmlReport = new HtmlReport();
            String html = null;
            if (StringUtils.isNotBlank(pageIndex) && !pageIndex.equals("0")) {
                Context context = report.getContext();
                int index = Integer.valueOf(pageIndex);
                SinglePageData pageData = PageBuilder.buildSinglePageData(index, report);
                List<Page> pages = pageData.getPages();
                if (pages.size() == 1) {
                    Page page = pages.get(0);
                    html = htmlProducer.produce(context, page, false);
                } else {
                    html = htmlProducer.produce(context, pages, pageData.getColumnMargin(), false);
                }
                htmlReport.setTotalPage(pageData.getTotalPages());
                htmlReport.setPageIndex(index);
            } else {
                html = htmlProducer.produce(report);
            }
            if (report.getPaper().isColumnEnabled()) {
                htmlReport.setColumn(report.getPaper().getColumnCount());
            }
            htmlReport.setChartDatas(report.getContext().getChartDataMap().values());
            htmlReport.setContent(html);
            htmlReport.setTotalPage(report.getPages().size());
            htmlReport.setStyle(reportDefinition.getStyle());
            htmlReport.setSearchFormData(reportDefinition.buildSearchFormData(report.getContext().getDatasetMap(), parameters));
            htmlReport.setReportAlign(report.getPaper().getHtmlReportAlign().name());
            htmlReport.setHtmlIntervalRefreshValue(report.getPaper().getHtmlIntervalRefreshValue());
        } else {
            if (StringUtils.isNotBlank(pageIndex) && !pageIndex.equals("0")) {
                int index = Integer.valueOf(pageIndex);
                htmlReport = exportManager.exportHtml(file, req.getContextPath(), parameters, index);
            } else {
                htmlReport = exportManager.exportHtml(file, req.getContextPath(), parameters);
            }
        }
        return htmlReport;
    }

    protected Map<String, Object> buildParameters(HttpServletRequest req) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        Enumeration<?> enumeration = req.getParameterNames();
        while (enumeration.hasMoreElements()) {
            Object obj = enumeration.nextElement();
            if (obj == null) {
                continue;
            }
            String name = obj.toString();
            String value = req.getParameter(name);
            if (name == null || value == null || name.startsWith("_")) {
                continue;
            }
            parameters.put(name, decode(value));
        }
        return parameters;
    }

    private String buildExceptionMessage(Throwable throwable) {
        Throwable root = buildRootException(throwable);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        root.printStackTrace(pw);
        String trace = sw.getBuffer().toString();
        trace = trace.replaceAll("\n", "<br>");
        pw.close();
        return trace;
    }

    private String buildCustomParameters(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        Enumeration<?> enumeration = req.getParameterNames();
        while (enumeration.hasMoreElements()) {
            Object obj = enumeration.nextElement();
            if (obj == null) {
                continue;
            }
            String name = obj.toString();
            String value = req.getParameter(name);
            if (name == null || value == null || (name.startsWith("_") && !name.equals("_n"))) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(name);
            sb.append("=");
            sb.append(value);
        }
        return sb.toString();
    }

    private String convertJson(Collection<ChartData> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(data);
            return json;
        } catch (Exception e) {
            throw new ReportComputeException(e);
        }
    }
}
