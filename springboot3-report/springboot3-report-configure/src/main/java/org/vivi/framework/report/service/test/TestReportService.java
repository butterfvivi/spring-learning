package org.vivi.framework.report.service.test;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.vivi.framework.report.service.common.base.MesExportExcel;
import org.vivi.framework.report.service.common.base.MesSheetConfig;
import org.vivi.framework.report.service.common.enums.LuckySheetPropsEnum;
import org.vivi.framework.report.service.common.utils.ListUtil;
import org.vivi.framework.report.service.common.utils.ReportExcelUtil;
import org.vivi.framework.report.service.web.dto.reporttpl.ResPreviewData;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestReportService {

    private ResPreviewData generateResPreviewData(String fileName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new IOException("File not found: " + fileName);
            }
            return objectMapper.readValue(inputStream, ResPreviewData.class);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void handleExportExcel(HttpServletResponse httpServletResponse) throws Exception {

        ResPreviewData resPreviewData = generateResPreviewData("param/chart_preview_data.json");

                MesExportExcel mesExportExcel = new MesExportExcel();
        List<MesSheetConfig> sheetConfigs = new ArrayList<MesSheetConfig>();
        if(!ListUtil.isEmpty(resPreviewData.getSheetDatas()))
        {
            for (int i = 0; i < resPreviewData.getSheetDatas().size(); i++) {
                MesSheetConfig mesSheetConfig = new MesSheetConfig();
                Object borderInfos = null;
                Map<String, Object> rowlen = null;
                Map<String, Object> columnlen = null;
                JSONObject authority = null;
                if(resPreviewData.getSheetDatas().get(i).getConfig() != null)
                {
                    borderInfos = resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.BORDERINFO.getCode());
                    rowlen = (Map<String, Object>) resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.ROWLEN.getCode());//行高
                    columnlen = (Map<String, Object>) resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.COLUMNLEN.getCode());//列宽
                    authority = JSON.parseObject(JSON.toJSONString(resPreviewData.getSheetDatas().get(i).getConfig().get("authority")));
                }
                List<Object> borderInfoList = null;
                if(borderInfos != null)
                {
                    borderInfoList = (List<Object>) borderInfos;
                }
                mesSheetConfig.setCellDatas(resPreviewData.getSheetDatas().get(i).getCellDatas());
                mesSheetConfig.setSheetname(resPreviewData.getSheetDatas().get(i).getSheetName());
                mesSheetConfig.setBorderInfos(borderInfoList);
                mesSheetConfig.setRowlen(rowlen);
                mesSheetConfig.setColumnlen(columnlen);
                mesSheetConfig.setFrozen(JSONObject.parseObject(JSON.toJSONString(resPreviewData.getSheetDatas().get(i).getFrozen())));
                mesSheetConfig.setImages(JSONObject.parseObject(JSON.toJSONString(resPreviewData.getSheetDatas().get(i).getImages())));
                mesSheetConfig.setHyperlinks(resPreviewData.getSheetDatas().get(i).getHyperlinks());
                mesSheetConfig.setBase64Images(resPreviewData.getSheetDatas().get(i).getBase64Imgs());
                mesSheetConfig.setImageDatas(resPreviewData.getSheetDatas().get(i).getImageDatas());
                mesSheetConfig.setChart(resPreviewData.getSheetDatas().get(i).getChart());
                mesSheetConfig.setChartCells(resPreviewData.getSheetDatas().get(i).getChartCells());
                mesSheetConfig.setColhidden(resPreviewData.getSheetDatas().get(i).getColhidden());
                mesSheetConfig.setRowhidden(resPreviewData.getSheetDatas().get(i).getRowhidden());
                mesSheetConfig.setDataVerification(resPreviewData.getSheetDatas().get(i).getDataVerification());
                mesSheetConfig.setAuthority(authority);
                mesSheetConfig.setLuckysheetConditionformatSave(resPreviewData.getSheetDatas().get(i).getLuckysheetConditionformatSave());
                if(resPreviewData.getSheetDatas().get(i).getMaxXAndY() != null)
                {
                    mesSheetConfig.setMaxXAndY(resPreviewData.getSheetDatas().get(i).getMaxXAndY());
                }else {
                    Map<String, Integer> maxXAndY = new HashMap<>();
                    maxXAndY.put("maxX", 50);
                    maxXAndY.put("maxY", 26);
                    mesSheetConfig.setMaxXAndY(maxXAndY);
                }
                sheetConfigs.add(mesSheetConfig);
            }
        }
        mesExportExcel.setSheetConfigs(sheetConfigs);
        //mesExportExcel.setChartsBase64(mesGenerateReportDto.getChartsBase64());
        ReportExcelUtil.export(mesExportExcel,"图表报表",null,httpServletResponse);
    }
}
