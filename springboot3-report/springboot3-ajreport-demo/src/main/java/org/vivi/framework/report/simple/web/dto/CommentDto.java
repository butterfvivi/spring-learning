package org.vivi.framework.report.simple.web.dto;


import lombok.Data;

@Data
public class CommentDto {
    /**
     * 批注框左边距
     */
    private int left;

    /**
     * 批注框上边距
     */
    private int top;

    /**
     * 批注框宽度
     */
    private int width;

    /**
     * 批注框高度
     */
    private int height;

    /**
     * 批注内容
     */
    private String value;

    /**
     * 批注框是否显示
     */
    private boolean isshow;
}

