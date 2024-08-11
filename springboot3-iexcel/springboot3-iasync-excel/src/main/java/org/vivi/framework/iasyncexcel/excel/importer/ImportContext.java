package org.vivi.framework.iasyncexcel.excel.importer;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vivi.framework.iasyncexcel.model.ExcelContext;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Future;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportContext extends ExcelContext {

    private String fileName;

    private String sheetName;

    private Class<?> errorHeadClass;

    private ExcelWriter excelWriter;

    private WriteSheet writeSheet;

    private ReadSheet readSheet;

    private OutputStream outputStream;

    private InputStream inputStream;

    private Future<String> future;

    private String errorFile;

    private String failMessage;

    private boolean validMaxRows;

    private int maxRows=1000;

    private boolean validHead=true;
}
