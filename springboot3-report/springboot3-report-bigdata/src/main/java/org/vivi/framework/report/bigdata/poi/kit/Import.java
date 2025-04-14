package org.vivi.framework.report.bigdata.poi.kit;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.CollectionUtils;
import org.vivi.framework.report.bigdata.common.exception.BaseRuntimeException;
import org.vivi.framework.report.bigdata.common.exception.ErrorCode;
import org.vivi.framework.report.bigdata.poi.model.SheetInfo;
import org.vivi.framework.report.bigdata.poi.config.ImportConfig;
import org.vivi.framework.report.bigdata.poi.model.TitleType;
import org.vivi.framework.report.bigdata.poi.utils.SheetUtils;
import org.vivi.framework.report.bigdata.poi.utils.WorkBookUtils;

import java.io.InputStream;
import java.util.List;

/**
 * 功能 :
 * 导入/模板导入
 */
@Slf4j
@NoArgsConstructor(staticName = "create",access = AccessLevel.PROTECTED)
public class Import {

    /**导入excel的文件流**/
    private InputStream in;
    /**最大行数**/
    private Integer maxRow = 20000;
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /****/
    private Workbook workbook;
    /**workbook中的sheet信息:默认只解析第一个sheet中的信息**/
    private List<SheetInfo> sheetInfos = Lists.newArrayList();
    /**sheet初始化配置**/
    private List<ImportConfig> importConfigs = Lists.newArrayList();

    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     调整导入方法
     .config(in)
     .addSheet(Interface2.class)
     .addSheet(Interface2.class,sheetIndex)
     .addSheet(Interface2.class,sheetName)
     .addSheet("headers")
     .addSheet("headers",sheetIndex)
     .addSheet("headers",sheetName)
     */

    public Import config(InputStream in){
        this.in = in;
        return this;
    }

    public Import config(InputStream in, Integer maxRow){
        this.in = in;
        this.maxRow = maxRow;
        return this;
    }

    public Import addSheet(Class<?> clazz){
        this.importConfigs.add(ImportConfig.builder()
                .maxRow(this.maxRow)
                .clazz(clazz)
                .build());
        return this;
    }

    public Import addSheet(Class<?> clazz, Integer sheetIndex){
        this.importConfigs.add(ImportConfig.builder()
                .maxRow(this.maxRow)
                .clazz(clazz)
                .sheetIndex(sheetIndex)
                .build());
        return this;
    }

    public Import addSheet(Class<?> clazz, String sheetName){
        this.importConfigs.add(ImportConfig.builder()
                .maxRow(this.maxRow)
                .clazz(clazz)
                .sheetName(sheetName)
                .build());
        return this;
    }

    public Import addSheet(String titles){
        this.importConfigs.add(ImportConfig.builder()
                .maxRow(this.maxRow)
                .titles(titles)
                .build());
        return this;
    }

    public Import addSheet(String titles, Integer sheetIndex){
        this.importConfigs.add(ImportConfig.builder()
                .maxRow(this.maxRow)
                .titles(titles)
                .sheetIndex(sheetIndex)
                .build());
        return this;
    }

    public Import addSheet(String titles, String sheetName){
        this.importConfigs.add(ImportConfig.builder()
                .maxRow(this.maxRow)
                .titles(titles)
                .sheetName(sheetName)
                .build());
        return this;
    }

    public Import addSheet(Integer[] columns){
        this.importConfigs.add(ImportConfig.builder()
                .maxRow(this.maxRow)
                .columns(columns)
                .build());
        return this;
    }

    public Import addSheet(Integer[] columns, Integer sheetIndex){
        this.importConfigs.add(ImportConfig.builder()
                .maxRow(this.maxRow)
                .sheetIndex(sheetIndex)
                .columns(columns)
                .build());
        return this;
    }

    public Import addSheet(Integer[] columns, String sheetName){
        this.importConfigs.add(ImportConfig.builder()
                .maxRow(this.maxRow)
                .sheetName(sheetName)
                .columns(columns)
                .build());
        return this;
    }

    public Import addSheet(Integer[] rows, Integer[] columns){
        this.importConfigs.add(ImportConfig.builder()
                .maxRow(this.maxRow)
                .columns(columns)
                .rows(rows)
                .build());
        return this;
    }

    public Import addSheet(Integer[] rows, Integer[] columns, Integer sheetIndex){
        this.importConfigs.add(ImportConfig.builder()
                .maxRow(this.maxRow)
                .sheetIndex(sheetIndex)
                .columns(columns)
                .rows(rows)
                .build());
        return this;
    }

    public Import addSheet(Integer[] rows, Integer[] columns, String sheetName){
        this.importConfigs.add(ImportConfig.builder()
                .maxRow(this.maxRow)
                .sheetName(sheetName)
                .columns(columns)
                .rows(rows)
                .build());
        return this;
    }


    public Import addSheet(Integer top, Integer bottom, Integer left, Integer right){
        List<Integer> columns = Lists.newArrayList();
        List<Integer> rows = Lists.newArrayList();
        for (int i = top;i <= bottom;i++) {
            rows.add(i);
        }
        for (int i = left;i <= right;i++) {
            columns.add(i);
        }
        this.importConfigs.add(ImportConfig.builder()
                .maxRow(this.maxRow)
                .columns(columns.toArray(new Integer[]{}))
                .rows(rows.toArray(new Integer[]{}))
                .build());
        return this;
    }

    public Import addSheet(Integer top, Integer bottom, Integer left, Integer right, Integer sheetIndex){
        List<Integer> columns = Lists.newArrayList();
        List<Integer> rows = Lists.newArrayList();
        for (int i = top;i <= bottom;i++) {
            rows.add(i);
        }
        for (int i = left;i <= right;i++) {
            columns.add(i);
        }
        this.importConfigs.add(ImportConfig.builder()
                .maxRow(this.maxRow)
                .sheetIndex(sheetIndex)
                .columns(columns.toArray(new Integer[]{}))
                .rows(rows.toArray(new Integer[]{}))
                .build());
        return this;
    }

    public Import addSheet(Integer top, Integer bottom, Integer left, Integer right, String sheetName){
        List<Integer> columns = Lists.newArrayList();
        List<Integer> rows = Lists.newArrayList();
        for (int i = top;i <= bottom;i++) {
            rows.add(i);
        }
        for (int i = left;i <= right;i++) {
            columns.add(i);
        }
        this.importConfigs.add(ImportConfig.builder()
                .maxRow(this.maxRow)
                .sheetName(sheetName)
                .columns(columns.toArray(new Integer[]{}))
                .rows(rows.toArray(new Integer[]{}))
                .build());
        return this;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**导入步骤**/
    public <T> List<T> onedata(){
        // 1.初始化workbook
        initImportWorkbook();
        // 2.初始化sheetInfo
        initSheetInfo();
        // 3.执行导入
        executeImport();
        return sheetInfos.get(0).getDatas();
    }

    /**导入步骤**/
    public SheetInfo one(){
        // 1.初始化workbook
        initImportWorkbook();
        // 2.初始化sheetInfo
        initSheetInfo();
        // 3.执行导入
        executeImport();
        return sheetInfos.get(0);
    }

    /**导入步骤**/
    public List<SheetInfo> all(){
        // 1.初始化workbook
        initImportWorkbook();
        // 2.导入全部,则默认选择第一个importConfig作为模板
        initAllSheetInfo();
        // 3.执行导入
        executeImport();
        return sheetInfos;
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

    /**初始化导入workbook**/
    private void initImportWorkbook() {
        this.workbook = WorkBookUtils.create(this.in);
    }

    /**初始化sheetInfo,为数据导入做准备:title,sheet初始化**/
    private void initSheetInfo() {
        if(CollectionUtils.isEmpty(importConfigs)){
            throw new BaseRuntimeException(ErrorCode.EXPORT_ERROR.getCode(),"Excel配置初始化失败,请传入必要的配置参数");
        }
        //   a.根据config,生成对应的sheetInfo
        int length = importConfigs.size();
        for (int i = 0;i < length;i++) {
            ImportConfig config = importConfigs.get(i);
            configSheetInfo(i, config);
        }
    }

    private void initAllSheetInfo() {
        if(CollectionUtils.isEmpty(importConfigs)){
            throw new BaseRuntimeException(ErrorCode.EXPORT_ERROR.getCode(),"Excel配置初始化失败,请传入必要的配置参数");
        }
        //   a.根据config,生成对应的sheetInfo
        int length = workbook.getNumberOfSheets();
        for (int i = 0;i < length;i++) {
            ImportConfig config = importConfigs.get(0);
            configSheetInfo(i, config);
        }
    }

    /**根据config配置sheetInfo**/
    private void configSheetInfo(int i, ImportConfig config) {
        SheetInfo sheetInfo = new SheetInfo();
        // 根据不同传入数据类型,设置表头
        initTitleByType(config, sheetInfo);

        Integer sheetIndex = config.getSheetIndex();
        String sheetName = config.getSheetName();
        Sheet sheet = null;
        if ( null == sheetIndex && StringUtils.isBlank(sheetName)) {
            sheet = workbook.getSheetAt(i);
        } else if ( null != sheetIndex ) {
            sheet = workbook.getSheetAt(sheetIndex);
        } else if (StringUtils.isNotBlank(sheetName)) {
            sheet = workbook.getSheet(sheetName);
        }

        if ( null == sheet ) {
            return;
        }
        if (workbook.isSheetHidden(workbook.getSheetIndex(sheet))) {
            return;
        }// 隐藏表格的数据,默认不读取
        sheetInfo.setSheet(sheet);
        sheetInfo.setName(sheet.getSheetName());
        this.sheetInfos.add(sheetInfo);
    }

    /**根据不同传入数据类型,设置表头**/
    private void initTitleByType(ImportConfig config, SheetInfo sheetInfo) {
        if( null != config.getClazz() ){
            sheetInfo.setTitleType(TitleType.CLASS);
            sheetInfo.setClazz(config.getClazz());
            SheetUtils.setTitles(sheetInfo,config.getClazz());
        }else if( StringUtils.isNotBlank(config.getTitles()) ){
            sheetInfo.setTitleType(TitleType.COLLECION);
            SheetUtils.setTitles(sheetInfo,config.getTitles());
        }else{
            sheetInfo.setTitleType(TitleType.NONE);
            if( null != config.getColumns() && config.getColumns().length > 0 ){
                sheetInfo.setColumns(Lists.newArrayList(config.getColumns()));
            }
            if( null != config.getRows() && config.getRows().length > 0 ){
                sheetInfo.setRows(Lists.newArrayList(config.getRows()));
            }
        }
    }

    /**执行数据导入逻辑**/
    private void executeImport() {
        for (SheetInfo sheetInfo : sheetInfos) {
            SheetUtils.load(sheetInfo);
        }
    }
}
