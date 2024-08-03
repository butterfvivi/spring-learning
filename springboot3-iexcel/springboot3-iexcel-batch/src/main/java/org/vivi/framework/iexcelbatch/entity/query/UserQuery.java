package org.vivi.framework.iexcelbatch.entity.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.vivi.framework.iexcelbatch.entity.model.User;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "条件搜索")
public class UserQuery extends Page<User> {

    /**
     *
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 性别: 0未知 1男 2 女
     */
    @ApiModelProperty(value = "性别: 0未知 1男 2 女")
    private Integer sex;

    /**
     * 年龄
     */
    @ApiModelProperty(value = "年龄")
    private Integer age;

    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    private LocalDateTime birthday;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
