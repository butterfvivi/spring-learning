 /** 
 * 模块：报表系统-DocTpl
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package org.vivi.framework.report.service.model.doctpl;

 import com.baomidou.mybatisplus.annotation.*;
 import lombok.AllArgsConstructor;
 import lombok.Builder;
 import lombok.Data;
 import lombok.NoArgsConstructor;
 import org.vivi.framework.report.service.common.entity.PageEntity;

 import java.util.Date;


@Data
@TableName("doc_tpl")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocTpl extends PageEntity {

    /** id -  */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** merchant_no - 商户号 */
    @TableField("merchant_no")
    private String merchantNo;

    /** tpl_code - 模板标识 */
    @TableField("tpl_code")
    private String tplCode;

    /** tpl_name - 模板名称 */
    @TableField("tpl_name")
    private String tplName;

    /** param_merge - 参数是否合并 1是 2否 */
    @TableField("param_merge")
    private Integer paramMerge;

    /** creator - 创建人 */
    @TableField(value = "creator",fill = FieldFill.INSERT)
    private Long creator;

    /** create_time - 创建时间 */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    /** updater - 更新人 */
    @TableField(value = "updater",fill = FieldFill.INSERT_UPDATE)
    private Long updater;

    /** update_time - 更新时间 */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /** del_flag - 删除标记 1未删除 2已删除 */
    @TableField("del_flag")
    private Integer delFlag;

    /** report_type - 类型 */
    @TableField("report_type")
    private Long reportType;

    /** firstpage_header_footer_show - 首页页眉页脚是否显示 1是 2否 */
    @TableField("firstpage_header_footer_show")
    private Integer firstpageHeaderFooterShow;
}