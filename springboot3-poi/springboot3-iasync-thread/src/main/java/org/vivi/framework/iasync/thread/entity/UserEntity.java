package org.vivi.framework.iasync.thread.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * USER实体类
 */
@TableName("USER")
@Data
@Accessors(chain = true)
public class UserEntity {

    /**
     * ID
     **/
    @TableId
    private long id;

    /**
     * 用户名
     **/
    private String name;
}
