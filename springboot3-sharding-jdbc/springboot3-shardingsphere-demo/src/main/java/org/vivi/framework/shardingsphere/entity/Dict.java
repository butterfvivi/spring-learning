package org.vivi.framework.shardingsphere.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("t_dict")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dict {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String dictType;

}
