package org.vivi.framework.ureport.store.console.designer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.vivi.framework.ureport.store.console.AbstractReportBasicController;
import org.vivi.framework.ureport.store.console.common.R;
import org.vivi.framework.ureport.store.console.exception.ReportDesignException;
import org.vivi.framework.ureport.store.core.cache.CacheUtils;
import org.vivi.framework.ureport.store.core.definition.ReportDefinition;
import org.vivi.framework.ureport.store.core.dsl.ReportParserLexer;
import org.vivi.framework.ureport.store.core.dsl.ReportParserParser;
import org.vivi.framework.ureport.store.core.export.ReportRender;
import org.vivi.framework.ureport.store.core.expression.ErrorInfo;
import org.vivi.framework.ureport.store.core.expression.ScriptErrorListener;
import org.vivi.framework.ureport.store.core.init.ReportProvidersInit;
import org.vivi.framework.ureport.store.core.parser.ReportParser;
import org.vivi.framework.ureport.store.core.provider.report.ReportProvider;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @RequestMapping(value = "/designer", method = RequestMethod.GET)
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
                reportProvidersInit.getReportProviders().stream().filter(r -> r.getName() != null && r.disabled()).collect(Collectors.toList())
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

        String name = req.getParameter("name");
        name = ReportUtils.decodeFileName(name);


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
        targetReportProvider.saveReport(file, content,name);
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

}
