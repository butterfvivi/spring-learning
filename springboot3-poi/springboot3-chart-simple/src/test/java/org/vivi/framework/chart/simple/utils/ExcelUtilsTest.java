//package org.vivi.framework.chart.simple.utils;
//
//import com.alibaba.fastjson2.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.poi.xssf.usermodel.XSSFCellStyle;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.vivi.framework.chart.simple.common.response.R;
//
//import java.io.FileOutputStream;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//public class ExcelUtilsTest {
//
//    /**
//     * 按用户统计数据导出
//     *
//     * @param condition
//     * @return
//     */
//    public R<String> exportExcelByUser(EventAlarmStaticsCondition condition) {
//        // 查询数据
//        R<List<JSONObject>> response = eventAlarmStaticsByUser(condition);
//        if(response.checkFailed() || CollectionUtils.isEmpty(response.getData())){
//            return R.success("");
//        }
//
//        List<String> heads = new ArrayList<>();
//        // 获取要导出的表头数据
//        List<EventTypeEntity> typeList = getEventTypeEntityList();
//        List<String> titles = typeList.stream().map(EventTypeEntity::getEventName).collect(Collectors.toList());
//        heads.add(Constants.NAME);
//        heads.add(Constants.TOTAL_EVENT);
//        heads.add(Constants.AVG_TIME);
//        heads.addAll(titles);
//
//        List<JSONObject> objectList = response.getData();
//        // 柱状图y轴事件总数数据
//        List<Double> lineYDataList = new ArrayList<>();
//        // 柱状图y轴平均处理时长数据
//        List<Double> timeYDataList = new ArrayList<>();
//        // 填充折线图y轴数据
//        for (JSONObject jsonObject : objectList) {
//            lineYDataList.add(jsonObject.getDoubleValue(Constants.TOTAL_COUNT));
//            timeYDataList.add(jsonObject.getDoubleValue(Constants.TOTAL_AVG_TIME));
//        }
//
//        // 创建wb并绘制折线图、饼图、柱状图
//        XSSFWorkbook wb = new XSSFWorkbook();
//        XSSFSheet sheet = wb.createSheet(ExportFileConstant.eventStaticsNamePrefix);
//        drawSheetTable(wb, sheet, heads, response.getData(), Constants.USER_NAME);
//
//        // 柱状图
//        ExcelUtils.createBarChart(sheet, lineYDataList, ExportFileConstant.staticsCount, 1, objectList.size(), 0, 0, 4, 30, 0, 10 );
//        ExcelUtils.createBarChart(sheet, timeYDataList, ExportFileConstant.staticsEfficiency, 1, objectList.size(), 0, 0, 4, 30, 11, 22 );
//        // 上传文件
//        String url = upload(wb, ExportFileConstant.eventStaticsNamePrefix);
//        return R.success(url);
//    }
//
//    /**
//     * 上传文件
//     *
//     * @param wb
//     * @param fileName
//     * @return
//     */
//    private String upload (XSSFWorkbook wb, String fileName){
//        String filePath = fileConfig.getCreatFilePath() + fileName + ExcelTypeEnum.XLSX.getValue();
//        try {
//            DownloadUtil.createFolderIfNotExists(fileConfig.getCreatFilePath());
//            FileOutputStream outputStream = new FileOutputStream(filePath);
//            wb.write(outputStream);
//            outputStream.close();
//        } catch (Exception e) {
//            log.error("Failed to upload data，exception:{}", e);
//            return null;
//        }
//        //上传到s3
//        return s3Service.fileUpload(filePath, fileName + ExcelTypeEnum.XLSX.getValue(), S3Tag.shortTerm);
//    }
//
//
//    /**
//     * sheet数据
//     *
//     * @param sheet
//     * @param titleList
//     * @param dataList
//     * @return
//     */
//    private boolean drawSheetTable(XSSFWorkbook wb, XSSFSheet sheet, List<String> titleList, List<JSONObject> dataList, String groupType) {
//        boolean result = true;
//
//        // 设置样式以及字体样式
//        XSSFCellStyle headerStyle = ExcelUtils.createHeadCellStyle(wb);
//        XSSFCellStyle contentStyle = ExcelUtils.createContentCellStyle(wb);
//        XSSFCellStyle intStyle = ExcelUtils.createContentNumericalCellStyle(wb,"#,##0");
//
//        // 获取事件类型数据
//        List<EventTypeEntity> typeList = getEventTypeEntityList();
//        List<Integer> countList = new ArrayList<>();
//        for (EventTypeEntity type : typeList) {
//            String eventType = Constants.EVENT_TYPE + type.getEventType();
//            int count = 0;
//            for (JSONObject jsonObject : dataList) {
//                count += jsonObject.getIntValue(eventType);
//            }
//            countList.add(count);
//        }
//        countList.add(dataList.stream().mapToInt(jsonObject -> jsonObject.getIntValue(Constants.TOTAL_COUNT)).sum());
//        countList.add(dataList.stream().mapToInt(jsonObject -> jsonObject.getIntValue(Constants.TOTAL_AVG_TIME)).sum());
//
//        // 根据数据创建excel第一行标题行
//        XSSFRow row0 = sheet.createRow(0);
//        for (int i = 0; i < titleList.size(); i++) {
//            // 设置标题
//            row0.createCell(i).setCellValue(titleList.get(i));
//            // 设置标题行样式
//            row0.getCell(i).setCellStyle(headerStyle);
//        }
//        // 填充数据
//        String period = Constants.PERIOD.equals(groupType) ? Constants.PERIOD : Constants.USER_NAME;
//        for (int k = 0; k < dataList.size(); k++) {
//            // 获取每一项的数据
//            JSONObject data = dataList.get(k);
//            // 设置每一行的字段标题和数据
//            XSSFRow row = sheet.createRow(k + 1);
//            // 标题
//            row.createCell(0).setCellValue(data.getString(period));
//            sheet.getRow(k + 1).getCell(0).setCellStyle(contentStyle);
//            row.createCell(1).setCellValue(data.getString(Constants.TOTAL_COUNT));
//            sheet.getRow(k + 1).getCell(1).setCellStyle(contentStyle);
//            // 处理数字过长转成科学计数法问题
//            BigDecimal avgTimeDecimal = new BigDecimal(data.getString(Constants.TOTAL_AVG_TIME));
//            row.createCell(2).setCellValue(avgTimeDecimal.toPlainString());
//            sheet.getRow(k + 1).getCell(2).setCellStyle(contentStyle);
//            // 设置左边字段样式
//            for (int j = 0; j < typeList.size(); j++) {
//                EventTypeEntity entity = typeList.get(j);
//                String eventTYpeData = data.getString(Constants.EVENT_TYPE + entity.getEventType());
//                row.createCell(j + 3).setCellValue(eventTYpeData);
//                // 设置数据样式
//                sheet.getRow(k + 1).getCell(j + 3).setCellStyle(intStyle);
//            }
//        }
//
//        // 合计
//        XSSFRow endRow = sheet.createRow(dataList.size() + 1);
//        endRow.createCell(0).setCellValue(Constants.TOTAL);
//        endRow.getCell(0).setCellStyle(contentStyle);
//        // 事件总数
//        endRow.createCell(1).setCellValue(countList.get(countList.size() - 2));
//        endRow.getCell(1).setCellStyle(contentStyle);
//        // 处理科学计数法问题
//        BigDecimal bigDecimal = new BigDecimal(countList.get(countList.size() - 1));
//        endRow.createCell(2).setCellValue(bigDecimal.toPlainString());
//        endRow.getCell(2).setCellStyle(contentStyle);
//        for (int i = 3; i <= countList.size(); i++) {
//            endRow.createCell(i).setCellValue(countList.get(i - 3));
//            endRow.getCell(i).setCellStyle(contentStyle);
//        }
//        return result;
//    }
//}
