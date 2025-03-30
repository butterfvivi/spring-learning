package org.vivi.framework.report.service.web.dto.reporttpl;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.vivi.framework.report.service.common.enums.YesNoEnum;

import java.util.List;
import java.util.Map;

/**  
 * @ClassName: GenerateReportDto
 * @Description: 生成报表用参数实体类
*/
@Data
public class MesGenerateReportDto {

	/**  
	 * @Fields tplId : 模板id
	 */
	private Long tplId;

	/**
	 *  动态参数
	 */
	private List<Map<String, Object>> searchData;
	
	/**  
	 * @Fields pagination : 分页参数
	 */
	private Map<String, Integer> pagination;
	
	/**
	 *	 每页显示的条数
	 */
	private Integer pageSize = 500;

	/**
	 * 	当前页数
	 */
	private int currentPage = 1;
	
	/**
	 * 	偏移量
	 */
	private int offSet = 1;
	
	/**  
	 * @Fields dataType : 请求来源 1预览数据 2导出全部数据 3导出当前页数据，默认为1
	 */
	private Integer source = 1;
	
	/**  
	 * @Fields password : 密码
	 */  
	private String password;
	

	public int getOffSet() {
	    return (currentPage-1)*pageSize;
	}
	
	/**  
	 * @Fields isValidateRole : 是否需要校验角色权限，1是 2否 默认2
	 */
	private Integer isValidateRole = YesNoEnum.NO.getCode();
	
	/**  
	 * @Fields roleId : 角色id，校验时需要传入角色id
	 */
	private Long roleId;
	
	/**  
	 * @Fields isPreview : 是否是预览
	 */
	private Integer isPreview = YesNoEnum.YES.getCode();
	
	/**  
	 * @Fields isPatination : 是否分页查询
	 */
	private boolean isPatination = false;
	
	/**  
	 * @Fields tplName : 文档名称
	 */
	private String tplName;
	
	/**  
	 * @Fields sheetIndex : sheet index
	 */
	private String sheetIndex;
	
	/**  
	 * @Fields pdfType : 横向还是纵向pdf 1纵向 2横向 默认1
	 */
	private Integer pdfType = 1;
	
	/**  
	 * @Fields isCustomerPage : 是否指定页数 1是 2否
	 */
	private Integer isCustomerPage = 2;
	
	/**  
	 * @Fields startPage : 起始页
	 */
	private Integer startPage;
	
	/**  
	 * @Fields endPage : 结束页
	 */
	private Integer endPage;
	
	/**  
	 * @Fields isMobile : 是否是手机端
	 */
	private Integer isMobile = YesNoEnum.NO.getCode();
	
	/**  
	 * @Fields chartsBase64 : chart的base64数据
	 */
	private JSONObject chartsBase64;
	
	/**  
	 * @Fields taskId : 任务id
	 */
	private Long taskId;
	
	/**  
	 * @Fields exportType : 导出类型 1excel  2pdf 3excel和pdf
	 */
	private int exportType = 1;
	
	/**  
	 * @Fields sendEmail : 发送邮件
	 */
	private String sendEmail;
	
	/**  
	 * @Fields jobName : 任务名称
	 */
	private String jobName;
	
	/**  
	 * @Fields fileId : 页面加载时分配一个文件名，防止每次查询都生成一个新的文件(doc用)
	 */
	private String fileId;
	
	/**  
	 * @Fields apiHeaders : api请求所需要的url中的动态header
	 */
	private JSONObject apiHeaders;
}
