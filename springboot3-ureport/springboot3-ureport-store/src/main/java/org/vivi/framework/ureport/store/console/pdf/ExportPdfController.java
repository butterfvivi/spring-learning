/*******************************************************************************
 * Copyright 2017 Bstek
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.vivi.framework.ureport.store.console.pdf;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.vivi.framework.ureport.store.console.AbstractReportBasicController;
import org.vivi.framework.ureport.store.console.exception.ReportDesignException;
import org.vivi.framework.ureport.store.core.build.ReportBuilder;
import org.vivi.framework.ureport.store.core.cache.CacheUtils;
import org.vivi.framework.ureport.store.core.definition.Paper;
import org.vivi.framework.ureport.store.core.definition.ReportDefinition;
import org.vivi.framework.ureport.store.core.exception.ReportComputeException;
import org.vivi.framework.ureport.store.core.exception.ReportException;
import org.vivi.framework.ureport.store.core.export.ExportConfigure;
import org.vivi.framework.ureport.store.core.export.ExportConfigureImpl;
import org.vivi.framework.ureport.store.core.export.ExportManager;
import org.vivi.framework.ureport.store.core.export.ReportRender;
import org.vivi.framework.ureport.store.core.export.pdf.PdfProducer;
import org.vivi.framework.ureport.store.core.model.Report;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


/**
 * @author summer
 * @Date: 2022/1/14
 * Description: pdf控制器
 */
@Controller
@RequestMapping(value = "/pdf")
public class ExportPdfController extends AbstractReportBasicController {
    @Autowired
    private ReportBuilder reportBuilder;
    @Autowired
    private ExportManager exportManager;
    @Autowired
    private ReportRender reportRender;

    private PdfProducer pdfProducer = new PdfProducer();

    /**
     * 导出pdf
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public void export(HttpServletRequest req, HttpServletResponse resp) {
        try {
            buildPdf(req, resp, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在线预览pdf
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public void show(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        buildPdf(req, resp, true);
    }

    public void buildPdf(HttpServletRequest req, HttpServletResponse resp, boolean forPrint) throws IOException {
        String file = req.getParameter("_u");
        file = decode(file);
        if (StringUtils.isBlank(file)) {
            throw new ReportComputeException("Report file can not be null.");
        }
        OutputStream outputStream = null;
        try {
            String fileName = req.getParameter("_n");
            fileName = buildDownloadFileName(file, fileName, ".pdf");
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            if (forPrint) {
                resp.setContentType("application/pdf");
                resp.setHeader("Content-Disposition", "inline;filename=\"" + fileName + "\"");
            } else {
                resp.setContentType("application/octet-stream;charset=ISO8859-1");
                resp.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            }
            outputStream = resp.getOutputStream();
            Map<String, Object> parameters = buildParameters(req);
            if (file.equals(PREVIEW_KEY)) {
                ReportDefinition reportDefinition = CacheUtils.getReportDefinition(PREVIEW_KEY);
                if (reportDefinition == null) {
                    throw new ReportDesignException("Report data has expired,can not do export pdf.");
                }
                Report report = reportBuilder.buildReport(reportDefinition, parameters);
                pdfProducer.produce(report, outputStream);
            } else {
                ExportConfigure configure = new ExportConfigureImpl(file, parameters, outputStream);
                exportManager.exportPdf(configure);
            }
        } catch (Exception ex) {
            throw new ReportException(ex);
        } finally {
            outputStream.flush();
            outputStream.close();
        }
    }

    @RequestMapping(value = "/newPaging", method = RequestMethod.POST)
    public void newPaging(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String file = req.getParameter("_u");
        if (StringUtils.isBlank(file)) {
            throw new ReportComputeException("Report file can not be null.");
        }
        Report report = null;
        Map<String, Object> parameters = buildParameters(req);
        if (file.equals(PREVIEW_KEY)) {
            ReportDefinition reportDefinition = CacheUtils.getReportDefinition(PREVIEW_KEY);
            if (reportDefinition == null) {
                throw new ReportDesignException("Report data has expired,can not do export pdf.");
            }
            report = reportBuilder.buildReport(reportDefinition, parameters);
        } else {
            ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
            report = reportRender.render(reportDefinition, parameters);
        }
        String paper = req.getParameter("_paper");
        ObjectMapper mapper = new ObjectMapper();
        Paper newPaper = mapper.readValue(paper, Paper.class);
        report.rePaging(newPaper);
    }

    public void setReportRender(ReportRender reportRender) {
        this.reportRender = reportRender;
    }

    public void setExportManager(ExportManager exportManager) {
        this.exportManager = exportManager;
    }

    public void setReportBuilder(ReportBuilder reportBuilder) {
        this.reportBuilder = reportBuilder;
    }

}
