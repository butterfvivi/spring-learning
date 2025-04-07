package org.vivi.framework.ureport.store.core.provider.report.mysql;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.vivi.framework.ureport.store.core.provider.report.ReportFile;
import org.vivi.framework.ureport.store.core.provider.report.ReportProvider;
import org.vivi.framework.ureport.store.domain.UreportFile;
import org.vivi.framework.ureport.store.domain.bo.UreportFileBo;
import org.vivi.framework.ureport.store.domain.vo.UreportFileVo;
import org.vivi.framework.ureport.store.service.IUreportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Component
public class MysqlReportProvider implements ReportProvider {


    @Autowired
    private IUreportService iUreportService;

    private String prefix = "mysql:";

    /**
     * 根据报表名加载报表文件
     *
     * @param file 报表名称
     * @return 返回的InputStream
     */
    @Override
    public InputStream loadReport(String file) {

        if (file.startsWith(prefix)) {
            file = file.substring(prefix.length(), file.length());
        }

        UreportFileBo bo=new UreportFileBo();

        bo.setName(file);

        UreportFileVo vo = iUreportService.getUreportFileByFile(bo);

        InputStream inputStream= new ByteArrayInputStream(vo.getContent());

        return inputStream;
    }

    /**
     * 根据报表名，删除指定的报表文件
     *
     * @param file 报表名称
     */
    @Override
    public void deleteReport(String file) {
        if (file.startsWith(prefix)) {
            file = file.substring(prefix.length(), file.length());
        }

        UreportFileBo bo=new UreportFileBo();

        bo.setName(file);

        iUreportService.deleteUreportFile(bo);
    }

    /**
     * 获取所有的报表文件
     *
     * @return 返回报表文件列表
     */
    @Override
    public List<ReportFile> getReportFiles() {

        List<UreportFileVo> ureportFileVos = iUreportService.selectUreportFile();


        JSONArray array =new JSONArray();

        array.addAll(ureportFileVos);

        List<ReportFile> list = JSONUtil.toList(array, ReportFile.class);


        return list;
    }

    /**
     * 保存报表文件
     *
     * @param file    报表名称
     * @param content 报表的XML内容
     */
    @Override
    public void saveReport(String file, String content,String name) {

        if (file.startsWith(prefix)) {
            file = file.substring(prefix.length(), file.length());
        }


        UreportFileBo bo=new UreportFileBo();
        bo.setName(file);
        bo.setFileName(name);
        bo.setContent(content.getBytes());


        boolean exists = iUreportService.isExists(bo);

        if(exists){
            bo.setUpdateTime(DateUtil.date());
            iUreportService.updateUreportFile(bo);
        }else{
            bo.setCreateTime(DateUtil.date());
            iUreportService.saveUreportFile(bo);
        }

//        System.out.println(file);
//        System.out.println(content);
//        System.out.println(name);



    }

    /**
     * @return 返回存储器名称
     */
    @Override
    public String getName() {
        return "MYSQL存储";
    }

    /**
     * @return 返回是否禁用
     */
    @Override
    public boolean disabled() {
        return true;
    }

    /**
     * @return 返回报表文件名前缀
     */
    @Override
    public String getPrefix() {
        return this.prefix;
    }
}
