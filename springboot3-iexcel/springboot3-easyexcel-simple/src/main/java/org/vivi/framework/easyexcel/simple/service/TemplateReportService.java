package org.vivi.framework.easyexcel.simple.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.vivi.framework.easyexcel.simple.model.dto.ScreenExcelDTO;
import org.vivi.framework.easyexcel.simple.model.dto.ScreenGatherDTO;
import org.vivi.framework.easyexcel.simple.model.dto.ScreenValueExcelDTO;
import org.vivi.framework.easyexcel.simple.service.strategy.ScreenScoreHeaderMergeStrategy;
import org.vivi.framework.easyexcel.simple.service.strategy.ScreenScoreValueMergeStrategy;
import org.vivi.framework.easyexcel.simple.service.strategy.ScreenValueMergeStrategy;
import org.vivi.framework.easyexcel.simple.service.strategy.ScreenScoreValueHorizontalMergeStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TemplateReportService {

    public void export(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        // HttpServletResponse消息头参数设置
        this.setHttpServletResponse(httpServletResponse);
        // 需要导出的临时模板文件
        InputStream screenTemporaryTemplate = null;
        // 通过ClassPathResource获取/resources/template下的模板文件
        ClassPathResource classPathResource = new ClassPathResource("template/screenTemplate.xlsx");
        // 这里使用try-with-resource
        try (
                // 获取模板文件
                InputStream screenTemplateFileName = classPathResource.getInputStream();

                OutputStream screenOut = httpServletResponse.getOutputStream();
                BufferedOutputStream screenBos = new BufferedOutputStream(screenOut);

                ByteArrayOutputStream screenTemplateOut = new ByteArrayOutputStream();
        ) {
            // --------------------------------基本配置--------------------------------
            // 设置内容的策略
            WriteCellStyle contentWriteCellStyle = this.getWriteCellStyle();
            // 配置横向填充
            FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
            // sheet名称，注意要和Excel模板中的工作表名称相同，不然无法导出数据
            WriteSheet writeSheet = EasyExcel.writerSheet("sheet0").build();
            // --------------------------------基本配置--------------------------------


            // ---------------------模拟获取第一部分的表格数据、表头参数---------------------
            // 构造第一部分产品数据
            List<ScreenGatherDTO> screenGatherDTOList = this.getScreenGatherDTOList();
            // 填充第一个表头的单元格
            List<ScreenValueExcelDTO> screenValueExcelDTOList = this.getScreenValueExcelDTOList();
            // 在屏规格末尾加上表头模板参数
            List<ScreenValueExcelDTO> screenTableExcelDTOList = this.getScreenTableExcelDTOList();
            // ---------------------模拟获取第一部分的表格数据、表头参数---------------------


            // --------------------------------第一次导出--------------------------------
            ExcelWriter screenTemplateExcelWriter = EasyExcel
//                    .write(screenBos)   // 导出临时文件，使用的是BufferedOutputStream
                    .write(screenTemplateOut)   // 导出最终临时文件，使用的是ByteArrayOutputStream
                    .withTemplate(screenTemplateFileName)  // 使用的模板
                    .registerWriteHandler(new ScreenValueMergeStrategy(screenGatherDTOList, 1, 6, 5))   // 自定义单元格合并策略
                    .registerWriteHandler(new HorizontalCellStyleStrategy(null, contentWriteCellStyle)) // 只配置内容策略，头部为null
                    .build();
            // 填充屏规格表格数据
            screenTemplateExcelWriter.fill(new FillWrapper("screenGatherDTOList", screenGatherDTOList), writeSheet);
            // 填充第一个表头的单元格
            screenTemplateExcelWriter.fill(new FillWrapper("screenValueExcelDTOList", screenValueExcelDTOList), writeSheet);
            // 填充表头模板参数
            screenTemplateExcelWriter.fill(new FillWrapper("screenTableExcelDTOList", screenTableExcelDTOList), writeSheet);
            screenTemplateExcelWriter.finish();

            // excel导出成流
            byte[] bytes = screenTemplateOut.toByteArray();
            screenTemporaryTemplate = new ByteArrayInputStream(bytes);
            // --------------------------------第一次导出--------------------------------


            // --------------------------模拟获取第二部分数据--------------------------
            // 模拟获取第二部分的表头数据
            List<ScreenExcelDTO> screenScoreExcelDTOList = this.getScoreExcelDTOList();
            // 模拟获取第二部分的表格数据
            Map<String, List<ScreenValueExcelDTO>> screenScoreMap = this.getScreenScoreMap(screenGatherDTOList);
            // --------------------------模拟获取第二部分的数据--------------------------


            // --------------------------------第二次导出--------------------------------
            // 测评模型表格数据开始索引
            int screenScoreCostFirstColumnIndex = 7;
            // 测评模型表格数据结束索引
            int screenScoreTargetEndColIndex = screenScoreCostFirstColumnIndex + screenScoreMap.get("screenValueTemplateParam0").size() - 1;
            // 导出最终文件
            ExcelWriter screenScoreExcelWriter = EasyExcel
                    .write(screenBos)   // 导出最终文件
                    .withTemplate(screenTemporaryTemplate)  // 以第一次导出的excel流，作为第二次导出的模板
                    .registerWriteHandler(new ScreenScoreHeaderMergeStrategy(0, screenScoreExcelDTOList, 1, screenScoreCostFirstColumnIndex))   // 表头（模块）合并策略
                    .registerWriteHandler(new ScreenScoreHeaderMergeStrategy(1, screenScoreExcelDTOList, 2, screenScoreCostFirstColumnIndex))   // 表头合并策略
                    .registerWriteHandler(new ScreenScoreValueMergeStrategy(screenGatherDTOList, screenScoreCostFirstColumnIndex, screenScoreTargetEndColIndex, 5)) // 表格数据合并策略
                    .registerWriteHandler(new ScreenScoreValueHorizontalMergeStrategy(screenGatherDTOList, screenScoreExcelDTOList, screenScoreCostFirstColumnIndex))   // 表格数据横向合并策略
                    .registerWriteHandler(new HorizontalCellStyleStrategy(null, contentWriteCellStyle)) // 只配置内容策略，头部为null
                    .build();
            // 填充测评模型表头数据
            screenScoreExcelWriter.fill(new FillWrapper("screenTableExcelDTOList", screenScoreExcelDTOList), fillConfig, writeSheet);
            // 填充测评模型表格数据
            screenScoreMap.forEach((k, v) -> screenScoreExcelWriter.fill(new FillWrapper(k, v), fillConfig, writeSheet));
            screenScoreExcelWriter.finish();
            // --------------------------------第二次导出--------------------------------
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 防止出现异常，导致流关闭失败
            if (screenTemporaryTemplate != null) {
                try {
                    screenTemporaryTemplate.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 模拟获取第二部分的表格数据
     *
     * @param screenGatherDTOList
     * @return
     */
    private Map<String, List<ScreenValueExcelDTO>> getScreenScoreMap(List<ScreenGatherDTO> screenGatherDTOList) {
        // 模拟这部分数据的字段没有落表，采用Map存储和处理数据
        Map<String, List<ScreenValueExcelDTO>> screenScoreMap = new LinkedHashMap<>();
        if (CollectionUtils.isNotEmpty(screenGatherDTOList)) {
            for (int j = 0; j < screenGatherDTOList.size(); j++) {
                Map<String, String> map = new LinkedHashMap<>();
                if ((j + 1) % 4 == 0) {
                    map.put("项目", "总得分");
                } else if ((j + 1) % 3 == 0) {
                    map.put("项目", "总权重得分");
                } else {
                    map.put("项目", "实测值");
                }
                for (int i = 1; i <= 1; i++) {
                    map.put("光学测试子项" + i, "1");
                }
                for (int i = 1; i <= 2; i++) {
                    map.put("电学测试子项" + i, "2");
                }
                for (int i = 1; i <= 3; i++) {
                    map.put("声学测试子项" + i, "3");
                }
                screenGatherDTOList.get(j).setScoreMap(map);
            }

            for (int i = 0; i < screenGatherDTOList.size(); i++) {
                List<ScreenValueExcelDTO> valueExcelDTOList = new ArrayList<>();
                Map<String, String> scoreMap = screenGatherDTOList.get(i).getScoreMap();
                if (MapUtils.isNotEmpty(scoreMap)) {
                    scoreMap.forEach((k, v) -> {
                        ScreenValueExcelDTO valueExcelDTO = new ScreenValueExcelDTO();
                        valueExcelDTO.setValue(v);
                        valueExcelDTOList.add(valueExcelDTO);
                    });
                }
                // 填充表格数据
                screenScoreMap.put("screenValueTemplateParam" + i, valueExcelDTOList);
            }
        }
        return screenScoreMap;
    }

    /**
     * 模拟获取第二部分的表头数据
     *
     * @return
     */
    private List<ScreenExcelDTO> getScoreExcelDTOList() {
        List<ScreenExcelDTO> screenScoreExcelDTOList = new ArrayList<>();
        for (int i = 1; i <= 1; i++) {
            ScreenExcelDTO screenExcelDTO = new ScreenExcelDTO();
            screenExcelDTO.setModelName("产品测试");
            screenExcelDTO.setTestItemCategory("光学");
            screenExcelDTO.setTestItemName("光学");
            screenExcelDTO.setSubTestItemName("光学测试子项" + i);
            screenScoreExcelDTOList.add(screenExcelDTO);
        }
        for (int i = 1; i <= 2; i++) {
            ScreenExcelDTO screenExcelDTO = new ScreenExcelDTO();
            screenExcelDTO.setModelName("产品测试");
            screenExcelDTO.setTestItemCategory("电学");
            screenExcelDTO.setTestItemName("电学");
            screenExcelDTO.setSubTestItemName("电学测试子项" + i);
            screenScoreExcelDTOList.add(screenExcelDTO);
        }
        for (int i = 1; i <= 3; i++) {
            ScreenExcelDTO screenExcelDTO = new ScreenExcelDTO();
            screenExcelDTO.setModelName("产品测试");
            screenExcelDTO.setTestItemCategory("声学");
            screenExcelDTO.setTestItemName("声学");
            screenExcelDTO.setSubTestItemName("声学测试子项" + i);
            screenScoreExcelDTOList.add(screenExcelDTO);
        }
        return screenScoreExcelDTOList;
    }

    /**
     * 在屏规格末尾加上表头模板参数
     *
     * @return
     */
    private List<ScreenValueExcelDTO> getScreenTableExcelDTOList() {
        List<ScreenValueExcelDTO> screenTableExcelDTOList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ScreenValueExcelDTO screenTableExcelDTO = new ScreenValueExcelDTO();
            switch (i) {
                case 0:
                    screenTableExcelDTO.setValue("{screenTableExcelDTOList.modelName}");
                    break;
                case 1:
                    screenTableExcelDTO.setValue("{screenTableExcelDTOList.testItemCategory}");
                    break;
                case 2:
                    screenTableExcelDTO.setValue("{screenTableExcelDTOList.testItemName}");
                    break;
                case 3:
                    screenTableExcelDTO.setValue("{screenTableExcelDTOList.subTestItemName}");
                    break;
                default:
                    break;
            }
            screenTableExcelDTOList.add(screenTableExcelDTO);
        }
        return screenTableExcelDTOList;
    }

    /**
     * 填充第一个表头的单元格
     *
     * @return
     */
    private List<ScreenValueExcelDTO> getScreenValueExcelDTOList() {
        ScreenValueExcelDTO screenValueExcelDTO = new ScreenValueExcelDTO();
        List<ScreenValueExcelDTO> screenValueExcelDTOList = new ArrayList<>();
        screenValueExcelDTO.setValue("产品测试");
        screenValueExcelDTOList.add(screenValueExcelDTO);
        return screenValueExcelDTOList;
    }

    /**
     * 构造第一部分产品数据
     *
     * @return
     */
    private List<ScreenGatherDTO> getScreenGatherDTOList() {
        List<ScreenGatherDTO> screenGatherDTOList = new ArrayList<>();
        // 构造5个产品数据
        for (int i = 1; i <= 5; i++) {
            // 每份数据乘以4，为了合并单元格做准备
            for (int j = 0; j < 4; j++) {
                ScreenGatherDTO screenGatherDTO = new ScreenGatherDTO();
                screenGatherDTO.setScreenSize(String.valueOf(i * 10));
                screenGatherDTO.setSupplier("厂商" + i);
                screenGatherDTO.setPartMode("型号" + i);
                screenGatherDTO.setResolution("1080P");
                screenGatherDTO.setRefreshRate("60Hz");
                screenGatherDTO.setPanel("IPS");
                screenGatherDTOList.add(screenGatherDTO);
            }
        }
        if (CollectionUtils.isNotEmpty(screenGatherDTOList)) {
            for (int i = 0; i < screenGatherDTOList.size(); i++) {
                // 在屏规格末尾加上表格模板参数
                screenGatherDTOList.get(i).setValueTemplateParam("{screenValueTemplateParam" + i + ".value}");
            }
        }
        return screenGatherDTOList;
    }

    /**
     * 设置内容的策略
     *
     * @return
     */
    private WriteCellStyle getWriteCellStyle() {
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 设置内容水平居中对齐
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 设置内容垂直居中对齐
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 自动换行
        contentWriteCellStyle.setWrapped(true);
        // 设置字体样式和大小
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontHeightInPoints((short) 12);
        contentWriteFont.setFontName("微软雅黑");
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        return contentWriteCellStyle;
    }

    /**
     * HttpServletResponse消息头参数设置
     *
     * @param httpServletResponse
     */
    private void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        String filename = "exportFile.xlsx";
        httpServletResponse.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename);
        httpServletResponse.setContentType("application/octet-stream;charset=UTF-8");
        httpServletResponse.addHeader("Pragma", "no-cache");
        httpServletResponse.addHeader("Cache-Control", "no-cache");
    }
}
