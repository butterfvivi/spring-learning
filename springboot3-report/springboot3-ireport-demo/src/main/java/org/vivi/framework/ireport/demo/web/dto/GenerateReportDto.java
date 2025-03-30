package org.vivi.framework.ireport.demo.web.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GenerateReportDto {

    private Long id;

    /**
     * 动态参数
     */
    private Map<String, Object> searchData;

    /**
     * 分页参数
     */
    private Map<String, Integer> pagination;

    /**
     *	 每页显示的条数
     */
    private Integer pageSize = 500;

    /**
     * 	当前页数
     */
    private int currentPage = 1;

    /**
     * 	偏移量
     */
    private int offSet = 1;

    /**
     * 是否指定页数 1是 2否
     */
    private Integer isCustomerPage = 2;

    /**
     *  是否分页查询
     */
    boolean isPatination = true;

    /**
     * 起始页
     */
    private Integer startPage;

    /**
     *  结束页
     */
    private Integer endPage;
}
