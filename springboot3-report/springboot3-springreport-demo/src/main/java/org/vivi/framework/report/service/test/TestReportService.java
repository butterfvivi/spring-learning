package org.vivi.framework.report.service.test;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.report.service.common.base.MesExportExcel;
import org.vivi.framework.report.service.common.base.MesSheetConfig;
import org.vivi.framework.report.service.common.constants.Constants;
import org.vivi.framework.report.service.common.constants.StatusCode;
import org.vivi.framework.report.service.common.enums.LuckySheetPropsEnum;
import org.vivi.framework.report.service.common.exception.BizException;
import org.vivi.framework.report.service.common.utils.DocumentToLuckysheetUtil;
import org.vivi.framework.report.service.common.utils.ListUtil;
import org.vivi.framework.report.service.common.utils.MessageUtil;
import org.vivi.framework.report.service.common.utils.ReportExcelUtil;
import org.vivi.framework.report.service.model.luckysheet.Luckysheet;
import org.vivi.framework.report.service.model.luckysheetcell.LuckysheetCell;
import org.vivi.framework.report.service.model.luckysheethis.LuckysheetHis;
import org.vivi.framework.report.service.web.dto.reporttpl.ResPreviewData;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
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

    public JSONArray uploadExcel(MultipartFile file) throws Exception {
        int sheetSize = 0;

        JSONArray result = null;
        if(file.getOriginalFilename().endsWith(".xlsx")) {
            result =  DocumentToLuckysheetUtil.xlsx2Luckysheet(file);
        }else if(file.getOriginalFilename().endsWith(".csv")){
            result =  DocumentToLuckysheetUtil.csv2Luckysheet(file);
        }

        if(!ListUtil.isEmpty(result))
        {
            List<LuckysheetHis> list = new ArrayList<>();
            for (int i = 0; i < result.size(); i++) {
                LuckysheetHis luckysheetHis = new LuckysheetHis();
                JSONObject sheetData = result.getJSONObject(i);

                Luckysheet luckysheet = new Luckysheet();
                luckysheet.setId(IdWorker.getId());
                luckysheet.setRowSize(sheetData.getIntValue("row"));
                luckysheet.setColumnSize(sheetData.getIntValue("column"));
                luckysheet.setSheetIndex(sheetData.getString("index"));
                luckysheet.setStatus(sheetData.getIntValue("status"));
                luckysheet.setSheetOrder(sheetData.getIntValue("order")+sheetSize);
                luckysheet.setSheetName(sheetData.getString("name"));
                luckysheet.setHide(sheetData.getIntValue("hide"));
                luckysheet.setMergeInfo(JSON.toJSONString(sheetData.getJSONObject("config").get("merge")));
                luckysheet.setRowlen(JSON.toJSONString(sheetData.getJSONObject("config").get("rowlen")));
                luckysheet.setColumnlen(JSON.toJSONString(sheetData.getJSONObject("config").get("columnlen")));
                luckysheet.setRowhidden(JSON.toJSONString(sheetData.getJSONObject("config").get("rowhidden")));
                luckysheet.setColhidden(JSON.toJSONString(sheetData.getJSONObject("config").get("colhidden")));
                luckysheet.setBorderInfo(JSON.toJSONString(sheetData.getJSONObject("config").get("borderInfo")));
                luckysheet.setCalcchain(JSON.toJSONString(sheetData.get("calcChain")));

                luckysheet.setDataverification(JSON.toJSONString(sheetData.get("dataVerification")));
                luckysheet.setFrozen(JSON.toJSONString(sheetData.get("frozen")));
                luckysheet.setFilterSelect(JSON.toJSONString(sheetData.get("filter_select")));
                luckysheet.setLuckysheetConditionformatSave(JSON.toJSONString(sheetData.get("luckysheet_conditionformat_save")));

                log.info("luckysheet JSON: {}",JSON.toJSONString(luckysheet));
                JSONArray cellDatas = sheetData.getJSONArray("celldata");
                if(!ListUtil.isEmpty(cellDatas))
                {
                    List<Map<String, JSONObject>> datas = new ArrayList<Map<String,JSONObject>>();
                    Map<String, JSONObject> map = null;
                    JSONObject cellData = null;
                    List<LuckysheetCell> luckysheetCells = new ArrayList<LuckysheetCell>();
                    LuckysheetCell newCellData = null;
                    for (int j = 0; j < cellDatas.size(); j++) {
                        if(j == 0 || j% Constants.MQ_LIST_LIMIT_SIZE == 0)
                        {
                            Map<String, JSONObject> tempMap = new HashMap<String, JSONObject>();
                            map = tempMap;
                            datas.add(map);
                        }
                        cellData = cellDatas.getJSONObject(j);
                        int r = cellData.getIntValue("r");
                        int c = cellData.getIntValue("c");
                        long id = cellData.getLongValue("id");
                        cellData.remove("r");
                        cellData.remove("c");
                        newCellData = new LuckysheetCell();
                        newCellData.setId(id);
                        newCellData.setRowNo(r);
                        newCellData.setColumnNo(c);
                        newCellData.setSheetIndex(luckysheet.getSheetIndex());
                        newCellData.setCellData(JSON.toJSONString(cellData));
                        luckysheetCells.add(newCellData);
                        cellData.put("r", r);
                        cellData.put("c", c);
                    }

                }
                luckysheetHis.setSheetIndex(sheetData.getString("index"));
                luckysheetHis.setChangeDesc("导入操作：从excel导入数据，sheet名称："+sheetData.getString("name")+"，sheet标识："+luckysheetHis.getSheetIndex());
                luckysheetHis.setRemark("导入操作：从excel导入数据，sheet名称："+sheetData.getString("name")+"，sheet标识："+luckysheetHis.getSheetIndex());
                luckysheetHis.setType(1);
                luckysheetHis.setOperateKey("shi");
                luckysheetHis.setCreateTime(new Date());
                luckysheetHis.setUpdateTime(new Date());
                list.add(luckysheetHis);
            }
            for (int i = 0; i < result.size(); i++) {
                result.getJSONObject(i).remove("celldata");
            }
            list.forEach(System.out::println);
        }
        return result;
    }
}
