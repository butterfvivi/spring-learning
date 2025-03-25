package org.vivi.framework.poi.demo.utils;


import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.vivi.framework.poi.demo.model.HeadDetails;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @Description: excel导出工具类,可以通过 sheetBiFunction 自定义导入 逻辑
 */
public class WorkbookUtil {
    public List<HSSFSheet> sheets = new ArrayList<>();
    public HSSFWorkbook hssfWorkbook;
    public volatile int sheetIndex = 0;
    public String fileName = "Excel默认名称.xls";


    public WorkbookUtil(String fileName) {
        this.fileName = fileName + ".xls";
    }

    public WorkbookUtil() {
    }

    public String getFileName() {
        return fileName;
    }

    public WorkbookUtil setFileName(String fileName) {
        if (Objects.nonNull(fileName)){
            this.fileName = fileName + ".xls";
        }
        return this;
    }


    public static void main(String[] args) {

        // 使用方法
        new WorkbookUtil()
                // 文件名称
                .setFileName(null)
                // TODO 初始化一个excel
                .initWorkbook()
                // TODO 初始化一个sheet页,可自定义逻辑，(HSSFWorkbook,String) ->{ HSSFSheet}
                .initSheet(null,null)
                // TODO 初始化sheet页表头 ，可自定义逻辑，(HSSFSheet,HeadDetails) ->{ HSSFSheet} 下同
                .initSheetHead(null,null,null,null)
                // 填充sheet页数据
                .setSheetData(null,null,null)
                // sheet页数据指定列合并行
                .setDataRowMerged(null,null,null)
                // sheet页数据指定相邻列合并,可以传入多值，(单行合并，值相同情况)
                .setDataCellMerged(null,null,null)
                // 当前sheet当前行插入数据  data ：Map<Integer, String> (列索引,数据)
                .setSheetRow(null,null)
                // 当前sheet当前行强制合并    (firstCol,lastCol) 合并索引[必填]
                .setRowMerged(null,null)
                // 当前sheet指定行强制合并
                .setCellRangeAddress(null,null)
                // 初始化表尾
                .initSheetFoot(null,null)
                // TODO 第二个sheet页..
                .initSheet(null,null)
                .initSheetHead(null,null,null,null)
                .setSheetData(null,null,null)
                .initSheetFoot(null,null)
                .setDataRowAndCellMerged(null,null,null)
                //....
                // .builderByte()  输出字节数组
                // 直接写入报文
                .builderResponseEntity();

    }



    /**
     * <per>
     * <p>行列合并，一般根据特定需求编码，这里没有写默认的逻辑</p>
     * <per/>
     * @param merged 传递的参数，Map<合并行的列索引,合并列的列索引>
     * @param list
     * @param sheetBiFunction
     * @return com.hhwy.pwps.util.excel.WorkbookUtil
     * @throws
     * @Description : TODO
     * @author Liruilong
     * @Date 2021-01-24 18:28
     **/
    public WorkbookUtil setDataRowAndCellMerged(Map<int[], int[]> merged,List<?> list, BiFunction<HSSFSheet, Map<int[], int[]>, HSSFSheet> sheetBiFunction){

        HSSFSheet sheet = this.sheets.get(sheetIndex);
        if (Objects.nonNull(merged)  && Objects.nonNull(list) && Objects.isNull(sheetBiFunction)) {
            // 默认行列同时合并逻辑
        }
        sheets.add(sheetIndex, Objects.isNull(sheetBiFunction) ? sheet : sheetBiFunction.apply(sheet, merged));
        return this;
    }


    /**
     * <per>
     * <p>数据指定列索引合并</p>
     * <per/>
     *
     * @param merged
     * @param list
     * @param sheetBiFunction
     * @return com.hhwy.pwps.managepointins.service.impl.WorkbookUtil
     * @throws
     * @Description : TODO
     * @author Liruilong
     * @Date 2021-01-22 11:19
     **/
    public WorkbookUtil setDataCellMerged(Map<Integer, Integer> merged, List<?> list, BiFunction<HSSFSheet, Map<Integer, Integer>, HSSFSheet> sheetBiFunction) {
        HSSFSheet sheet = this.sheets.get(sheetIndex);
        List<CellRangeAddress> cellRangeAddresses = new ArrayList<>();
        if (Objects.nonNull(merged) && Objects.isNull(sheetBiFunction)) {
            int numberOfRows = sheet.getPhysicalNumberOfRows() - list.size();
            List<Integer> cellIndex = merged.keySet().stream().collect(Collectors.toList());
            for (int j = 0; j < cellIndex.size(); j++) {
                int cellIndex_ = cellIndex.get(j);
                for (int i = numberOfRows; i < sheet.getPhysicalNumberOfRows(); i++) {
                    HSSFRow sheetRow = sheet.getRow(i);
                    String cell1 = sheetRow.getCell(cellIndex_).toString();
                    String cell2 = sheetRow.getCell(merged.get(cellIndex_)).toString();
                    if (cell1.equals(cell2)) {
                        sheet.addMergedRegionUnsafe(new CellRangeAddress(i, i, cellIndex_, merged.get(cellIndex_)));
                    }
                }
            }
        }
        sheets.add(sheetIndex, Objects.isNull(sheetBiFunction) ? sheet : sheetBiFunction.apply(sheet, merged));
        return this;
    }


    /**
     * <per>
     * <p>指定行索引强制合并指定的列数据:CellRangeAddress原始方法调用</p>
     * <per/>
     * @param merged
     * @param sheetBiFunction
     * @return com.hhwy.pwps.managepointins.service.impl.WorkbookUtil
     * @throws
     * @Description : TODO
     * @author Liruilong
     * @Date 2021-01-21 20:26
     **/
    public WorkbookUtil setCellRangeAddress(Map<int[], int[]> merged, BiFunction<HSSFSheet, Map<int[], int[]>, HSSFSheet> sheetBiFunction) {
        HSSFSheet sheet = this.sheets.get(sheetIndex);
        if (Objects.nonNull(merged) && Objects.isNull(sheetBiFunction)) {
            merged.forEach((o1, o2) -> {
                sheet.addMergedRegionUnsafe(new CellRangeAddress(o1[0], o1[1], o2[0], o2[1]));
            });
        }
        sheets.add(sheetIndex, Objects.isNull(sheetBiFunction) ? sheet : sheetBiFunction.apply(sheet, merged));
        return this;
    }

    /**
     * <per>
     * <p>数据列合并对应行数据</p>
     * <per/>
     *
     * @param list            sheet数据[必填]
     * @param merged          列索引[必填]
     * @param sheetBiFunction
     * @return com.hhwy.pwps.managepointins.service.impl.WorkbookUtil
     * @throws
     * @Description : TODO
     * @author Liruilong
     * @Date 2021-01-21 15:13
     **/
    public WorkbookUtil setDataRowMerged(List<Integer> merged, List<?> list, BiFunction<HSSFSheet, List<Integer>, HSSFSheet> sheetBiFunction) {
        HSSFSheet sheet = this.sheets.get(sheetIndex);
        List<CellRangeAddress> cellRangeAddresses = new ArrayList<>();
        if (Objects.nonNull(merged) && Objects.isNull(sheetBiFunction)) {
            //数据起始行
            int numberOfRows = sheet.getPhysicalNumberOfRows();
            int first_ = numberOfRows - list.size();
            merged.stream().forEach(index -> {
                String old = null;
                int first = first_;
                for (int i = first; i < numberOfRows; i++) {
                    HSSFRow sheetRow = sheet.getRow(i);
                    String cell = sheetRow.getCell(index).toString();
                    //第一行跳过
                    if (i == first_) {
                        old = cell;
                        continue;
                    }
                    //合并逻辑
                    if (!old.equals(cell)) {
                        if (first != i - 1) {
                            sheet.addMergedRegionUnsafe(new CellRangeAddress(first, i - 1, index, index));
                        }
                        first = i;
                        old = cell;
                        // 最后一行判断
                    } else if (i == numberOfRows - 1) {
                        if (first != i) {
                            sheet.addMergedRegionUnsafe(new CellRangeAddress(first, i, index, index));
                            //    cellRangeAddresses.add(new CellRangeAddress(first, i - 1, index, index));
                        }
                    }
                }
            });
        }
        sheets.add(sheetIndex, Objects.isNull(sheetBiFunction) ? sheet : sheetBiFunction.apply(sheet, merged));
        return this;
    }

    /**
     * <per>
     * <p>当前行实现单元格强制合并</p>
     * <per/>
     *
     * @param merged          合并索引[必填] (firstCol,lastCol)
     * @param sheetBiFunction
     * @return com.hhwy.pwps.managepointins.service.impl.WorkbookUtil
     * @throws
     * @Description : TODO
     * @author Liruilong
     * @Date 2021-01-21 14:43
     **/
    public WorkbookUtil setRowMerged(Map<Integer, Integer> merged, BiFunction<HSSFSheet, Map<Integer, Integer>, HSSFSheet> sheetBiFunction) {
        HSSFSheet sheet = this.sheets.get(sheetIndex);
        if (Objects.nonNull(merged) && Objects.isNull(sheetBiFunction)) {
            int numberOfRows = sheet.getPhysicalNumberOfRows();
            List<Integer> cellIndex = merged.keySet().stream().collect(Collectors.toList());
            for (int i = 0; i < cellIndex.size(); i++) {
                int index = cellIndex.get(i);
                sheet.addMergedRegionUnsafe(new CellRangeAddress(numberOfRows - 1, numberOfRows - 1, index, merged.get(index)));
            }
        }
        sheets.add(sheetIndex, Objects.isNull(sheetBiFunction) ? sheet : sheetBiFunction.apply(sheet, merged));
        return this;
    }

    /**
     * <per>
     * <p>sheet页指定列索引插入单行数据</p>
     * <per/>
     *
     * @param data            单行数据[必填]  Map<Integer, String> (列索引,数据)
     * @param sheetBiFunction
     * @return com.hhwy.pwps.managepointins.service.impl.WorkbookUtil
     * @throws
     * @Description : TODO Specify column index inserts single line data
     * @author Liruilong
     * @Date 2021-01-21 10:33
     **/
    public WorkbookUtil setSheetRow(Map<Integer, String> data, BiFunction<HSSFSheet, Map<Integer, String>, HSSFSheet> sheetBiFunction) {
        HSSFSheet sheet = this.sheets.get(sheetIndex);
        if (Objects.nonNull(data) && Objects.isNull(sheetBiFunction)) {
            int numberOfRows = sheet.getPhysicalNumberOfRows();
            HSSFRow sheetRow = sheet.createRow(numberOfRows);
            HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            List<Integer> cellIndex = data.keySet().stream().collect(Collectors.toList());
            for (int i = 0; i < cellIndex.size(); i++) {
                int index = cellIndex.get(i);
                HSSFCell cell = sheetRow.createCell(index);
                cell.setCellValue(data.get(index));
                cell.setCellStyle(cellStyle);
            }
        }
        sheets.add(sheetIndex, Objects.isNull(sheetBiFunction) ? sheet : sheetBiFunction.apply(sheet, data));
        return this;
    }


    /**
     * <per>
     * <p>sheet页数据填充</p>
     * <per/>
     *
     * @param headDetails     表头[必填]
     * @param list            数据[必填]
     * @param sheetBiFunction
     * @return com.hhwy.pwps.managepointins.service.impl.WorkbookUtil
     * @throws
     * @Description : TODO
     * @author Liruilong
     * @Date 2021-01-21 8:42
     **/
    public WorkbookUtil setSheetData(HeadDetails headDetails, List<?> list, BiFunction<HSSFSheet, List<?>, HSSFSheet> sheetBiFunction) {
        HSSFSheet sheet = this.sheets.get(sheetIndex);
        List<List<String>> sheetData = new ArrayList<List<String>>();
        if (Objects.nonNull(list) && Objects.isNull(sheetBiFunction)) {
            //准备数据
            list.stream().forEach(o -> {
                String jsonString = JSONObject.toJSONStringWithDateFormat(o, "yyyy-MM-dd HH:mm:ss");
                // 将单行数据转化为JSON串。
                JSONObject json = JSONObject.parseObject(jsonString);
                List<String> collect = headDetails.builder().stream().map(headDetail -> {
                    String key = headDetail.getKey();
                    Object obj = json.getObject(key, Object.class);
                    if (obj instanceof String) {
                        return obj.toString();
                    } else if (obj instanceof BigDecimal) {
                        return ((BigDecimal) obj).stripTrailingZeros().toPlainString();
                    } else {
                        if (obj != null) {
                            return obj.toString();
                        } else {
                            return " ";
                        }
                    }
                }).collect(Collectors.toList());
                sheetData.add(collect);
            });
            int numberOfRows = sheet.getPhysicalNumberOfRows();
            HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //数据解析
            for (int rowIndex = 0; rowIndex < sheetData.size(); rowIndex++) {
                HSSFRow sheetRow = sheet.createRow(numberOfRows + rowIndex);
                List<String> cells = sheetData.get(rowIndex);
                for (int cellIndex = 0; cellIndex < cells.size(); cellIndex++) {
                    HSSFCell cell = sheetRow.createCell(cellIndex);
                    cell.setCellValue(cells.get(cellIndex));
                    if (headDetails.builder().get(cellIndex).isCenter()) {
                        cell.setCellStyle(cellStyle);
                    }
                }
            }
        }
        sheets.add(sheetIndex, Objects.isNull(sheetBiFunction) ? sheet : sheetBiFunction.apply(sheet, list));
        return this;
    }

    /**
     * <per>
     * <p>shell页表尾初始化</p>
     * <per/>
     *
     * @param sheetFoot       表尾[可选]
     * @param sheetBiFunction
     * @return com.hhwy.pwps.managepointins.service.impl.WorkbookUtil
     * @throws
     * @Description : TODO
     * @author Liruilong
     * @Date 2021-01-20 22:19
     **/
    public WorkbookUtil initSheetFoot(String sheetFoot, BiFunction<HSSFSheet, String, HSSFSheet> sheetBiFunction) {
        HSSFSheet sheet = this.sheets.get(sheetIndex);
        if (Objects.nonNull(sheetFoot) && Objects.isNull(sheetBiFunction)) {
            HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            HSSFFont font = hssfWorkbook.createFont();
            int numberOfRows = sheet.getPhysicalNumberOfRows();
            int numberOfCells = sheet.getRow(numberOfRows - 1).getPhysicalNumberOfCells();
            HSSFRow sheetRow = sheet.createRow(numberOfRows);
            sheetRow.setHeight((short) 800);
            font.setFontHeightInPoints((short) 8);
            font.setFontName("宋体");
            cellStyle.setFont(font);
            HSSFCell cell = sheetRow.createCell(0);
            cell.setCellValue(sheetFoot);
            cell.setCellStyle(cellStyle);
            sheet.addMergedRegionUnsafe(new CellRangeAddress(numberOfRows, numberOfRows, 0, numberOfCells - 1));
        }
        sheets.add(sheetIndex, Objects.isNull(sheetBiFunction) ? sheet : sheetBiFunction.apply(sheet, sheetFoot));
        return this;
    }


    /**
     * <per>
     * <p>shell页表头的初始化,可以传入BiFunction自定义初始化逻辑,使用默认值，传入null</p>
     * <per/>
     *
     * @param headDetails     表头[必填]
     * @param sheetTitle      标题[可选]
     * @param projectName     工程名称[可选]
     * @param sheetBiFunction 初始化逻辑
     * @return org.apache.poi.hssf.usermodel.HSSFSheet
     * @throws
     * @Description : TODO
     * @author Liruilong
     * @Date 2021-01-20 17:52
     **/
    public WorkbookUtil initSheetHead(HeadDetails headDetails, String sheetTitle, String projectName, BiFunction<HSSFSheet, HeadDetails, HSSFSheet> sheetBiFunction) {
        //行索引
        int rowIndex = 0;
        HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
        HSSFRow sheetRow = null;
        HSSFSheet sheet = this.sheets.get(sheetIndex);
        // 有标题行时
        if (Objects.nonNull(sheetTitle)) {
            HSSFCellStyle cellStyleTitle = hssfWorkbook.createCellStyle();
            HSSFFont fontTitle = hssfWorkbook.createFont();
            //对齐方式
            cellStyleTitle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
            cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
            fontTitle.setFontName("宋体");
            //大小
            fontTitle.setFontHeightInPoints((short) 21);
            cellStyleTitle.setFont(fontTitle);
            sheetRow = sheet.createRow(rowIndex++);
            sheetRow.setHeight((short) 800);
            HSSFCell cell = sheetRow.createCell(0);
            cell.setCellValue(sheetTitle);
            cell.setCellStyle(cellStyleTitle);
            // 合并单元格
            sheet.addMergedRegionUnsafe(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, headDetails.headSize() - 1));
        }
        // 有项目工程名的时
        if (Objects.nonNull(projectName)) {
            HSSFCellStyle cellStyleName = hssfWorkbook.createCellStyle();
            HSSFFont fontName = hssfWorkbook.createFont();
            sheetRow = sheet.createRow(rowIndex++);
            HSSFCell cell = sheetRow.createCell(0);
            fontName.setBold(true);
            fontName.setFontHeightInPoints((short) 10);
            fontName.setFontName("微软雅黑");
            cellStyleName.setAlignment(HorizontalAlignment.LEFT);
            cellStyleName.setFont(fontName);
            cell.setCellStyle(cellStyleName);
            cell.setCellValue(projectName);
            // 合并单元格
            sheet.addMergedRegionUnsafe(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, headDetails.headSize() - 1));
        }


        sheetRow = sheet.createRow(rowIndex++);
        List<HeadDetails.HeadDetail> builder = headDetails.builder();
        HSSFFont fontHead = hssfWorkbook.createFont();
        fontHead.setBold(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        fontHead.setFontHeightInPoints((short) 10);
        fontHead.setFontName("微软雅黑");
        cellStyle.setFont(fontHead);
        for (int i = 0; i < headDetails.headSize(); i++) {
            HSSFCell cell = sheetRow.createCell(i);
            //填充单元格数据
            HeadDetails.HeadDetail headDetail = builder.get(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(headDetail.getTitle());
            sheet.setColumnWidth(i, headDetail.getWidth() * 200);
        }
        sheets.add(sheetIndex, Objects.isNull(sheetBiFunction) ? sheet : sheetBiFunction.apply(sheet, headDetails));
        return this;
    }


    /**
     * <per>
     * <p>sheet页初始化，设置默认值，可以传入BiFunction自定义初始化逻辑,使用默认值，传入null</p>
     * <per/>
     *
     * @param sheetName       sheet页名称[可选]
     * @param sheetBiFunction 自定义sheet页规则，使用默认传入 null
     * @return org.apache.poi.hssf.usermodel.HSSFSheet
     * @throws
     * @Description : TODO
     * @author Liruilong
     * @Date 2021-01-20 16:23
     **/
    public WorkbookUtil initSheet(String sheetName, BiFunction<HSSFWorkbook, String, HSSFSheet> sheetBiFunction) {
        HSSFSheet sheet = this.hssfWorkbook.createSheet(Optional.ofNullable(sheetName).orElse("sheet页XX"));
        //创建默认样式
        sheet.setDefaultColumnWidth(15);
        sheet.setDefaultRowHeight((short) 300);

        sheets.add(sheetIndex == 0 ? sheetIndex : ++sheetIndex, Objects.isNull(sheetBiFunction) ? sheet : sheetBiFunction.apply(this.hssfWorkbook, sheetName));
        return this;
    }


    /**
     * <per>
     * <p>Workbook 初始化</p>
     * <per/>
     *
     * @param setCategory 文档类别[可选]
     * @param setManager  文档管理员[可选]
     * @param setCompany  设置公司信息[可选]
     * @param setTitle    文档标题[可选]
     * @param setAuthor   文档作者[可选]
     * @param setComments 文档备注[可选]
     * @return com.hhwy.pwps.managepointins.service.impl.ManagePointIn.WorkbookUtil
     * @throws
     * @Description : TODO
     * @author Liruilong
     * @Date 2021-01-20 16:27
     **/
    public WorkbookUtil initWorkbook(String setCategory, String setManager, String setCompany, String setTitle, String setAuthor, String setComments) {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //文档类别
        docInfo.setCategory(Optional.ofNullable(setCategory).orElse("配网输出报表"));
        //文档管理员
        docInfo.setManager(Optional.ofNullable(setManager).orElse("配网工程评审平台"));
        //设置公司信息
        docInfo.setCompany(Optional.ofNullable(setCompany).orElse("XXXXXX"));
        docInfo.setDocumentVersion("1.0");
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle(Optional.ofNullable(setTitle).orElse("配网输出报表"));
        //文档作者
        summInfo.setAuthor(Optional.ofNullable(setAuthor).orElse("配网工程评审平台"));
        // 创建时间
        summInfo.setCreateDateTime(new Date());
        // 文档备注
        summInfo.setComments(Optional.ofNullable(setComments).orElse(LocalDateTime.now().toString() + " 配网工程评审平台导出"));
        this.hssfWorkbook = workbook;
        return this;
    }


    public WorkbookUtil initWorkbook() {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        this.hssfWorkbook = workbook;
        return this;
    }


    /**
     * <per>
     * <p>Excel以字节数组输出</p>
     * <per/>
     *
     * @param
     * @return byte[]
     * @throws
     * @Description : TODO Output byte array
     * @author Liruilong
     * @Date 2021-01-21 8:44
     **/
    public byte[] builderByte() {
        return hssfWorkbook.getBytes();
    }


    /**
     * <per>
     * <p>Excel以application/octet-stream形式输出，返回二进制的报文实体</p>
     * <per/>
     *
     * @param
     * @return org.springframework.http.ResponseEntity<byte       [       ]>
     * @throws
     * @Description : TODO
     * @author Liruilong
     * @Date 2021-01-21 8:43
     **/
    public ResponseEntity<byte[]> builderResponseEntity() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            hssfWorkbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }



}

