package org.vivi.framework.codegen.service;

import cn.hutool.db.Entity;
import org.vivi.framework.codegen.common.PageResult;
import org.vivi.framework.codegen.entity.GenConfig;
import org.vivi.framework.codegen.entity.TableRequest;

/**
 * <p>
 * 代码生成器
 * </p>
 */
public interface CodeGenService {
    /**
     * 生成代码
     *
     * @param genConfig 生成配置
     * @return 代码压缩文件
     */
    byte[] generatorCode(GenConfig genConfig);

    /**
     * 分页查询表信息
     *
     * @param request 请求参数
     * @return 表名分页信息
     */
    PageResult<Entity> listTables(TableRequest request);
}
