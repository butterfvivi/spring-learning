package org.vivi.framework.securityjwt.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_role")
public class UserRole implements Serializable {

    private Integer id;

    private String name;

    private String remark;
}
