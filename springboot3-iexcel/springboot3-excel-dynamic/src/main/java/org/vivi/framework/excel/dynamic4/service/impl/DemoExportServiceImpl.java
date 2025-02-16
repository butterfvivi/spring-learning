//package org.vivi.framework.excel.dynamic4.service.impl;
//
//
//import cn.hutool.core.util.StrUtil;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.springframework.stereotype.Service;
//import org.vivi.framework.excel.dynamic4.model.DemoDataVO;
//import org.vivi.framework.excel.dynamic4.model.ExportParam;
//import org.vivi.framework.excel.dynamic4.service.IEasyExcelService;
//import org.vivi.framework.excel.exception.CommonException;
//
//import java.util.*;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//@Service
//@Slf4j
//public class DemoExportServiceImpl {
//
//    private static final String FILE_NAME_PRODUCT_DETAIL = "测试动态表头";
//    private final IDemoService service;
//
//    private final IEasyExcelService excelService;
//    public DemoExportServiceImpl(IDemoService service, IEasyExcelService excelService) {
//        this.service = service;
//        this.excelService = excelService;
//    }
//
//    public void export(HttpServletResponse response, ExportParam param) throws CommonException {
//        // 根据param获取查询到的参数
//        List<DemoDataVO> pageInfoList =  service.listAllData(param);
//        // export with dynamics head
//        excelService.exportExcelWithDynamicsHead(response, head(param), getData(pageInfoList, param),
//                FILE_NAME_PRODUCT_DETAIL, FILE_NAME_PRODUCT_DETAIL, new FreezeRowColHandler(FreezeRowColConstant.PRODUCT_DETAIL));
//    }
//
//    public List<List<Object>> getData(List<DemoDataVO> detailList, ExportParam param){
//        if (CollectionUtils.isEmpty(detailList)){
//            return new ArrayList<>();
//        }
//        // 查询的动态参数
//        List<String> choiceParam = param.getChoiceParam();
//
//        return detailList.stream().map(c -> {
//            List<Object> objectList = new LinkedList<>();
//            objectList.add(c.getParamData1());
//            // 设置动态表头的数据
//            this.setChoiceParamValue(objectList, c, choiceParam);
//            objectList.add(c.getParamData2());
//            return objectList;
//        }).collect(Collectors.toList());
//    }
//
//    private void setChoiceParamValue(List<Object> objectList, DemoDataVO vo, List<String> choiceParams){
//
//        List<String> paramData = vo.getChoiceParamData();
//        if (CollectionUtils.isEmpty(paramData) || CollectionUtils.isEmpty(choiceParams)){
//            return;
//        }
//        // 转成map
//        Map<String, String> paramDataMap = paramData.stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
//
//        for (String choiceData : choiceParams) {
//            // 获取对应的值
//            String matchingValue = paramDataMap.get(choiceData);
//            // 添加 对应的值 到 objectList
//            if (StrUtil.isNotBlank(matchingValue)) {
//                objectList.add(matchingValue);
//            } else {
//                objectList.add("");
//            }
//        }
//    }
//
//    private List<List<String>> head(ExportParam param) {
//        List<List<String>> headList = new ArrayList<>();
//        addHeadItem(headList, "参数1");
//        // 动态表头 此处是根据参数中的动态参数设置，选择多少参数就动态加多少表头 其它同理
//        // 这里也可以自定义：比如 根据查询的数据量设置等
//        List<String> choiceParam = param.getChoiceParam();
//        if (!CollectionUtils.isEmpty(choiceParam)){
//            for (String choice : choiceParam) {
//                addHeadItem(headList, "动态头" + "-" + choice);
//            }
//        }
//        addHeadItem(headList, "参数2");
//        // .......其他表头
//        return headList;
//    }
//
//    private static void addHeadItem(List<List<String>> headList, String value) {
//        List<String> item = new ArrayList<>();
//        item.add(value);
//        headList.add(item);
//    }
//
//}