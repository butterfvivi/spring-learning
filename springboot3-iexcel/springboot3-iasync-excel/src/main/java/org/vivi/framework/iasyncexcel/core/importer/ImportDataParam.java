package org.vivi.framework.iasyncexcel.core.importer;

import lombok.*;
import lombok.experimental.Accessors;
import org.vivi.framework.iasyncexcel.core.model.DataParam;

import java.io.InputStream;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class ImportDataParam extends DataParam {

    /**
     * 输入流
     */
    private InputStream stream;

    /**
     * 源文件url,用于保存源文件地址
     */
    private String sourceFile;
    /**
     * 文件名称
     */
    private String filename;
    /**
     * 导入对应的实体类
     */
    private Class<?> model;
    /**
     * 分批次大小，如果你导入1w条数据，每次1000会分10次读到内存中
     */
    private int batchSize = 1000;

    /**
     * 是否限制导入行数，默认false，如果限制行数将会出发行数限制异常，例如限制1000行，你的文件如果超过1000行将会抛异常
     */
    private boolean validMaxRows = false;

    /**
     * 行数限制validMaxRows=true时起作用
     */
    private int maxRows = 1000;

    /**
     * 是否进行表头校验，顺序单元格内容都应该与实体类保持一致。
     */
    private boolean validHead = true;
}
