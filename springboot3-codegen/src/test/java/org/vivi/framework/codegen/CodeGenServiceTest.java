package org.vivi.framework.codegen;

import cn.hutool.core.io.IoUtil;
import cn.hutool.db.Entity;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.vivi.framework.codegen.common.PageResult;
import org.vivi.framework.codegen.entity.GenConfig;
import org.vivi.framework.codegen.entity.TableRequest;
import org.vivi.framework.codegen.service.CodeGenService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * <p>
 * 代码生成service测试
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019-03-22 10:34
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CodeGenServiceTest {
    @Autowired
    private CodeGenService codeGenService;

    @Test
    public void testTablePage() {
        TableRequest request = new TableRequest();
        request.setCurrentPage(1);
        request.setPageSize(10);
        request.setPrepend("jdbc:mysql://");
        request.setUrl("127.0.0.1:3306/springboot3_demo");
        request.setUsername("root");
        request.setPassword("wtf0010.");
        request.setTableName("t_");
        PageResult<Entity> pageResult = codeGenService.listTables(request);
        log.info("【pageResult】= {}", pageResult);
    }

    @Test
    @SneakyThrows
    public void testGeneratorCode() {
        GenConfig config = new GenConfig();

        TableRequest request = new TableRequest();
        request.setPrepend("jdbc:mysql://");
        request.setUrl("127.0.0.1:3306/springboot3_demo");
        request.setUsername("root");
        request.setPassword("wtf0010.");
        request.setTableName("t_user");
        config.setRequest(request);

        config.setModuleName("shiro");
        config.setAuthor("Yangkai.Shen");
        config.setComments("用户角色信息");
        config.setPackageName("com.xkcoding");
        config.setTablePrefix("shiro_");

        byte[] zip = codeGenService.generatorCode(config);
        OutputStream outputStream = new FileOutputStream(new File("/Users/vivi/Desktop/" + request.getTableName() + ".zip"));
        IoUtil.write(outputStream, true, zip);
    }

}
