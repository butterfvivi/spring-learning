
package org.vivi.framework.report.simple.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.vivi.framework.report.simple.entity.dataset.DataSet;
import org.vivi.framework.report.simple.entity.dataset.dto.DataSetDto;
import org.vivi.framework.report.simple.entity.dataset.dto.OriginalDataDto;

import java.util.List;

/**
* @desc DataSet 数据集服务接口
**/

public interface DataSetService {

    /**
     * 单条详情
     * @param id
     * @return
     */
    DataSetDto detailSet(Long id);

    /**
     * 单条详情
     * @param setCode
     * @return
     */
    DataSetDto detailSet(String setCode);

    /**
     * 新增数据集、添加查询参数、数据转换
     * @param dto
     */
    DataSetDto insertSet(DataSetDto dto);

    /**
     * 更新数据集、添加查询参数、数据转换
     * @param dto
     */
    void updateSet(DataSetDto dto);

    /**
     * 删除数据集、添加查询参数、数据转换
     * @param id
     */
    void deleteSet(Long id);

    /**
     * 获取数据
     * @param dto
     * @return
     */
    OriginalDataDto getData(DataSetDto dto);

    /**
     *
     * @param dto
     * @return
     */
    OriginalDataDto testTransform(DataSetDto dto);

    /**
     * 获取所有数据集
     * @return
     */
    List<DataSet> queryAllDataSet();

    void copy(DataSetDto dto);
}
