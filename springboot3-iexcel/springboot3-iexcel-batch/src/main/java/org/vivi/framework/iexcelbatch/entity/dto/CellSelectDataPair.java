package org.vivi.framework.iexcelbatch.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CellSelectDataPair {
    /**
     * 坐标
     */
    private CellSelectDto cellSelectDataVo;
    /**
     * 下拉数据
     */
    private List<String> selectDataList;

}

