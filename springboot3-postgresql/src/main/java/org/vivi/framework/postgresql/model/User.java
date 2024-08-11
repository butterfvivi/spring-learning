package org.vivi.framework.postgresql.model;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("t_user")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @TableId
    private Long id;
    private String name;
    private String email;
    private int age;

}
