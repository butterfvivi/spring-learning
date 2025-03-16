package org.vivi.framework.report.service.web.dto.reporttpl;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.vivi.framework.report.service.common.enums.YesNoEnum;

import java.util.List;
import java.util.Map;

/**  
 * @ClassName: ResPreviewData
 * @Description: 预览返回结果用实体类
*/
@Data
public class ResPreviewData {

	/**  
	 * @Fields isParamMerge : 是否是合并参数
	 */
	private Integer isParamMerge = YesNoEnum.NO.getCode();
	
	/**  
	 * @Fields sheetDatas : 返回数据
	 */
	private List<ResLuckySheetDataDto> sheetDatas;
	
	/**  
	 * @Fields cellDictsLabelValue : 单元格坐标对应的字典 外层map的key是sheetindex_r_c，内层map的key是label，根据label获取value，返回给前端上报数据时用
	 */
	private Map<String, Map<String, String>> cellDictsLabelValue;
	
	/**  
	 * @Fields imgCells : 有图片的单元格
	 */
	private Map<String, JSONObject> imgCells;
	
	/**  
	 * @Fields pagination : 分页信息
	 */
	private Map<String, Object> pagination;
	
	/**  
	 * @Fields version : 当前更新版本号，防止重复提交覆盖数据
	 */
	private Long version;
	
	/**  
	 * @Fields tplName : 报表名称
	 */
	private String tplName;
	
	/**  
	 * @Fields showToolbar : 预览是否展示工具栏
	 */
	private Integer showToolbar;
	
	/**  
	 * @Fields showRowHeader : 预览是否显示行标题1是 2否
	 */
	private Integer showRowHeader;
	
	/**  
	 * @Fields showColHeader : 预览是否显示列标题 1是 2否
	 */
	private Integer showColHeader;
	
	/**  
	 * @Fields showGridlines : 预览是否显示网格线 1是 2否
	 */
	private Integer showGridlines;
	
	/**  
	 * @Fields tplType : 报表类型 1查询报表 2填报报表
	 */
	private Integer tplType;
	
	/**  
     * @Fields refreshPage : 填报报表提交后是否刷新页面 1是 2否
     */
    private Integer refreshPage = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields coeditFlag : 是否开启协同 1是 2否
     */
    private Integer coeditFlag = YesNoEnum.YES.getCode();
    
    /**  
     * @Fields sqlMaps : 本次查询每个数据集对应的sql语句
     */
    private List<Map<String, String>> reportSqls;
    
    /**  
     * @Fields showReportSql : 是否显示sql语句
     */
    private boolean showReportSql = false;
}
