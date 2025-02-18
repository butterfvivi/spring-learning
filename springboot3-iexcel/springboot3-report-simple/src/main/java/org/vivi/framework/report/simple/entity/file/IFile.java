package org.vivi.framework.report.simple.entity.file;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(keepGlobalPrefix=true, value = "gaea_file")
public class IFile {

    /** 文件标识 */
    private String fileId;

    /** 文件类型 */
    private String fileType;

    /** 文件路径 */
    private String filePath;

    /** url路径 */
    private String urlPath;

    /** 内容说明 */
    private String fileInstruction;
}
