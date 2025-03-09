package org.vivi.framework.report.service.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfType;
import org.vivi.framework.report.service.common.base.MesExportExcel;
import org.vivi.framework.report.service.common.enums.YesNoEnum;
import org.vivi.framework.report.service.common.utils.*;
import org.vivi.framework.report.service.common.utils.DateUtil;

import java.awt.*;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;

public class HandleExcelUtils {

    /**
     * @MethodName: export
     * @Description: 导出excel文件
     */
    public static void export(MesExportExcel mesExportExcel, String filename, String password, HttpServletResponse httpServletResponse) throws Exception
    {
        httpServletResponse.setContentType("octets/stream");
        //设置文件名编码格式
        filename = URLEncoder.encode(filename, "UTF-8");
        httpServletResponse.addHeader("Content-Disposition", "attachment;filename=" +filename + ".xlsx");
        httpServletResponse.addHeader("filename", filename + ".xlsx");
        XSSFWorkbook wb = new XSSFWorkbook();
        if(!ListUtil.isEmpty(mesExportExcel.getSheetConfigs()))
        {
            for (int i = 0; i < mesExportExcel.getSheetConfigs().size(); i++) {
                List<Map<String, Object>> cellDatas = mesExportExcel.getSheetConfigs().get(i).getCellDatas();
                Map<String, Integer> maxXAndY = mesExportExcel.getSheetConfigs().get(i).getMaxXAndY();
                Map<String, Map<String, Object>> hyperlinks = mesExportExcel.getSheetConfigs().get(i).getHyperlinks();
                List<Object> borderInfos = mesExportExcel.getSheetConfigs().get(i).getBorderInfos();
                Map<String, Object> rowlen = mesExportExcel.getSheetConfigs().get(i).getRowlen();
                Map<String, Object> columnlen = mesExportExcel.getSheetConfigs().get(i).getColumnlen();
                JSONObject frozen = mesExportExcel.getSheetConfigs().get(i).getFrozen();
                JSONObject images = mesExportExcel.getSheetConfigs().get(i).getBase64Images();
                List<JSONObject> imageDatas = mesExportExcel.getSheetConfigs().get(i).getImageDatas();
                String sheetname = mesExportExcel.getSheetConfigs().get(i).getSheetname();
                JSONObject colhidden = mesExportExcel.getSheetConfigs().get(i).getColhidden();
                JSONObject rowhidden = mesExportExcel.getSheetConfigs().get(i).getRowhidden();
                JSONObject dataVerification = mesExportExcel.getSheetConfigs().get(i).getDataVerification();
                JSONObject authority = mesExportExcel.getSheetConfigs().get(i).getAuthority();
                JSONObject filter = mesExportExcel.getSheetConfigs().get(i).getFilter();
                JSONArray xxbtCells = new JSONArray();//斜线表头单元格
                XSSFSheet sheet = wb.createSheet(sheetname);
                List<String> noViewAuthCells = mesExportExcel.getSheetConfigs().get(i).getNoViewAuthCells();
                if(!StringUtil.isEmptyMap(filter))
                {
                    String filterRange = getFilterRange(filter);
                    CellRangeAddress c = CellRangeAddress.valueOf(filterRange);
                    sheet.setAutoFilter(c);
                }

                sheet.setForceFormulaRecalculation(true);
                Map<String, String> unProtectCells = null;
                if(authority != null && authority.getIntValue("sheet") == 1)
                {
                    unProtectCells = getUnProtectCells(authority);
                }
                if(colhidden != null && !colhidden.isEmpty())
                {
                    for(String key:colhidden.keySet())
                    {
                        int value = colhidden.getIntValue(key);
                        if(value == 0)
                        {
                            int column = Integer.parseInt(key);
                            sheet.setColumnHidden(column, true);
                        }

                    }
                }
                if(frozen != null)
                {
                    String frozenType = frozen.getString("type");
                    int row = 0;
                    int column = 0;
                    if(StringUtil.isNotEmpty(frozenType))
                    {
                        switch (frozenType) {
                            case "row":
                                sheet.createFreezePane(0, 1);
                                break;
                            case "column":
                                sheet.createFreezePane(1, 0);
                                break;
                            case "both":
                                sheet.createFreezePane(1, 1);
                                break;
                            case "rangeRow":
                                row = frozen.getJSONObject("range").getIntValue("row_focus");
                                sheet.createFreezePane(0, row+1);
                                break;
                            case "rangeColumn":
                                column = frozen.getJSONObject("range").getIntValue("column_focus");
                                sheet.createFreezePane(column+1,0);
                            case "rangeBoth":
                                row = frozen.getJSONObject("range").getIntValue("row_focus");
                                column = frozen.getJSONObject("range").getIntValue("column_focus");
                                sheet.createFreezePane(column+1, row+1);
                            default:
                                break;
                        }
                    }
                }
//        		sheet.setRandomAccessWindowSize(-1);
                LuckySheetCellUtil cellUtil = new LuckySheetCellUtil(wb, sheet);
                cellUtil.createCells(maxXAndY.get("maxX"), maxXAndY.get("maxY"),rowlen,rowhidden);
                if(columnlen != null)
                {
                    for (Map.Entry<String, Object> entry : columnlen.entrySet()) {
                        BigDecimal wid = new BigDecimal(String.valueOf(entry.getValue()));
                        BigDecimal excleWid=new BigDecimal(32);
                        sheet.setColumnWidth(Integer.parseInt(String.valueOf(entry.getKey())), wid.multiply(excleWid).setScale(0,BigDecimal.ROUND_HALF_UP).intValue());//列宽px值
                    }
                }
                JSONArray barCodeCells = new JSONArray();//条形码单元格
                JSONArray qrCodeCells = new JSONArray();//二维码单元格
                Map<String, Integer> wrapText = new HashMap<>();
                cellUtil.setCellValues(cellDatas,hyperlinks,borderInfos,unProtectCells,mesExportExcel.getSheetConfigs().get(i).getMerge(),mesExportExcel.getSheetConfigs().get(i).getIsCoedit(), dataVerification,xxbtCells,false,barCodeCells,qrCodeCells,wrapText,noViewAuthCells);
                cellUtil.setRowHeight(maxXAndY.get("maxX"), rowlen, rowhidden, wrapText);
                JSONObject rowlenObj = JSONObject.parseObject(JSONObject.toJSONString(rowlen));
                JSONObject columnlenObj = JSONObject.parseObject(JSONObject.toJSONString(columnlen));
                insertChart(wb, sheet, mesExportExcel.getSheetConfigs().get(i).getChart(),mesExportExcel.getSheetConfigs().get(i).getChartCells(),mesExportExcel.getSheetConfigs().get(i).getIsCoedit(),rowlenObj,rowhidden,columnlenObj,colhidden);
                addDataVerification(sheet, dataVerification);
                if(authority != null && authority.getIntValue("sheet") == 1)
                {
                    sheet.protectSheet(StringUtil.isNotEmpty(authority.getString("password"))?authority.getString("password"):"");
                }
                if(!ListUtil.isEmpty(xxbtCells))
                {
                    List<SlashLinePosition> slashes = new ArrayList<>();
                    List<SlashLineText> slashTexts = new ArrayList<>();
                    for (int j = 0; j < xxbtCells.size(); j++) {
                        JSONObject cellData = xxbtCells.getJSONObject(j);
                        int r = cellData.getIntValue("r");
                        int c = cellData.getIntValue("c");
                        int rs = 1;
                        int cs = 1;
                        String cellValue = "";
                        if(cellData.getJSONObject("v") != null)
                        {
                            JSONObject v = cellData.getJSONObject("v");
                            if(v.containsKey("mc"))
                            {
                                rs = v.getJSONObject("mc").getInteger("rs") != null?v.getJSONObject("mc").getInteger("rs"):1;
                                cs = v.getJSONObject("mc").getInteger("cs") != null?v.getJSONObject("mc").getInteger("cs"):1;
                            }
                            cellValue = v.getString("v");
                        }
                        getSlashLinePositionXlsx(sheet,r,c,rs,cs,cellValue,slashes,slashTexts);
                    }
                    drawLineXlsx(sheet,slashes,slashTexts);
                }

                processSheetConditionFormat(sheet, mesExportExcel.getSheetConfigs().get(i).getLuckysheetConditionformatSave());
            }
        }
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        try {
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void drawLineXlsx(XSSFSheet xssfSheet,List<SlashLinePosition> slashes,
                                     List<SlashLineText> slashLineTexts) {
        XSSFDrawing xssfDrawing  = xssfSheet.createDrawingPatriarch();
        for(SlashLinePosition slp:slashes)
        {
            XSSFClientAnchor xssfClientAnchor = new XSSFClientAnchor(slp.getStartX(), slp.getStartY(), slp.getEndX(), slp.getEndY(), slp.getC(), slp.getR(), slp.getEndc(), slp.getEndr());
            XSSFSimpleShape shape = xssfDrawing.createSimpleShape(xssfClientAnchor);
            shape.setShapeType(ShapeTypes.LINE);
            shape.setLineWidth(0.5);
            shape.setLineStyle(0);
            shape.setLineStyleColor(0, 0, 0);
        }
        for(SlashLineText slt:slashLineTexts) {
            XSSFClientAnchor createAnchor = xssfDrawing.createAnchor(slt.getStartX(), slt.getStartY(), slt.getEndX(), slt.getEndY(), slt.getC(), slt.getR(), slt.getC(), slt.getR());
            XSSFTextBox tb1 = xssfDrawing.createTextbox(createAnchor);
            XSSFRichTextString richTextString = new XSSFRichTextString(slt.getContent());
            tb1.setText(richTextString);
        }
    }

    /**
     * @MethodName: getFilterRange
     * @Description: 获取筛选条件范围
     */
    private static String getFilterRange(JSONObject filter)
    {
        JSONArray row = filter.getJSONArray("row");
        JSONArray column = filter.getJSONArray("column");
        int startr = row.getIntValue(0);
        int endr = row.getIntValue(1);
        int startc = column.getIntValue(0);
        int endc = column.getIntValue(1);
        String startcName = SheetUtil.excelColIndexToStr(startc+1);
        String endcName = SheetUtil.excelColIndexToStr(endc+1);
        String result = startcName+(startr+1)+":"+endcName+(endr+1);
        return result;
    }

    /**
     * @MethodName: getUnProtectCells
     * @Description: 获取不受保护的单元格
     */
    private static Map<String, String> getUnProtectCells(JSONObject authority){
        Map<String, String> result = null;
        JSONArray allowRangeList = authority.getJSONArray("allowRangeList");
        if(!ListUtil.isEmpty(allowRangeList))
        {
            result = new HashMap<>();
            for (int i = 0; i < allowRangeList.size(); i++) {
                String sqref = allowRangeList.getJSONObject(i).getString("sqref");
                int c = 0;
                int r = 0;
                int endc = 0;
                int endr = 0;
                if(sqref.contains(":"))
                {
                    String[] sqrefs = sqref.split(":");
                    c = SheetUtil.excelColStrToNum(SheetUtil.getColumnFlag(sqrefs[0].replaceAll("\\$", "")))-1;
                    r = SheetUtil.getRowNum(sqrefs[0].replaceAll("\\$", ""))-1;
                    endc = SheetUtil.excelColStrToNum(SheetUtil.getColumnFlag(sqrefs[1].replaceAll("\\$", "")))-1;
                    endr = SheetUtil.getRowNum(sqrefs[1].replaceAll("\\$", ""))-1;
                }else {
                    c = SheetUtil.excelColStrToNum(SheetUtil.getColumnFlag(sqref.replaceAll("\\$", "")))-1;
                    r = SheetUtil.getRowNum(sqref.replaceAll("\\$", ""))-1;
                    endc = c;
                    endr = r;
                }
                for (int j = r; j <= endr; j++) {
                    for (int j2 = c; j2 <= endc; j2++) {
                        result.put(j+"_"+j2, j+"_"+j2);
                    }
                }
            }
        }
        return result;
    }

    /**
     * @MethodName: processSheetConditionFormat
     * @Description: 条件格式处理
     */
    private static void processSheetConditionFormat(XSSFSheet sheet,JSONArray conditionFormats) throws Exception {
        if(ListUtil.isNotEmpty(conditionFormats)) {
            for (int i = 0; i < conditionFormats.size(); i++) {
                String type = conditionFormats.getJSONObject(i).getString("type");
                if("default".equals(type)) {
                    String conditionName = conditionFormats.getJSONObject(i).getString("conditionName");
                    JSONArray conditionValue = conditionFormats.getJSONObject(i).getJSONArray("conditionValue");
                    JSONObject format = conditionFormats.getJSONObject(i).getJSONObject("format");
                    for (int j = 0; j < conditionFormats.getJSONObject(i).getJSONArray("cellrange").size(); j++) {
                        JSONArray row = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("row");
                        JSONArray column = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("column");
                        ConditionalFormattingRule rule = null;
                        if("lessThan".equals(conditionName)) {
                            rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(ComparisonOperator.LT, conditionValue.getString(0));
                        }else if("greaterThan".equals(conditionName)) {
                            rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(ComparisonOperator.GT, conditionValue.getString(0));
                        }else if("equal".equals(conditionName)) {
                            rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(ComparisonOperator.EQUAL, conditionValue.getString(0));
                        }else if("betweenness".equals(conditionName)) {
                            rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(ComparisonOperator.BETWEEN, conditionValue.getString(0),conditionValue.getString(1));
                        }else if("textContains".equals(conditionName)) {
                            //字符串包含
                            String col = SheetUtil.excelColIndexToStr(column.getIntValue(0)+1);
                            String ruleStr = "NOT(ISERROR(SEARCH(\"" + conditionValue.getString(0) + "\","+(col+(row.getIntValue(0)+1))+")))";
                            rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(ruleStr);
                        }else if("occurrenceDate".equals(conditionName)) {
                            //发生日期，暂不支持 TODO
                        }else if("duplicateValue".equals(conditionName)) {
                            //重复值
                            rule = createConditionalFormattingRuleDuplicate(sheet.getSheetConditionalFormatting());
                        }else if("top10".equals(conditionName) || "top10%".equals(conditionName) || "last10%".equals(conditionName)
                                || "last10".equals(conditionName) || "AboveAverage".equals(conditionName) || "SubAverage".equals(conditionName)) {
                            rule = createConditionalFormattingRuleRank(sheet.getSheetConditionalFormatting(),conditionName,conditionValue);
                        }
                        if(rule != null) {
                            String cellColor = format.getString("cellColor");
                            int[] rgb = null;
                            if(cellColor.contains("rgb")) {
                                rgb = StringUtil.rgbStringToRgb(cellColor);
                            }else {
                                rgb = StringUtil.hexToRgb(cellColor);
                            }
                            PatternFormatting patternFormatting = rule.createPatternFormatting();
                            patternFormatting.setFillBackgroundColor(new XSSFColor(new Color(rgb[0], rgb[1], rgb[2]),new DefaultIndexedColorMap()));
                            FontFormatting fontFormatting = rule.createFontFormatting();
                            String textColor = format.getString("textColor");
                            if(textColor.contains("rgb")) {
                                rgb = StringUtil.rgbStringToRgb(textColor);
                            }else {
                                rgb = StringUtil.hexToRgb(textColor);
                            }
                            fontFormatting.setFontColor(new XSSFColor(new Color(rgb[0], rgb[1], rgb[2]),new DefaultIndexedColorMap()));
                            CellRangeAddress[] regions = {
                                    new CellRangeAddress(row.getIntValue(0), row.getIntValue(1), column.getIntValue(0), column.getIntValue(1))
                            };
                            sheet.getSheetConditionalFormatting().addConditionalFormatting(regions,rule);
                        }
                    }

                }else if("dataBar".equals(type)) {
                    //数据条
                    JSONArray format = conditionFormats.getJSONObject(i).getJSONArray("format");
                    int[] rgb = null;
                    if(format.getString(0).contains("rgb")) {
                        rgb = StringUtil.rgbStringToRgb(format.getString(0));
                    }else {
                        rgb = StringUtil.hexToRgb(format.getString(0));
                    }
                    ConditionalFormattingRule rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(new XSSFColor(new Color(rgb[0], rgb[1], rgb[2]),new DefaultIndexedColorMap()));
                    XSSFDataBarFormatting dataBarFormatting = (XSSFDataBarFormatting) rule.getDataBarFormatting();
                    CellRangeAddress[] regions = new CellRangeAddress[conditionFormats.getJSONObject(i).getJSONArray("cellrange").size()];
                    for (int j = 0; j < conditionFormats.getJSONObject(i).getJSONArray("cellrange").size(); j++) {
                        JSONArray row = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("row");
                        JSONArray column = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("column");
                        regions[j] = new CellRangeAddress(row.getIntValue(0), row.getIntValue(1), column.getIntValue(0), column.getIntValue(1));
                    }
                    sheet.getSheetConditionalFormatting().addConditionalFormatting(regions,rule);

                }else if("colorGradation".equals(type)) {
                    //色阶
                    JSONArray format = conditionFormats.getJSONObject(i).getJSONArray("format");
                    ConditionalFormattingRule rule = sheet.getSheetConditionalFormatting().createConditionalFormattingColorScaleRule();
                    ColorScaleFormatting colorScaleFormatting = rule.getColorScaleFormatting();
                    colorScaleFormatting.setNumControlPoints(format.size());//设置几色阶
                    if(format.size() ==3) {//三色阶
                        //设置最小色阶
                        colorScaleFormatting.getThresholds()[0].setRangeType(org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType.MIN);
                        // 设置两个颜色递进的形式 此处为百分比
                        colorScaleFormatting.getThresholds()[1].setRangeType(org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType.PERCENTILE);
                        colorScaleFormatting.getThresholds()[1].setValue(50d);
                        // 设置最大色阶
                        colorScaleFormatting.getThresholds()[2].setRangeType(org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType.MAX);
                        String color = format.getString(2);
                        if(color.contains("rgb")) {
                            int[] rgb = StringUtil.rgbStringToRgb(color);
                            color = StringUtil.rgb2Hex(rgb[0], rgb[1], rgb[2]);
                        }
                        ((ExtendedColor)colorScaleFormatting.getColors()[0]).setARGBHex(color.replaceAll("#", ""));
                        color = format.getString(1);
                        if(color.contains("rgb")) {
                            int[] rgb = StringUtil.rgbStringToRgb(color);
                            color = StringUtil.rgb2Hex(rgb[0], rgb[1], rgb[2]);
                        }
                        ((ExtendedColor)colorScaleFormatting.getColors()[1]).setARGBHex(color.replaceAll("#", ""));
                        color = format.getString(0);
                        if(color.contains("rgb")) {
                            int[] rgb = StringUtil.rgbStringToRgb(color);
                            color = StringUtil.rgb2Hex(rgb[0], rgb[1], rgb[2]);
                        }
                        ((ExtendedColor)colorScaleFormatting.getColors()[2]).setARGBHex(color.replaceAll("#", ""));
                    }else if(format.size() ==2) {
                        //设置最小色阶
                        colorScaleFormatting.getThresholds()[0].setRangeType(org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType.MIN);
                        // 设置最大色阶
                        colorScaleFormatting.getThresholds()[1].setRangeType(org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType.MAX);
                        String color = format.getString(1);
                        if(color.contains("rgb")) {
                            int[] rgb = StringUtil.rgbStringToRgb(color);
                            color = StringUtil.rgb2Hex(rgb[0], rgb[1], rgb[2]);
                        }
                        ((ExtendedColor)colorScaleFormatting.getColors()[0]).setARGBHex(color.replaceAll("#", ""));
                        color = format.getString(0);
                        if(color.contains("rgb")) {
                            int[] rgb = StringUtil.rgbStringToRgb(color);
                            color = StringUtil.rgb2Hex(rgb[0], rgb[1], rgb[2]);
                        }
                        ((ExtendedColor)colorScaleFormatting.getColors()[1]).setARGBHex(color.replaceAll("#", ""));
                    }
                    CellRangeAddress[] regions = new CellRangeAddress[conditionFormats.getJSONObject(i).getJSONArray("cellrange").size()];
                    for (int j = 0; j < conditionFormats.getJSONObject(i).getJSONArray("cellrange").size(); j++) {
                        JSONArray row = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("row");
                        JSONArray column = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("column");
                        regions[j] = new CellRangeAddress(row.getIntValue(0), row.getIntValue(1), column.getIntValue(0), column.getIntValue(1));
                    }

                    sheet.getSheetConditionalFormatting().addConditionalFormatting(regions,rule);
                }else if("icons".equals(type)) {
                    //图标集
                    JSONObject format = conditionFormats.getJSONObject(i).getJSONObject("format");
                    ConditionalFormattingRule rule = createConditionalFormattingRuleIconSets(sheet.getSheetConditionalFormatting(),format);
                    CellRangeAddress[] regions = new CellRangeAddress[conditionFormats.getJSONObject(i).getJSONArray("cellrange").size()];
                    for (int j = 0; j < conditionFormats.getJSONObject(i).getJSONArray("cellrange").size(); j++) {
                        JSONArray row = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("row");
                        JSONArray column = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("column");
                        regions[j] = new CellRangeAddress(row.getIntValue(0), row.getIntValue(1), column.getIntValue(0), column.getIntValue(1));
                    }
                    sheet.getSheetConditionalFormatting().addConditionalFormatting(regions,rule);
                }
            }
        }
    }


    public static void insertChart(XSSFWorkbook wb,XSSFSheet sheet,JSONArray jsonArray,JSONObject chartCells,int isCoedit,JSONObject rowlen,JSONObject rowhidden,JSONObject columnlen,JSONObject colhidden) throws IOException
    {
        if(!ListUtil.isEmpty(jsonArray))
        {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String chartId = jsonObject.getString("chart_id");
                JSONObject chartOptions = jsonObject.getJSONObject("chartOptions");
                JSONObject defaultOption = chartOptions.getJSONObject("defaultOption");
                JSONObject specObj = chartOptions.getJSONObject("defaultOption").getJSONObject("spec");
                if (Objects.isNull(specObj) ){
                    continue;
                }
                JSONArray values = chartOptions.getJSONObject("defaultOption").getJSONObject("spec").getJSONObject("data").getJSONArray("values");
                if( ListUtil.isEmpty(values)) {
                    continue;
                }
                String chartAllType = chartOptions.getString("chartAllType");
                JSONObject chartCell = null;
                int r = 0;
                int rs = 1;
                int c = 0;
                int cs = 1;
                if(YesNoEnum.YES.getCode().intValue() == isCoedit) {
                    double top = jsonObject.getDoubleValue("top");
                    double left = jsonObject.getDoubleValue("left");
                    double height = jsonObject.getDoubleValue("height");
                    double width = jsonObject.getDoubleValue("width");
                    JSONObject strObj = LuckysheetUtil.calculateRows(top, rowlen, rowhidden);
                    JSONObject edrObj = LuckysheetUtil.calculateRows(top+height, rowlen, rowhidden);
                    JSONObject stcObj = LuckysheetUtil.calculateCols(left, columnlen, colhidden);
                    JSONObject edcObj = LuckysheetUtil.calculateCols(left+width, columnlen, colhidden);
                    r = strObj.getIntValue("r");
                    rs = edrObj.getIntValue("r") - strObj.getIntValue("r") + 1;
                    c = stcObj.getIntValue("c");
                    cs = edcObj.getIntValue("c") - stcObj.getIntValue("c") + 1;
                    chartCell = new JSONObject();
                    chartCell.put("r", r);
                    chartCell.put("rs", rs);
                    chartCell.put("c", c);
                    chartCell.put("cs", cs);
                }else {
                    chartCell = chartCells.getJSONObject(chartId);
                    if(chartCell != null)
                    {
                        r = chartCell.getIntValue("r");
                        rs = chartCell.getIntValue("rs");
                        c = chartCell.getIntValue("c");
                        cs = chartCell.getIntValue("cs");
                    }
                }
                XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0,c,r,c+cs,r+rs);
                anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
//				boolean showTitle = defaultOption.getJSONObject("title").getBooleanValue("show");
//				String title = defaultOption.getJSONObject("title").getString("text");
                if(chartAllType.contains("pie"))
                {//饼图
                    double innerRadius = defaultOption.getJSONObject("spec").getDoubleValue("innerRadius");
                    if(innerRadius>0) {
                        ExcelChartUtil.createDoughnut(sheet, chartCell, chartOptions,isCoedit);
                    }else {
                        ExcelChartUtil.createPie(sheet, chartCell, chartOptions,isCoedit);
                    }
                }else if(chartAllType.contains("line")) {
                    boolean smooth = false;
                    boolean showLabel = false;
                    if(chartAllType.contains("smooth")) {
                        smooth = true;
                    }
                    if(chartAllType.contains("label")) {
                        showLabel = true;
                    }
                    ExcelChartUtil.createLineChart(sheet, chartCell, chartOptions,smooth,showLabel,isCoedit);
                }
                else if(chartAllType.contains("area")) {
                    ExcelChartUtil.createAreaChart(sheet, chartCell, chartOptions,isCoedit);
                }
                else if(chartAllType.contains("column")) {
                    ExcelChartUtil.createBar(sheet, chartCell, chartOptions, "column", chartAllType.contains("stack"),isCoedit);
                }else if(chartAllType.contains("bar")) {
                    ExcelChartUtil.createBar(sheet, chartCell, chartOptions, "bar", chartAllType.contains("stack"),isCoedit);
                }else if(chartAllType.contains("radar")) {
                    ExcelChartUtil.createRadar(sheet, chartCell, chartOptions,isCoedit);
                }

            }
        }
    }

    /**
     * @MethodName: addDataVerification
     * @Description: 添加数据校验项
     * @author caiyang
     * @return void
     * @date 2023-05-06 08:37:01
     */
    private static void addDataVerification(XSSFSheet sheet,JSONObject dataVerification) {
        if(dataVerification != null && !dataVerification.isEmpty())
        {
            DataValidationHelper helper = sheet.getDataValidationHelper();
            for(Map.Entry entry : dataVerification.entrySet()){
                String key = (String) entry.getKey();
                int r = Integer.parseInt(key.split("_")[0]);
                int c = Integer.parseInt(key.split("_")[1]);
                JSONObject value = (JSONObject) entry.getValue();
                String type = value.getString("type");
                String value1 = value.getString("value1");
                String value2 = value.getString("value2");
                DataValidationConstraint constraint = null;
                if("dropdown".equals(type))
                {//下拉
                    if(value1.contains("$"))
                    {
                        constraint = helper.createFormulaListConstraint(value1);
                    }else {
                        constraint = helper.createExplicitListConstraint(value1.split(","));
                    }

                }else if("number_integer".equals(type))
                {//整数
                    String type2 = value.getString("type2");
                    int operate = getNumberOperateType(type2);
                    constraint = helper.createIntegerConstraint(operate, value1, value2);
                }else if("number_decimal".equals(type))
                {//小数
                    String type2 = value.getString("type2");
                    int operate = getNumberOperateType(type2);
                    constraint = helper.createDecimalConstraint(operate, value1, value2);
                }else if("text_length".equals(type))
                {//长度
                    String type2 = value.getString("type2");
                    int operate = getNumberOperateType(type2);
                    constraint = helper.createTextLengthConstraint(operate, value1, value2);
                }else if("date".equals(type)) {
                    //日期
                    String type2 = value.getString("type2");
                    int operate = getDateOperateType(type2);
                    constraint = helper.createDateConstraint(operate, value1, value2, DateUtil.FORMAT_LONOGRAM);
                }
                CellRangeAddressList addressList = new CellRangeAddressList(r,r,c,c);
                if(constraint != null)
                {
                    DataValidation validation = helper.createValidation(constraint,addressList);
                    validation.setSuppressDropDownArrow(true);
                    validation.setShowErrorBox(true);
                    sheet.addValidationData(validation);
                }

            }
        }
    }


    private static int getNumberOperateType(String type) {
        int operate = 0;
        switch (type) {
            case "bw":
                operate = 0;
                break;
            case "nb":
                operate = 1;
                break;
            case "eq":
                operate = 2;
                break;
            case "ne":
                operate = 3;
                break;
            case "gt":
                operate = 4;
            case "lt":
                operate = 5;
                break;
            case "gte":
                operate = 6;
                break;
            case "lte":
                operate = 7;
                break;
            default:
                operate = 0;
                break;
        }
        return operate;
    }

    private static int getDateOperateType(String type) {
        int operate = 0;
        switch (type) {
            case "bw":
                operate = 0;
                break;
            case "nb":
                operate = 1;
                break;
            case "eq":
                operate = 2;
                break;
            case "ne":
                operate = 3;
                break;
            case "af":
                operate = 4;
            case "bf":
                operate = 5;
                break;
            case "nbf":
                operate = 6;
                break;
            case "naf":
                operate = 7;
                break;
            default:
                operate = 0;
                break;
        }
        return operate;
    }

    private static void getSlashLinePositionXlsx(XSSFSheet xssfSheet,int rowIndex,int colIndex,int rs,int cs,String v,List<SlashLinePosition> slashes
            ,List<SlashLineText> slashTexts){
        int height = 0;
        int width = 0;
        Map<Integer, Integer> rowHeights = new LinkedHashMap<Integer, Integer>();
        Map<Integer, Integer> colWidths = new LinkedHashMap<Integer, Integer>();
        for (int i = 0; i < rs; i++) {
            int rowHeight = xssfSheet.getRow(rowIndex+i).getHeight()*700;
            rowHeights.put(rowIndex+i, rowHeight);
            height = height + rowHeight;
        }
        for (int i = 0; i < cs; i++) {
            int colWidth = xssfSheet.getColumnWidth(colIndex+i)*300;
            colWidths.put(colIndex+i, colWidth);
            width = width + colWidth;
        }
        String[] splits = v.split("\\|");
        int lineCount = v.split("\\|").length - 1;
        if(lineCount == 1)
        {
            SlashLinePosition slp1 = new SlashLinePosition(0, 0, width, height,rowIndex,colIndex,rowIndex+rs-1,colIndex+cs-1);
            slashes.add(slp1);
            int startx = 0;
            int starty = 0;
            if(rs > 1)
            {
                starty = 0;
            }else {
                starty = height/2;
            }
            SlashLineText slashLineText1 = new SlashLineText(startx, starty, colWidths.get(colIndex), rowHeights.get(rowIndex+rs-1), splits[0], rowIndex+rs-1, colIndex, rs, cs);
            slashTexts.add(slashLineText1);
            startx = 0;
            starty = 0;
            if(cs > 1)
            {
                startx = 0;
            }else {
                startx = width/2;
            }
            SlashLineText slashLineText2 = new SlashLineText(startx, starty, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex), splits[1], rowIndex, colIndex+cs-1, rs, cs);
            slashTexts.add(slashLineText2);
        }else if(lineCount == 2)
        {
            int rsMid = (int)Math.ceil(rs/2.0);
            int csMid = (int)Math.floor(cs/2.0);
            SlashLinePosition slp1 = new SlashLinePosition(0, 0, cs>1?getWidth(colWidths, colIndex, csMid):width/2, height,rowIndex,colIndex,rowIndex+rs-1,cs>1?colIndex+csMid-1:colIndex);
            slashes.add(slp1);
            int startx = 0;
            int starty = 0;
            if(rs > 1)
            {
                starty = 0;
            }else {
                starty = height/2;
            }
            SlashLineText slashLineText1 = new SlashLineText(0, starty, getWidth(colWidths, colIndex, csMid), rowHeights.get(rowIndex+rs-1), splits[0], rowIndex+rs-1, colIndex, rs, cs);
            slashTexts.add(slashLineText1);
            SlashLinePosition slp2 = new SlashLinePosition(0, 0, width, rs>1?getHeight(rowHeights, rowIndex, rsMid):height/2,rowIndex,colIndex,rs>1?rowIndex+rsMid-1:rowIndex,colIndex+cs-1);
            slashes.add(slp2);
            if(cs > 1)
            {
                startx = 0;
            }else {
                startx = width/2;
            }
            if(rs > 1)
            {
                starty = 0;
            }else {
                starty = height/2;
            }
            SlashLineText slashLineText2 = new SlashLineText(startx, starty, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex+rs-1), splits[1], rowIndex+rs-1,colIndex+cs-1, rs, cs);
            slashTexts.add(slashLineText2);
            startx = 0;
            starty = 0;
            if(cs > 1)
            {
                startx = 0;
            }else {
                startx = width/2;
            }
            SlashLineText slashLineText3 = new SlashLineText(startx, 0, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex), splits[2], rowIndex, colIndex+cs-1, rs, cs);
            slashTexts.add(slashLineText3);
        }else if(lineCount == 3) {
            int rsMid = (int)Math.floor(rs/2.0);
            int csMid = (int)Math.floor(cs/2.0);
            SlashLinePosition slp1 = new SlashLinePosition(0, 0, cs>1?getWidth(colWidths, colIndex, csMid):width/2, height,rowIndex,colIndex,rowIndex+rs-1,cs>1?colIndex+csMid-1:colIndex);
            slashes.add(slp1);
            int startx = 0;
            int starty = 0;
            if(rs > 1)
            {
                starty = 0;
            }else {
                starty = height/2;
            }
            SlashLineText slashLineText1 = new SlashLineText(0, starty, getWidth(colWidths, colIndex, csMid), rowHeights.get(rowIndex+rs-1), splits[0], rowIndex+rs-1, colIndex, rs, cs);
            slashTexts.add(slashLineText1);
            SlashLinePosition slp2 = new SlashLinePosition(0, 0, width, height,rowIndex,colIndex,rowIndex+rs-1,colIndex+cs-1);
            slashes.add(slp2);
            startx = 0;
            starty = 0;
            if(cs > 1)
            {
                startx = 0;
            }else {
                startx = colWidths.get(colIndex)/2;
            }
            if(rs > 1)
            {
                starty = rowHeights.get(rowIndex+rs-1)*1/3;
            }else {
                starty = rowHeights.get(rowIndex)*3/5;
            }
            SlashLineText slashLineText2 = new SlashLineText(startx, starty, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex+rs-1), splits[1], rowIndex+rs-1, colIndex+cs-1, rs, cs);
            slashTexts.add(slashLineText2);
            SlashLinePosition slp3 = new SlashLinePosition(0, 0, width, rs>1?getHeight(rowHeights, rowIndex, rsMid):height/2,rowIndex,colIndex,rs>1?rowIndex+rsMid-1:rowIndex,colIndex+cs-1);
            slashes.add(slp3);
            startx = 0;
            starty = 0;
            if(cs > 1)
            {
                startx = colWidths.get(colIndex+cs-1)*1/3;
            }else {
                startx = colWidths.get(colIndex)*3/5;
            }
            if(rs > 1)
            {
                starty = 0;
            }else {
                starty = rowHeights.get(rowIndex)*2/5;
            }
            SlashLineText slashLineText3 = new SlashLineText(startx, starty, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex+rs-1), splits[2], rowIndex+rs-1, colIndex+cs-1, rs, cs);
            slashTexts.add(slashLineText3);
            startx = 0;
            starty = 0;
            if(cs > 1)
            {
                startx = 0;
            }else {
                startx = width/2;
            }
            SlashLineText slashLineText4 = new SlashLineText(startx, 0, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex), splits[3], rowIndex, colIndex+cs-1, rs, cs);
            slashTexts.add(slashLineText4);
        }
    }


    private static int getHeight(Map<Integer, Integer> rowHeights,int r,int rs) {
        int height = rowHeights.get(r);
        if(rs > 0) {
            for (int i = 1; i < rs; i++) {
                height = height + rowHeights.get(r+i);
            }
        }
        return height;
    }

    private static int getWidth(Map<Integer, Integer> colWidths,int c,int cs) {
        int width = colWidths.get(c);
        if(cs > 0) {
            for (int i = 1; i < cs; i++) {
                width = width + colWidths.get(c+i);
            }
        }
        return width;
    }



    private static XSSFConditionalFormattingRule createConditionalFormattingRuleIconSets(XSSFSheetConditionalFormatting sheetCF,JSONObject format) throws Exception {
        Field _sheet = XSSFSheetConditionalFormatting.class.getDeclaredField("_sheet");
        _sheet.setAccessible(true);
        XSSFSheet sheet = (XSSFSheet)_sheet.get(sheetCF);
        Constructor constructor = XSSFConditionalFormattingRule.class.getDeclaredConstructor(XSSFSheet.class);
        constructor.setAccessible(true);
        XSSFConditionalFormattingRule rule = (XSSFConditionalFormattingRule)constructor.newInstance(sheet);
        String top = format.getString("top");
        String leftMin = format.getString("leftMin");
        if("0".equals(top)) {
            if("0".equals(leftMin)) {
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.GYR_3_ARROW);
            }else if("5".equals(leftMin)) {
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.GREY_3_ARROWS);
            }
        } else if("1".equals(top)) {
            if("5".equals(leftMin)) {
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.GREY_4_ARROWS);
            }else if("0".equals(leftMin)){
                //3个三角形不支持，使用三色交通灯GYR_3_TRAFFIC_LIGHTS代替
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.GYR_3_TRAFFIC_LIGHTS);
            }
        }else if("2".equals(top)) {
            if("0".equals(leftMin)) {
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.GYR_4_ARROWS);
            }else if("5".equals(leftMin)) {
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.GREY_5_ARROWS);
            }
        }else if("3".equals(top)) {
            if("0".equals(leftMin)) {
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.GYYYR_5_ARROWS);
            }
        }else if("4".equals(top)) {
            if("0".equals(leftMin)) {
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.GYR_3_TRAFFIC_LIGHTS);
            }else if("5".equals(leftMin)) {
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.GYR_3_TRAFFIC_LIGHTS_BOX);
            }
        }else if("5".equals(top)) {
            if("0".equals(leftMin)) {
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.GYR_3_SHAPES);
            }else if("5".equals(leftMin)) {
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.GYRB_4_TRAFFIC_LIGHTS);
            }
        }else if("6".equals(top)) {
            if("0".equals(leftMin)) {
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.RB_4_TRAFFIC_LIGHTS);
            }else if("5".equals(leftMin)) {
            }
        }else if("7".equals(top)) {
            if("0".equals(leftMin)) {
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.GYR_3_SYMBOLS_CIRCLE);
            }else if("5".equals(leftMin)) {
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.GYR_3_SYMBOLS);
            }
        }else if("8".equals(top)) {
            if("0".equals(leftMin)) {
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.GYR_3_FLAGS);
            }else if("5".equals(leftMin)) {
            }
        }else if("9".equals(top)) {
            if("0".equals(leftMin)) {
                //3个星星不支持，使用三色交通灯GYR_3_TRAFFIC_LIGHTS代替
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.GYR_3_TRAFFIC_LIGHTS);
            }else if("5".equals(leftMin)) {
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.RATINGS_4);
            }
        }else if("10".equals(top)) {
            if("0".equals(leftMin)) {
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.QUARTERS_5);
            }else if("5".equals(leftMin)) {
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.RATINGS_5);
            }
        }else if("11".equals(top)) {
            if("0".equals(leftMin)) {
                //5个框不支持，使用QUARTERS_5代替
                rule.createMultiStateFormatting(IconMultiStateFormatting.IconSet.QUARTERS_5);
            }else if("5".equals(leftMin)) {
            }
        }
        Field _cfRule = XSSFConditionalFormattingRule.class.getDeclaredField("_cfRule");
        _cfRule.setAccessible(true);
        CTCfRule cfRule = (CTCfRule)_cfRule.get(rule);
        cfRule.setType(STCfType.ICON_SET);
        return rule;
    }

    private static XSSFConditionalFormattingRule createConditionalFormattingRuleDuplicate(XSSFSheetConditionalFormatting sheetCF) throws Exception {
        Field _sheet = XSSFSheetConditionalFormatting.class.getDeclaredField("_sheet");
        _sheet.setAccessible(true);
        XSSFSheet sheet = (XSSFSheet)_sheet.get(sheetCF);
        Constructor constructor = XSSFConditionalFormattingRule.class.getDeclaredConstructor(XSSFSheet.class);
        constructor.setAccessible(true);
        XSSFConditionalFormattingRule rule = (XSSFConditionalFormattingRule)constructor.newInstance(sheet);
        Field _cfRule = XSSFConditionalFormattingRule.class.getDeclaredField("_cfRule");
        _cfRule.setAccessible(true);
        CTCfRule cfRule = (CTCfRule)_cfRule.get(rule);
        cfRule.setType(STCfType.DUPLICATE_VALUES);
        return rule;
    }

    private static XSSFConditionalFormattingRule createConditionalFormattingRuleRank(XSSFSheetConditionalFormatting sheetCF,String conditionName,JSONArray conditionValue) throws Exception {
        Field _sheet = XSSFSheetConditionalFormatting.class.getDeclaredField("_sheet");
        _sheet.setAccessible(true);
        XSSFSheet sheet = (XSSFSheet)_sheet.get(sheetCF);
        Constructor constructor = XSSFConditionalFormattingRule.class.getDeclaredConstructor(XSSFSheet.class);
        constructor.setAccessible(true);
        XSSFConditionalFormattingRule rule = (XSSFConditionalFormattingRule)constructor.newInstance(sheet);
        Field _cfRule = XSSFConditionalFormattingRule.class.getDeclaredField("_cfRule");
        _cfRule.setAccessible(true);
        CTCfRule cfRule = (CTCfRule)_cfRule.get(rule);
        if("top10".equals(conditionName)) {
            cfRule.setType(STCfType.TOP_10);
            cfRule.setRank(conditionValue.getLongValue(0));
        }else if("top10%".equals(conditionName)) {
            cfRule.setType(STCfType.TOP_10);
            cfRule.setRank(conditionValue.getLongValue(0));
            cfRule.setPercent(true);
        }else if("last10".equals(conditionName)) {
            cfRule.setType(STCfType.TOP_10);
            cfRule.setRank(conditionValue.getLongValue(0));
            cfRule.setBottom(true);
        }else if("last10%".equals(conditionName)) {
            cfRule.setType(STCfType.TOP_10);
            cfRule.setRank(conditionValue.getLongValue(0));
            cfRule.setBottom(true);
            cfRule.setPercent(true);
        }else if("AboveAverage".equals(conditionName)) {
            cfRule.setType(STCfType.ABOVE_AVERAGE);
        }else if("SubAverage".equals(conditionName)) {
            cfRule.setType(STCfType.ABOVE_AVERAGE);
            cfRule.setAboveAverage(false);
        }
        return rule;
    }
}
