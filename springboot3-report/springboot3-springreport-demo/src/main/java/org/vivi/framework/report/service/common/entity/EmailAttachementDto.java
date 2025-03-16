package org.vivi.framework.report.service.common.entity;

import lombok.Data;

import java.io.InputStream;

/**  
 * @ClassName: EmailAttachementDto
 * @Description: 邮件附件实体类
*/
@Data
public class EmailAttachementDto {

	private String fileName;
	
	/**  
	 * @Fields is : 文件流
	 * @author caiyang
	 * @date 2023-07-29 10:48:02 
	 */  
	private InputStream is;
}
