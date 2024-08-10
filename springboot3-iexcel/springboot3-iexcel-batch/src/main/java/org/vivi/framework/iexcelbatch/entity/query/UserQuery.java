package org.vivi.framework.iexcelbatch.entity.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.vivi.framework.iexcelbatch.entity.model.User;

import java.time.LocalDateTime;

@Data
@Schema(name = "条件搜索")
public class UserQuery extends Page<User> {

    /**
     *
     */
    @Schema(name = "主键")
    private Long id;

    /**
     * 姓名
     */
    @Schema(name = "姓名")
    private String name;

    /**
     * 性别: 0未知 1男 2 女
     */
    @Schema(name = "性别: 0未知 1男 2 女")
    private Integer sex;

    /**
     * 年龄
     */
    @Schema(name = "年龄")
    private Integer age;

    /**
     * 生日
     */
    @Schema(name = "生日")
    private LocalDateTime birthday;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    private LocalDateTime createTime;

}
