 /** 
 * 模块：报表系统-ReportTplDatasource
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package org.vivi.framework.report.service.model.reporttpldatasource;

 import com.baomidou.mybatisplus.annotation.*;
 import lombok.AllArgsConstructor;
 import lombok.Builder;
 import lombok.Data;
 import lombok.NoArgsConstructor;
 import org.vivi.framework.report.service.common.entity.PageEntity;

 import java.util.Date;

@Data
@TableName("report_tpl_datasource")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportTplDatasource extends PageEntity {

    /** id - 主键 */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** merchant_no - 商户号 */
    @TableField("merchant_no")
    private String merchantNo;

    /** tpl_id - 模板id */
    @TableField("tpl_id")
    private Long tplId;

    /** datasource_id - 数据源id */
    @TableField("datasource_id")
    private Long datasourceId;

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
}