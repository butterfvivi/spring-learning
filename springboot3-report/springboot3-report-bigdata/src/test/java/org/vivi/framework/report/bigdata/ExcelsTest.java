package org.vivi.framework.report.bigdata;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.vivi.framework.report.bigdata.poi.kit.Excels;
import org.vivi.framework.report.bigdata.poi.model.Version;
import org.vivi.framework.report.bigdata.utils.JsonUtils;
import org.vivi.framework.report.bigdata.utils.LogUtils;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
public class ExcelsTest {

    private List<Object> datas = Lists.newArrayList();

    @Before
    public void init(){
        /*for ( int i = 0; i< 101; i++ ) {
            Map data = Maps.newHashMap();
            data.put("name","测试测试测试"+i);
            data.put("params","https://blog.csdn.net/gaolu/article/details/52708177导入模板格式固定，不得删除标题行，不得对标题行做任何编辑"+i);
            data.put("paramType",i % 2);
            data.put("url","https://blog.csdn.net/gaolu/article/details/52708177"+i);
            data.put("requestType","POST"+i);
            data.put("expectCode","0"+i);
            data.put("result","展示的结果"+i);
            data.put("price",(3.45689 * i));
            datas.add(data);
        }*/
        for ( int i = 0; i< 10000; i++ ) {
            Interface2 data = new Interface2();
            data.setName("测试测试测试"+i);
            data.setParams("https://blog.csdn.net/gaolu/article/details/52708177导入模板格式固定，不得删除标题行，不得对标题行做任何编辑"+i);
            data.setParamType(i % 2);
            data.setUrl("https://blog.csdn.net/gaolu/article/details/52708177"+i);
            data.setRequestType("POST"+i);
            data.setExpectCode("0"+i);
            data.setResult("展示的结果"+i);
            data.setPrice((3.45689 * i));
            data.setDate(String.valueOf(new Date()));
            datas.add(data);
        }
    }

    /**使用格式化类作为表头**/
    @Test
    public void exportUseClass2() throws Exception {
        // 说明:导出的时候,不管data是List<Map>还是List<Class>,都能够用Class的方式导出
        FileOutputStream out = new FileOutputStream(System.getProperty("user.home")+"/Desktop/test5.xls");
        Map<String, List<?>> handler = Maps.newHashMap();
        handler.put("请求url地址", Lists.newArrayList("百度地址","阿里地址","本地地址","吃瓜地址"));
        Excels.Export()
                .config(Version.XLS,200)
                .addSheet(datas,Interface2.class,handler,"东成西就","操作说明：\n" +
                        "1、导入模板格式固定，不得删除标题行，不得对标题行做任何编辑，不得插入、删除标题列，不得调整标题行和列的位置。\n" +
                        "2、导入模板内容仅包括：楼号、单元/楼层、房间号、房间面积、房东姓名、房东手机号、房东身份证号码，7项，不得任意添加其他导入项目，所有项均为必填项。\n" +
                        "3、【楼号】：支持中文、数字和英文，如果是社区，需要输入“11号楼”或“11栋”之类的内容，如果是写楼楼，可以输入“A座”之类的内容； 但无论输入什么内容，必须所有内容统一，否则为显示为2个楼号；\n" +
                        "4、【单元/楼层】【房间号】：仅支持数字和英文；   最终展示的结果为是“5#402”； 如果是社区，建议是“单元号#房间号”，如果是楼层，建议是“楼层#房间号”；\n" +
                        "4、【房间面职】：主要为了区分房间的户型，不需要精确输入房间面积；\n" +
                        "5、【房东姓名】：支持汉字、英文、数字；【房东手机号】：仅支持11位阿拉伯数字，数字前后、之间不得有空格或其他字符；   【房东身份证号】：身份证号码必须是正确的身份证号，错误的号码将无法上传；\n" +
                        "6、导入模板各项内容不符合编辑规则的，导入时，系统将自动跳过，并计为导入失败数据；")
                .addSheet(datas,Interface2.class,"西就东成")
                .toStream(out);
    }

    @Test
    /**使用格式化字符串作为表头**/
    public void exportUseString2() throws Exception {
        FileOutputStream out = new FileOutputStream(System.getProperty("user.home")+"/Desktop/test5.xlsx");
        String titles = "接口名称:name,请求url地址:url,请求参数json数据:params,参数请求类型:paramType,请求方式:requestType,期望的返回结果code:expectCode,完整的返回结果:result,价格:price,日期:date";
        Excels.Export()
                .config(Version.XLSX,50000)
                .addSheet(datas,titles,"东成西就","操作说明：\n" +
                        "1、导入模板格式固定，不得删除标题行，不得对标题行做任何编辑，不得插入、删除标题列，不得调整标题行和列的位置。\n" +
                        "2、导入模板内容仅包括：楼号、单元/楼层、房间号、房间面积、房东姓名、房东手机号、房东身份证号码，7项，不得任意添加其他导入项目，所有项均为必填项。\n" +
                        "3、【楼号】：支持中文、数字和英文，如果是社区，需要输入“11号楼”或“11栋”之类的内容，如果是写楼楼，可以输入“A座”之类的内容； 但无论输入什么内容，必须所有内容统一，否则为显示为2个楼号；\n" +
                        "4、【单元/楼层】【房间号】：仅支持数字和英文；   最终展示的结果为是“5#402”； 如果是社区，建议是“单元号#房间号”，如果是楼层，建议是“楼层#房间号”；\n" +
                        "4、【房间面职】：主要为了区分房间的户型，不需要精确输入房间面积；\n" +
                        "5、【房东姓名】：支持汉字、英文、数字；【房东手机号】：仅支持11位阿拉伯数字，数字前后、之间不得有空格或其他字符；   【房东身份证号】：身份证号码必须是正确的身份证号，错误的号码将无法上传；\n" +
                        "6、导入模板各项内容不符合编辑规则的，导入时，系统将自动跳过，并计为导入失败数据；")
                .addSheet(datas,titles,"西就东成")
                .toStream(out);
    }

    @Test
    /**使用格式化类作为表头**/
    public void exportUseTemplate2() throws Exception {
        // 说明:导出的时候,不管data是List<Map>还是List<Class>,都能够用Class的方式导出
        FileInputStream template = new FileInputStream(System.getProperty("user.home")+"/Desktop/test5.xls");
        FileOutputStream out = new FileOutputStream(System.getProperty("user.home")+"/Desktop/test6.xls");
        Excels.Export()
                .addSheet(datas,template)
                .addSheet(datas,template)
                .toStream(out);
    }

    @Test
    public void importByClass2() throws Exception {
        List result = Excels.Import()
                .config(new FileInputStream(System.getProperty("user.home")+"/Desktop/test1.xls"))
                .addSheet(Interface2.class,0)
                .addSheet(Interface2.class,1)
                .addSheet(Interface2.class)
                .onedata();
        LogUtils.info(log,JsonUtils.objToStr(result));
    }

    @Test
    public void importByString2() throws Exception {
        String titles = "接口名称:name,请求url地址:url,请求参数json数据:params,参数请求类型:paramType,请求方式:requestType,期望的返回结果code:expectCode,完整的返回结果:result,价格:price";
        List result = Excels.Import()
                .config(new FileInputStream(System.getProperty("user.home")+"/Desktop/testone.xlsx"))
                .addSheet(titles)
                .onedata();
        LogUtils.info(log,JsonUtils.objToStr(result));
    }

    @Test
    public void importNoneTitleColumns2() throws Exception {
        List result = Excels.Import()
                .config(new FileInputStream(System.getProperty("user.home")+"/Desktop/test1.xls"))
                .addSheet(new Integer[]{1,2,3})
                .onedata();
        LogUtils.info(log, JsonUtils.objToStr(result));
    }

    @Test
    public void importNoneTitleRowsColumns2() throws Exception {
        List result = Excels.Import()
                .config(new FileInputStream(System.getProperty("user.home")+"/Desktop/test1.xls"))
                .addSheet(new Integer[]{2,3,4},new Integer[]{1,2,3})
                .onedata();
        LogUtils.info(log,JsonUtils.objToStr(result));
    }

    @Test
    public void importNoneTitleRectangle2() throws Exception {
        List result = Excels.Import()
                .config(new FileInputStream(System.getProperty("user.home")+"/Desktop/test1.xls"))
                .addSheet(2,4,1,3)
                .onedata();
        LogUtils.info(log,JsonUtils.objToStr(result));
    }

}
