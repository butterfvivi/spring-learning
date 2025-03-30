package org.vivi.framework.ireport.demo.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageEntity {

    /**
     *	 每页显示的条数
     */
    @TableField(exist = false)
    private Integer pageSize = 10;

    /**
     * 	当前页数
     */
    @TableField(exist = false)
    private int currentPage = 1;

    /**
     * 	偏移量
     */
    @TableField(exist = false)
    private int offSet = 1;

    /**
     * 	表格数据行
     */
    @TableField(exist = false)
    private List<?> data;

    /**
     * 总条数
     */
    @TableField(exist = false)
    private Long total = (long) 0;
}
