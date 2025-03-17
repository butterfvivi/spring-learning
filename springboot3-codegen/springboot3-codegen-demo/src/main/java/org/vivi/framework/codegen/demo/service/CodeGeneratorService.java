package cn.molu.generator.service;

import cn.molu.generator.mapper.CodeGeneratorMapper;
import cn.molu.generator.pojo.ColumnDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author 陌路
 * @apiNote 代码生成器业务层
 * @date 2024/1/17 13:22
 * @tool Created by IntelliJ IDEA
 */
@Slf4j
@RequiredArgsConstructor
@Service("codeGeneratorService")
public class CodeGeneratorService {

    @Value("${cn.molu.generate.database}")
    private String database; // 这里是数据库的名字，我的数据库是 temp1
    private final CodeGeneratorMapper codeGeneratorMapper;

    /**
     * 获取数据库中所有表信息
     */
    public List<ColumnDetail> getAllTableInfo() {
        return codeGeneratorMapper.getColumnDetailMapVo(database);
    }

    /**
     * 获取表中所有字段信息
     *
     * @param tableName 表名
     */
    public List<ColumnDetail> getColumnDetails(String tableName) {
        return codeGeneratorMapper.getColumnDetailMapVoByTableName(database, tableName);
    }

    /**
     * 生成代码
     *
     * @param columnDetailList 表字段信息
     * @param delPrefix        需要去除的前缀（tb_）
     * @param packageName      文件所在包（cn.molu.generator）
     * @param type             生成类型（java/entity.java、vue3/vue2Page.vue3...）
     */
    public String generateCode(List<ColumnDetail> columnDetailList, String delPrefix, String packageName, String type) {
        try (StringWriter writer = new StringWriter()) {
            Properties properties = new Properties();
            properties.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
            Velocity.init(properties);
            Map<String, Object> map = new ColumnDetail().listToMap(columnDetailList, delPrefix);
            map.put("packageName", packageName);
            VelocityContext context = new VelocityContext();
            context.put("map", map);
            Velocity.getTemplate("vm/" + type + ".vm", "UTF-8").merge(context, writer);
            return writer.toString();
        } catch (Exception e) {
            log.error("报错了：" + e.getMessage(), e);
            return "报错了：" + e.getMessage();
        }
    }
}
