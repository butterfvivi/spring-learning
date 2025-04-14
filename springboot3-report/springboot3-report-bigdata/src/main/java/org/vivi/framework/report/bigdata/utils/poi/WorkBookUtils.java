package org.vivi.framework.report.bigdata.utils.poi;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.vivi.framework.report.bigdata.common.exception.BaseRuntimeException;
import org.vivi.framework.report.bigdata.common.exception.ErrorCode;
import org.vivi.framework.report.bigdata.entity.model.Version;
import org.vivi.framework.report.bigdata.utils.IOUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkBookUtils {

    private static final String EXCEL_XLS_CLASS_NAME = "HSSFWorkbook";
    private static final String EXCEL_XLSX_CLASS_NAME = "XSSFWorkbook";

    /**用流初始化workbook**/
    public static Workbook create(InputStream in) {
        if( null == in ){
            throw new BaseRuntimeException(ErrorCode.EXPORT_ERROR.getCode(),"Excel文件错误,请确认是否传入指定格式的文件！");
        }
        if (!in.markSupported()) {
            in = new BufferedInputStream(in);
        }
        try {
            //操作Excel2003以前（包括2003）的版本，扩展名是.xls
            Workbook workbook = null;
            if (FileMagic.OLE2.equals(FileMagic.valueOf(in))) {
                workbook = new HSSFWorkbook(in);
            }else if (FileMagic.OOXML.equals(FileMagic.valueOf(in))) {
                //操作Excel2007的版本，扩展名是.xlsx
                workbook = new XSSFWorkbook(OPCPackage.open(in));
            }
            return workbook;
        } catch (Exception e) {
            throw new BaseRuntimeException(ErrorCode.EXPORT_ERROR.getCode(),"Excel文件错误,请确认是否传入指定格式的文件！");
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**用流初始化workbook**/
    public static Workbook create(Version version) {
        Workbook workbook = null;
        if(Version.XLS.equals(version)){
            workbook = new HSSFWorkbook();
        }else
        if(Version.XLSX.equals(version)){
            workbook = new XSSFWorkbook();
        }
        return workbook;
    }

    /**根据workbook获取版本**/
    public static Version version(Workbook workbook){
        if( null == workbook ){
            throw new BaseRuntimeException(ErrorCode.EXPORT_ERROR.getCode(),"Excel对象为空,无法获取版版本号");
        }
        String className = workbook.getClass().getSimpleName();
        if(EXCEL_XLS_CLASS_NAME.equals(className)){
            return Version.XLS;
        }else if(EXCEL_XLSX_CLASS_NAME.equals(className)){
            return Version.XLSX;
        }
        return null;
    }

    /**导出到指定的流文件中:需要先执行stream()方法,初始化outputStream**/
    public static void write(Workbook workbook,OutputStream out){
        try {
            workbook.write(out);
        } catch (Exception e) {
            log.error("{}",e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
}
