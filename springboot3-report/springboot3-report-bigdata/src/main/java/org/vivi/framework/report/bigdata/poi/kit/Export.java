package org.vivi.framework.report.bigdata.poi.kit;


import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.CollectionUtils;
import org.vivi.framework.report.bigdata.poi.config.ExportConfig;
import org.vivi.framework.report.bigdata.common.exception.BaseRuntimeException;
import org.vivi.framework.report.bigdata.common.exception.ErrorCode;
import org.vivi.framework.report.bigdata.poi.model.SheetInfo;
import org.vivi.framework.report.bigdata.poi.model.TitleType;
import org.vivi.framework.report.bigdata.poi.model.Version;
import org.vivi.framework.report.bigdata.poi.utils.CellStyleUtils;
import org.vivi.framework.report.bigdata.poi.utils.SheetUtils;
import org.vivi.framework.report.bigdata.poi.utils.WorkBookUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


/**
 * 导入/导出/模板导入/模板导出
 */
@Slf4j
@NoArgsConstructor(staticName = "create",access = AccessLevel.PROTECTED)
public class Export {

    /**导入excel的文件流**/
    private InputStream in;
    /**是否使用模板**/
    private Boolean useTemplate = false;
    /**新建excel的版本号**/
    private Version version = Version.XLS;
    /**最大行数**/
    private Integer maxRow = 20000;
    /**文件后缀**/
    private String suffix = "";

    //////////////////////////////////////////////////////////////////////////////////////////////////

    /****/
    private Workbook workbook;
    /**workbook中的sheet信息:默认只解析第一个sheet中的信息**/
    private List<SheetInfo> sheetInfos = Lists.newArrayList();
    /**sheet初始化配置**/
    private List<ExportConfig> exportConfigs = Lists.newArrayList();

    //////////////////////////////////////////////////////////////////////////////////////////////////
    /**表头格式**/
    private CellStyle headerStyle;
    /**特殊表头-合并单元格格式**/
    private CellStyle mergeTitleStyle;
    /**奇数行格式**/
    private CellStyle oddStyle;
    /**偶数行格式**/
    private CellStyle evenStyle;

    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     调整导出方法
     .config(Version.XLS,OperateType.ALL)
     .config(Version.XLS,OperateType.ALL,100000)
     .addSheet(datas,Interface2.class,"sheetName")
     .addSheet(datas,Interface2.class,map,"sheetName")
     .addSheet(datas,Interface2.class,"sheetName","sheetDescription")
     .addSheet(datas,Interface2.class,map,"sheetName","sheetDescription")
     .addSheet(datas,"headers","sheetName")
     .addSheet(datas,"headers",map,"sheetName")
     .addSheet(datas,"headers","sheetName","sheetDescription")
     .addSheet(datas,"headers",map,"sheetName","sheetDescription")
     */

    public Export config(Version version){
        this.version = version;
        return this;
    }

    public Export config(Version version, Integer maxRow){
        this.config(version);
        this.maxRow = maxRow;
        return this;
    }

    public Export addSheet(List datas, Class<?> clazz, String sheetName){
        this.exportConfigs.add(ExportConfig.builder()
                .maxRow(this.maxRow)
                .clazz(clazz)
                .sheetName(sheetName)
                .datas(datas)
                .build());
        return this;
    }

    public Export addSheet(List datas, Class<?> clazz, String sheetName, String sheetDescription){
        this.exportConfigs.add(ExportConfig.builder()
                .maxRow(this.maxRow)
                .clazz(clazz)
                .sheetName(sheetName)
                .sheetDescription(sheetDescription)
                .datas(datas)
                .build());
        return this;
    }

    public Export addSheet(List datas, Class<?> clazz, Map<String,List<?>> dropDowns, String sheetName){
        this.exportConfigs.add(ExportConfig.builder()
                .maxRow(this.maxRow)
                .clazz(clazz)
                .sheetName(sheetName)
                .dropDowns(dropDowns)
                .datas(datas)
                .build());
        return this;
    }

    public Export addSheet(List datas, Class<?> clazz, Map<String,List<?>> dropDowns, String sheetName, String sheetDescription){
        this.exportConfigs.add(ExportConfig.builder()
                .maxRow(this.maxRow)
                .clazz(clazz)
                .sheetName(sheetName)
                .sheetDescription(sheetDescription)
                .dropDowns(dropDowns)
                .datas(datas)
                .build());
        return this;
    }

    public Export addSheet(List datas, String titles, String sheetName){
        this.exportConfigs.add(ExportConfig.builder()
                .maxRow(this.maxRow)
                .titles(titles)
                .sheetName(sheetName)
                .datas(datas)
                .build());
        return this;
    }

    public Export addSheet(List datas, String titles, String sheetName, String sheetDescription){
        this.exportConfigs.add(ExportConfig.builder()
                .maxRow(this.maxRow)
                .titles(titles)
                .sheetName(sheetName)
                .sheetDescription(sheetDescription)
                .datas(datas)
                .build());
        return this;
    }

    public Export addSheet(List datas, String titles, Map<String,List<?>> dropDowns, String sheetName){
        this.exportConfigs.add(ExportConfig.builder()
                .maxRow(this.maxRow)
                .titles(titles)
                .sheetName(sheetName)
                .dropDowns(dropDowns)
                .datas(datas)
                .build());
        return this;
    }

    public Export addSheet(List datas, String titles, Map<String,List<?>> dropDowns, String sheetName, String sheetDescription){
        this.exportConfigs.add(ExportConfig.builder()
                .maxRow(this.maxRow)
                .titles(titles)
                .sheetName(sheetName)
                .sheetDescription(sheetDescription)
                .dropDowns(dropDowns)
                .datas(datas)
                .build());
        return this;
    }

    /**导出:使用模板导出,默认使用模板中的active sheet的模板**/
    public Export addSheet(List datas, InputStream template){
        this.in = template;
        this.useTemplate = true;
        this.exportConfigs.add(ExportConfig.builder()
                .maxRow(this.maxRow)
                .datas(datas)
                .build());
        return this;
    }

    /**导出:使用模板导出,指定使用模板中索引为index的sheet作为模板**/
    public Export addSheet(List datas, InputStream template, Integer index){
        this.in = template;
        this.useTemplate = true;
        this.exportConfigs.add(ExportConfig.builder()
                .maxRow(this.maxRow)
                .templateIndex(index)
                .datas(datas)
                .build());
        return this;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**设置sheet最大行数限制**/
    public Export maxRow(Integer maxRow){
        this.maxRow = maxRow;
        return this;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    /**设置单元格样式**/
    public Export styleHeader(IndexedColors colors, Short fontSize, String fontName){
        CellStyleUtils.setCellStyle(workbook,headerStyle,colors, fontSize, fontName);
        return this;
    }
    public Export styleMergeTitle(IndexedColors colors, Short fontSize, String fontName){
        CellStyleUtils.setCellStyle(workbook,mergeTitleStyle,colors, fontSize, fontName);
        return this;
    }
    public Export styleOdd(IndexedColors colors, Short fontSize, String fontName){
        CellStyleUtils.setCellStyle(workbook,oddStyle,colors, fontSize, fontName);
        return this;
    }
    public Export styleEven(IndexedColors colors, Short fontSize, String fontName){
        CellStyleUtils.setCellStyle(workbook,evenStyle,colors, fontSize, fontName);
        return this;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**保留workbook对象**/
    public Workbook toWorkBook(){
        export();
        return this.workbook;
    }

    /**导出到response中**/
    public void toResponse(HttpServletResponse response, String fileName){
        export();
        try {
            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("application/form-data");
            // 转码中文
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), "iso8859-1");
            // TODO: 2017/4/27 文件扩展名
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName +"."+ suffix);

            OutputStream out = response.getOutputStream();

            WorkBookUtils.write(this.workbook,out);

        } catch (Exception e) {
            log.error("{}",e);
        }
    }

    /**导出字节数组**/
    public byte[] toBytes(){
        export();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            WorkBookUtils.write(this.workbook,out);

            byte[] bytes = out.toByteArray();
            out.close();
            return bytes;
        } catch (Exception e) {
            log.error("{}",e);
        }
        return new byte[0];
    }

    /**导出到指定的流文件中:需要先执行stream()方法,初始化outputStream**/
    public void toStream(OutputStream out){
        export();
        WorkBookUtils.write(this.workbook,out);
    }

    /**导出到指定的流文件中:需要先执行stream()方法,初始化outputStream**/
    public void toFile(String path){
        File file = new File(path);
        try(FileOutputStream out = new FileOutputStream(file)){
            export();
            WorkBookUtils.write(this.workbook,out);
        } catch (Exception e) {
            log.error("{}",e);
        }
    }

    /*
     ****************************************私有方法区*******************************************
                   _               _                           _    _                 _
                  (_)             | |                         | |  | |               | |
      _ __   _ __  _ __   __ __ _ | |_  ___   _ __ ___    ___ | |_ | |__    ___    __| |
     | '_ \ | '__|| |\ \ / // _` || __|/ _ \ | '_ ` _ \  / _ \| __|| '_ \  / _ \  / _` |
     | |_) || |   | | \ V /| (_| || |_|  __/ | | | | | ||  __/| |_ | | | || (_) || (_| |
     | .__/ |_|   |_|  \_/  \__,_| \__|\___| |_| |_| |_| \___| \__||_| |_| \___/  \__,_|
     | |
     |_|

     ****************************************私有方法区*******************************************
     */

    /**导出步骤**/
    private void export(){
        // 1.初始化workbook
        initExportWorkbook();
        // 2.初始化单元格样式
        initCellStyle();
        // 3.初始化sheetInfo
        initSheetInfo();
        //   b.根据每个sheetInfo的datas,考虑是否自动增加sheet,满足数据超多的要求?
        executeExport();
    }

    /**初始化导出workbook**/
    private void initExportWorkbook() {
        if( null != workbook ){
            return;
        }
        if( this.useTemplate ){
            this.workbook = WorkBookUtils.create(this.in);
            this.version = WorkBookUtils.version(this.workbook);
            return;
        }
        this.workbook = WorkBookUtils.create(this.version);
        this.suffix = this.version.name().toLowerCase();
    }

    /**初始化单元格样式**/
    private void initCellStyle() {
        headerStyle = CellStyleUtils.defaultHeaderStyle(workbook);
        mergeTitleStyle = CellStyleUtils.defaultMergeTitleStyle(workbook);
        oddStyle = CellStyleUtils.defaultOddStyle(workbook);
        evenStyle = CellStyleUtils.defaultEvenStyle(workbook);
    }

    /**设置单元格样式**/
    private void setColumnStyle(SheetInfo sheetInfo){
        if( null != oddStyle){
            List<CellStyle> oddStyles = CellStyleUtils.columnCellStyle(workbook,sheetInfo.getHeaderNames(),sheetInfo.getHeaderInfos(),oddStyle);
            sheetInfo.setOddStyle(oddStyles);
        }
        if( null != evenStyle){
            List<CellStyle> evenStyles = CellStyleUtils.columnCellStyle(workbook,sheetInfo.getHeaderNames(),sheetInfo.getHeaderInfos(),evenStyle);
            sheetInfo.setEvenStyle(evenStyles);
        }
    }

    /**初始化sheetInfo,为数据导出做准备:title,sheet初始化**/
    private void initSheetInfo() {
        if(CollectionUtils.isEmpty(exportConfigs)){
            throw new BaseRuntimeException(ErrorCode.EXPORT_ERROR.getCode(),"Excel配置初始化失败,请传入必要的配置参数");
        }
        //   a.根据config,生成对应的sheetInfo
        for (ExportConfig config : exportConfigs) {
            SheetInfo sheetInfo = new SheetInfo();
            List datas = config.getDatas();
            if( config.getMaxRow() >= datas.size() ) {
                initBaseSheetInfo(config, sheetInfo);
                sheetInfo.setVersion(this.version);
                sheetInfo.setDatas(config.getDatas());
                this.sheetInfos.add(sheetInfo);
            }else{
                List sheetDatas = config.getDatas();
                Integer length = datas.size();
                Integer remainder = length % this.maxRow;
                Integer cMaxRow = config.getMaxRow();
                splitDataToSheetInfos(config, sheetDatas, length, remainder, cMaxRow);
            }
        }
    }

    /**拆分data到多个sheetinfo中**/
    private void splitDataToSheetInfos(ExportConfig config, List sheetDatas, Integer length, Integer remainder, Integer maxRow) {
        SheetInfo sheetInfo;
        for (int i = 0; i < length; i++) {
            if (i % maxRow == 0 || i == length - remainder) {
                sheetInfo = new SheetInfo();
                initBaseSheetInfo(config, sheetInfo, i);
                sheetInfo.setVersion(this.version);
                if( i >= length - remainder || length < this.maxRow ){
                    sheetInfo.setDatas(sheetDatas.subList(i,length));
                }else{
                    sheetInfo.setDatas(sheetDatas.subList(i,i + this.maxRow));
                }
                sheetInfos.add(sheetInfo);
            }
        }
    }

    /**初始化:sheetInfo信息,除data之外的其他信息**/
    private void initBaseSheetInfo(ExportConfig config, SheetInfo sheetInfo) {
        this.initBaseSheetInfo(config,sheetInfo,null);
    }

    /**初始化:sheetInfo信息,除data之外的其他信息**/
    private void initBaseSheetInfo(ExportConfig config, SheetInfo sheetInfo, Integer sheetIndex) {
        initTitles(config, sheetInfo);
        sheetInfo.setMaxRowNum(config.getMaxRow());
        sheetInfo.setDescription(config.getSheetDescription());
        if( this.useTemplate ){
            Sheet sheet;
            Sheet active = workbook.getSheetAt(workbook.getActiveSheetIndex());
            if( null == config.getTemplateIndex() ){
                sheet = workbook.cloneSheet(workbook.getActiveSheetIndex());
            }else{
                sheet = workbook.cloneSheet(config.getTemplateIndex());
            }
            int newSheetIndex = workbook.getSheetIndex(sheet);
            workbook.setActiveSheet(newSheetIndex);
            workbook.setSheetName(newSheetIndex,active.getSheetName()+newSheetIndex);
            if(StringUtils.isNotBlank(config.getSheetName()) ){
                workbook.setSheetName(newSheetIndex,config.getSheetName());
            }
            sheetInfo.setSheet(sheet);
            sheetInfo.setDescription(config.getSheetDescription());
            // 从sheet默认的最后一行开始写数据
            sheetInfo.setBeginRow(sheet.getLastRowNum());
        }else{
            sheetInfo.setDescription(config.getSheetDescription());
            if( null == sheetIndex ){
                Sheet sheet = workbook.createSheet(config.getSheetName());
                sheetInfo.setSheet(sheet);
            }else{
                Integer sheets = workbook.getNumberOfSheets();
                Sheet sheet = workbook.createSheet(config.getSheetName()+(sheets));
                sheetInfo.setSheet(sheet);
            }
        }
        setColumnStyle(sheetInfo);
        // 初始化特殊表头
        SheetUtils.setSheetDescription(sheetInfo,mergeTitleStyle);
    }

    /**初始化表头**/
    private void initTitles(ExportConfig config, SheetInfo sheetInfo) {
        if( null != config.getClazz()){
            SheetUtils.setTitles(sheetInfo,config);
            sheetInfo.setTitleType(TitleType.CLASS);
        }else if( null != config.getTitles()){
            SheetUtils.setTitles(sheetInfo,config.getTitles());
            sheetInfo.setTitleType(TitleType.COLLECION);
        }else{
            sheetInfo.setTitleType(TitleType.NONE);
        }
    }

    /**执行导出任务**/
    private void executeExport() {
        for (SheetInfo sheetInfo : sheetInfos) {
            SheetUtils.export(sheetInfo,headerStyle);
        }
    }
}
