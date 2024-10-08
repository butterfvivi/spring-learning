package org.vivi.framework.iasyncexcel.core.exporter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.handler.WriteHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.vivi.framework.iasyncexcel.core.model.DataParam;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class DataExportParam<T> extends DataParam {

    /**
     * 分页大小
     */
    private int limit=1000;
    /**
     * 导出文件名称
     */
    private String exportFileName;
    /**
     * 写入excel的sheetName
     */
    @Deprecated
    private String sheetName;
    /**
     * 是否动态表头，默认false。
     */
    @Deprecated
    private boolean dynamicHead;
    /**
     * 当dynamicHead=true时需要传一个动态表头进来
     */
    @Deprecated
    private List<List<String>> headList;
    /**
     * 表头对应的实体类
     */
    @Deprecated
    private Class<?> headClass;
    /**
     * 自定义写处理器为了，自定义样式，表格合并之类的easyExcel原生扩展
     */
    @Deprecated
    private List<WriteHandler> writeHandlers;
    /**
     * 自定义类型转换器easyExcel原生扩展
     */
    @Deprecated
    private List<Converter<?>> converters;

}
