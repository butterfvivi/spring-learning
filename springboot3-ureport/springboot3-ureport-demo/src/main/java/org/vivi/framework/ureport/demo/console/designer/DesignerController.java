package org.vivi.framework.ureport.demo.console.designer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.ureport.demo.console.AbstractReportBasicController;
import org.vivi.framework.ureport.demo.console.common.R;
import org.vivi.framework.ureport.demo.console.exception.ReportDesignException;
import org.vivi.framework.ureport.demo.core.cache.CacheUtils;
import org.vivi.framework.ureport.demo.core.definition.ReportDefinition;
import org.vivi.framework.ureport.demo.core.dsl.ReportParserLexer;
import org.vivi.framework.ureport.demo.core.dsl.ReportParserParser;
import org.vivi.framework.ureport.demo.core.export.ReportRender;
import org.vivi.framework.ureport.demo.core.expression.ErrorInfo;
import org.vivi.framework.ureport.demo.core.expression.ScriptErrorListener;
import org.vivi.framework.ureport.demo.core.init.ReportProvidersInit;
import org.vivi.framework.ureport.demo.core.parser.ReportParser;
import org.vivi.framework.ureport.demo.core.provider.report.ReportProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description: 设计器控制器
 **/
@Slf4j
@Controller
@RequestMapping(value = "/designer")
public class DesignerController extends AbstractReportBasicController {

    @Autowired
    private ReportRender reportRender;

    @Autowired
    private ReportParser reportParser;

    @Autowired
    private ReportProvidersInit reportProvidersInit;

    /**
     * 报表设计器首页
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String designer(HttpServletRequest request, Model model) {
        //application
        model.addAttribute("contextPath", request.getContextPath());
        return "designer";
    }

    /**
     * 加载报表表格
     *
     * @param request
     */
    @RequestMapping(value = "/loadReport", method = RequestMethod.POST)
    @ResponseBody
    public R loadReportTable(HttpServletRequest request, HttpServletResponse response) {
        String file = request.getParameter("file");
        if (file == null) {
            throw new ReportDesignException("Report file can not be null.");
        }

        file = ReportUtils.decodeFileName(file);
        ReportDefinition reportDefinition = CacheUtils.getReportDefinition(file);
        try {
            if (reportDefinition != null) {
                CacheUtils.removeReportDefinition(file);
            } else {
                reportDefinition = reportRender.parseReport(file);
            }
            return R.ok().success(Optional.ofNullable(reportDefinition).map(r -> new ReportDefinitionWrapper(r)).orElse(null));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 加载报表表格
     */
    @RequestMapping(value = "/savePreviewData", method = RequestMethod.POST)
    @ResponseBody
    public R savePreviewData(HttpServletRequest req) {
        String content = req.getParameter("content");
        String fileName = req.getParameter("_u");
        content = decodeContent(content);
        InputStream inputStream = null;
        inputStream = IOUtils.toInputStream(content, "utf-8");
        ReportDefinition reportDef = reportParser.parse(inputStream, fileName);
        reportRender.rebuildReportDefinition(reportDef);
        IOUtils.closeQuietly(inputStream);

        try {
            CacheUtils.cacheReportDefinition(fileName, reportDef);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return R.ok();
    }

    /**
     * 加载报表
     *
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/loadReportProviders", method = RequestMethod.GET)
    @ResponseBody
    public R loadReportProviders() throws ServletException, IOException {
        return R.ok().success(
                reportProvidersInit.getReportProviders().stream().filter(r -> r.getName() != null && r.enabled()).collect(Collectors.toList())
        );
    }

    /**
     * 保存报表文件
     *
     * @param req
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/saveReportFile", method = RequestMethod.POST)
    @ResponseBody
    public R saveReportFile(HttpServletRequest req) throws ServletException, IOException {
        String file = req.getParameter("file");
        file = ReportUtils.decodeFileName(file);
        String content = req.getParameter("content");
        content = decodeContent(content);
        ReportProvider targetReportProvider = null;
        for (ReportProvider provider : reportProvidersInit.getReportProviders()) {
            if (file.startsWith(provider.getPrefix())) {
                targetReportProvider = provider;
                break;
            }
        }
        if (targetReportProvider == null) {
            throw new ReportDesignException("File [" + file + "] not found available report provider.");
        }
        targetReportProvider.saveReport(file, content);
        InputStream inputStream = IOUtils.toInputStream(content, "utf-8");
        ReportDefinition reportDef = reportParser.parse(inputStream, file);
        reportRender.rebuildReportDefinition(reportDef);
        CacheUtils.cacheReportDefinition(file, reportDef);
        IOUtils.closeQuietly(inputStream);

        return R.ok();
    }

    /**
     * 表达式合法校验
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/scriptValidation", method = RequestMethod.POST)
    @ResponseBody
    public R scriptValidation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String content = req.getParameter("content");
        ANTLRInputStream antlrInputStream = new ANTLRInputStream(content);
        ReportParserLexer lexer = new ReportParserLexer(antlrInputStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ReportParserParser parser = new ReportParserParser(tokenStream);
        ScriptErrorListener errorListener = new ScriptErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        parser.expression();
        List<ErrorInfo> infos = errorListener.getInfos();

        return R.ok().success(infos);
    }

    /**
     * 过滤条件表达式合法校验
     *
     * @param req
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/conditionScriptValidation", method = RequestMethod.POST)
    @ResponseBody
    public R conditionScriptValidation(HttpServletRequest req) throws ServletException, IOException {
        String content = req.getParameter("content");
        ANTLRInputStream antlrInputStream = new ANTLRInputStream(content);
        ReportParserLexer lexer = new ReportParserLexer(antlrInputStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ReportParserParser parser = new ReportParserParser(tokenStream);
        ScriptErrorListener errorListener = new ScriptErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        parser.expr();
        List<ErrorInfo> infos = errorListener.getInfos();

        return R.ok().success(infos);
    }

    @RequestMapping(value = "/parseDatasetName", method = RequestMethod.POST)
    @ResponseBody
    public R parseDatasetName(HttpServletRequest req) throws ServletException, IOException {
        String expr = req.getParameter("expr");
        ANTLRInputStream antlrInputStream = new ANTLRInputStream(expr);
        ReportParserLexer lexer = new ReportParserLexer(antlrInputStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ReportParserParser parser = new ReportParserParser(tokenStream);
        parser.removeErrorListeners();
        ReportParserParser.DatasetContext ctx = parser.dataset();
        String datasetName = ctx.Identifier().getText();
        Map<String, String> result = new HashMap<String, String>();
        result.put("datasetName", datasetName);

        return R.ok().success(result);
    }

    @RequestMapping(value = "/deleteReportFile", method = RequestMethod.POST)
    @ResponseBody
    public R deleteReportFile(HttpServletRequest req) throws ServletException, IOException {
        String file = req.getParameter("file");
        if (file == null) {
            throw new ReportDesignException("Report file can not be null.");
        }
        ReportProvider targetReportProvider = null;
        for (ReportProvider provider : reportProvidersInit.getReportProviders()) {
            if (file.startsWith(provider.getPrefix())) {
                targetReportProvider = provider;
                break;
            }
        }
        if (targetReportProvider == null) {
            throw new ReportDesignException("File [" + file + "] not found available report provider.");
        }
        targetReportProvider.deleteReport(file);

        return R.ok();
    }


    /**
     * 下载报表
     *
     * @param req      请求
     * @param response 响应
     */
    @RequestMapping(value = "/downloadReportFile", method = RequestMethod.GET)
    public void downloadReport(HttpServletRequest req, HttpServletResponse response) {
        String file = req.getParameter("file");
        if (file == null) {
            throw new ReportDesignException("Report file can not be null.");
        }
        ReportProvider targetReportProvider = null;
        String fileName = null;
        for (ReportProvider provider : reportProvidersInit.getReportProviders()) {
            if (file.startsWith(provider.getPrefix())) {
                targetReportProvider = provider;
                if (file.startsWith(provider.getPrefix())) {
                    fileName = file.substring(provider.getPrefix().length(), file.length());
                }
                break;
            }
        }
        if (targetReportProvider == null) {
            throw new ReportDesignException("File [" + file + "] not found available report provider.");
        }

        // 文件解码
        try {
            file = URLDecoder.decode(file, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("文件解码失败", e);
        }

        // 获取文件流
        InputStream stream = targetReportProvider.loadReport(file);

        // 1、设置response 响应头
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");

        // 写出文件--输出流
        try (OutputStream out = response.getOutputStream()) {
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            byte[] buff = new byte[1024];
            int index = 0;
            // 执行 写出操作
            while ((index = stream.read(buff)) != -1) {
                out.write(buff, 0, index);
                out.flush();
            }
        } catch (IOException e) {
            log.error("写数据失败：", e);
            throw new ReportDesignException("File [" + fileName + "] download fail.");
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                log.error("关闭流失败：", e);
            }
        }

    }

    /**
     * 上传报表，同名则被覆盖
     *
     * @param providerName 报表来源
     * @param files        文件
     */
    @RequestMapping(value = "/uploadReportFile", method = RequestMethod.POST)
    @ResponseBody
    public R uploadReportFile(@RequestParam String providerName, @RequestParam("file") MultipartFile[] files) {
        if (StringUtils.isBlank(providerName)) {
            throw new ReportDesignException("Report provider name can not be null.");
        }
        ReportProvider targetReportProvider = null;
        for (ReportProvider provider : reportProvidersInit.getReportProviders()) {
            if ((providerName).equals(provider.getPrefix())) {
                targetReportProvider = provider;
                break;
            }
        }
        if (targetReportProvider == null) {
            throw new ReportDesignException("Provider name [" + providerName + "] not found available report provider.");
        }
        // 保存报表文件
        try {
            for (MultipartFile file : files) {
                byte[] bytes = file.getBytes();
                String fileName = file.getOriginalFilename();
                String content = new String(bytes, "utf-8");
                targetReportProvider.saveReport(fileName, content);
            }
        } catch (IOException e) {
            throw new ReportDesignException("upload report file fail!\r\n" + e.getMessage());
        }
        return R.ok("上传成功！");
    }

}
