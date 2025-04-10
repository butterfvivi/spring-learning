package org.vivi.framework.report.simple.modules.dataset.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.vivi.framework.report.simple.common.constant.JdbcConstants;
import org.vivi.framework.report.simple.common.enums.Enabled;
import org.vivi.framework.report.simple.common.enums.SetTypeEnum;
import org.vivi.framework.report.simple.common.exception.BusinessExceptionBuilder;
import org.vivi.framework.report.simple.modules.dataset.entity.DataSet;
import org.vivi.framework.report.simple.modules.dataset.service.DataSetService;
import org.vivi.framework.report.simple.web.dto.DataSetDto;
import org.vivi.framework.report.simple.web.dto.OriginalDataDto;
import org.vivi.framework.report.simple.modules.datasetparam.entity.DataSetParam;
import org.vivi.framework.report.simple.modules.datasetparam.entity.dto.DataSetParamDto;
import org.vivi.framework.report.simple.modules.datasettransform.entity.DataSetTransform;
import org.vivi.framework.report.simple.modules.datasettransform.entity.dto.DataSetTransformDto;
import org.vivi.framework.report.simple.modules.datasource.entity.DataSource;
import org.vivi.framework.report.simple.modules.datasource.entity.dto.DataSourceDto;
import org.vivi.framework.report.simple.modules.dataset.dao.DataSetMapper;
import org.vivi.framework.report.simple.modules.datasetparam.dao.DataSetParamMapper;
import org.vivi.framework.report.simple.modules.datasettransform.dao.DataSetTransformMapper;
import org.vivi.framework.report.simple.modules.datasource.dao.DataSourceMapper;
import org.vivi.framework.report.simple.modules.datasetparam.service.DataSetParamService;
import org.vivi.framework.report.simple.modules.datasettransform.service.DataSetTransformService;
import org.vivi.framework.report.simple.modules.datasource.service.DataSourceService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据集服务实现
 */
@Service
@Slf4j
public class DataSetServiceImpl implements DataSetService {

    @Autowired
    private DataSetMapper dataSetMapper;

    @Autowired
    private DataSetParamService dataSetParamService;

    @Autowired
    private DataSetParamMapper dataSetParamMapper;

    @Autowired
    private DataSetTransformService dataSetTransformService;

    @Autowired
    private DataSetTransformMapper dataSetTransformMapper;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private DataSourceMapper dataSourceMapper;

    /**
     * 单条详情
     *
     * @param id
     * @return
     */
    @Override
    public DataSetDto detailSet(Long id) {
        DataSetDto dto = new DataSetDto();
        DataSet result = dataSetMapper.selectById(id);
        String setCode = result.getSetCode();
        BeanUtils.copyProperties(result, dto);
        return getDetailSet(dto, setCode);
    }

    /**
     * 单条详情
     *
     * @param setCode
     * @return
     */
    @Override
    public DataSetDto detailSet(String setCode) {
        DataSetDto dto = new DataSetDto();
        DataSet result = dataSetMapper.selectOne(
                new QueryWrapper<DataSet>().lambda().eq(DataSet::getSetCode
                        , setCode));
        BeanUtils.copyProperties(result, dto);
        return getDetailSet(dto, setCode);
    }

    public DataSetDto getDetailSet(DataSetDto dto, String setCode) {
        //查询参数
        List<DataSetParam> dataSetParamList = dataSetParamMapper.selectList(
                new QueryWrapper<DataSetParam>()
                        .lambda()
                        .eq(DataSetParam::getSetCode, setCode)
        );
        List<DataSetParamDto> dataSetParamDtoList = new ArrayList<>();
        dataSetParamList.forEach(dataSetParam -> {
            DataSetParamDto dataSetParamDto = new DataSetParamDto();
            BeanUtils.copyProperties(dataSetParam, dataSetParamDto);
            BeanUtils.copyProperties(dataSetParam, dataSetParamDto);
            dataSetParamDtoList.add(dataSetParamDto);
        });
        dto.setDataSetParamDtoList(dataSetParamDtoList);

        //数据转换

        List<DataSetTransform> dataSetTransformList = dataSetTransformMapper.selectList(
                new QueryWrapper<DataSetTransform>()
                        .lambda()
                        .eq(DataSetTransform::getSetCode, setCode)
                        .orderByAsc(DataSetTransform::getOrderNum)
        );
        List<DataSetTransformDto> dataSetTransformDtoList = new ArrayList<>();
        dataSetTransformList.forEach(dataSetTransform -> {
            DataSetTransformDto dataSetTransformDto = new DataSetTransformDto();
            BeanUtils.copyProperties(dataSetTransform, dataSetTransformDto);
            dataSetTransformDtoList.add(dataSetTransformDto);
        });
        dto.setDataSetTransformDtoList(dataSetTransformDtoList);

        if (StringUtils.isNotBlank(dto.getCaseResult())) {
            try {
                JSONArray jsonArray = JSONArray.parseArray(dto.getCaseResult());
                if (!jsonArray.isEmpty()) {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    dto.setSetParamList(jsonObject.keySet());
                }
            } catch (Exception e) {
                log.error("error", e);
            }
        }
        return dto;
    }


    /**
     * 新增数据集、添加查询参数、数据转换
     *
     * @param dto
     */
    @Override
    @Transactional
    public DataSetDto insertSet(DataSetDto dto) {
        List<DataSetParamDto> dataSetParamDtoList = dto.getDataSetParamDtoList();
        List<DataSetTransformDto> dataSetTransformDtoList = dto.getDataSetTransformDtoList();

        //1.新增数据集
        DataSet dataSet = new DataSet();
        BeanUtils.copyProperties(dto, dataSet);
        if (StringUtils.isNotBlank(dataSet.getCaseResult())) {
            try {
                JSONArray objects = JSONObject.parseArray(dataSet.getCaseResult());
                if (objects.size() > 1) {
                    Object o = objects.get(0);
                    objects = new JSONArray();
                    objects.add(o);
                    dataSet.setCaseResult(JSON.toJSONString(objects, SerializerFeature.WriteMapNullValue));
                }
            } catch (Exception e) {
                log.info("结果集只保留一行数据失败...{}", e.getMessage());
            }
        }
        dataSetMapper.insert(dataSet);
        //2.更新查询参数
        dataSetParamBatch(dataSetParamDtoList, dto.getSetCode());

        //3.更新数据转换
        dataSetTransformBatch(dataSetTransformDtoList, dto.getSetCode());
        return dto;
    }

    /**
     * 更新数据集、添加查询参数、数据转换
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSet(DataSetDto dto) {
        List<DataSetParamDto> dataSetParamDtoList = dto.getDataSetParamDtoList();
        List<DataSetTransformDto> dataSetTransformDtoList = dto.getDataSetTransformDtoList();
        //1.更新数据集
        DataSet dataSet = new DataSet();
        BeanUtils.copyProperties(dto, dataSet);
        if (StringUtils.isNotBlank(dataSet.getCaseResult())) {
            try {
                JSONArray objects = JSONObject.parseArray(dataSet.getCaseResult());
                if (objects.size() > 1) {
                    Object o = objects.get(0);
                    objects = new JSONArray();
                    objects.add(o);
                    dataSet.setCaseResult(JSON.toJSONString(objects, SerializerFeature.WriteMapNullValue));
                }
            } catch (Exception e) {
                log.info("结果集只保留一行数据失败...{}", e.getMessage());
            }
        }
        dataSetMapper.update(new QueryWrapper<>(dataSet));

        //2.更新查询参数
        dataSetParamBatch(dataSetParamDtoList, dto.getSetCode());

        //3.更新数据转换
        dataSetTransformBatch(dataSetTransformDtoList, dto.getSetCode());
    }


    /**
     * 删除数据集、添加查询参数、数据转换
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSet(Long id) {
        DataSet dataSet = dataSetMapper.selectById(id);
        String setCode = dataSet.getSetCode();
        //1.删除数据集
        dataSetMapper.deleteById(id);

        //2.删除查询参数
        dataSetParamMapper.delete(
                new QueryWrapper<DataSetParam>()
                        .lambda()
                        .eq(DataSetParam::getSetCode, setCode)
        );

        //3.删除数据转换
        dataSetTransformMapper.delete(
                new QueryWrapper<DataSetTransform>()
                        .lambda()
                        .eq(DataSetTransform::getSetCode, setCode)
        );
    }

    /**
     * 获取数据
     *
     * @param dto
     * @return
     */
    @Override
    public OriginalDataDto getData(DataSetDto dto) {

        OriginalDataDto originalDataDto = new OriginalDataDto();
        String setCode = dto.getSetCode();
        if (StringUtils.isBlank(setCode)) {
            return new OriginalDataDto(new ArrayList<>());
        }
        //1.获取数据集、参数替换、数据转换
        DataSetDto dataSetDto = detailSet(setCode);
        String dynSentence = dataSetDto.getDynSentence();
        //2.获取数据源
        DataSource dataSource;
        if (StringUtils.isNotBlank(dataSetDto.getSetType())
                && dataSetDto.getSetType().equals(SetTypeEnum.HTTP.getCodeValue())) {
            //http不需要数据源，兼容已有的逻辑，将http所需要的数据塞进DataSource
            dataSource = new DataSource();
            dataSource.setSourceConfig(dynSentence);
            dataSource.setSourceType(JdbcConstants.HTTP);
            String body = JSONObject.parseObject(dynSentence).getString("body");
            if (StringUtils.isNotBlank(body)) {
                dynSentence = body;
            } else {
                dynSentence = "{}";
            }

        } else {
            dataSource = dataSourceMapper.selectOne(
                    new QueryWrapper<DataSource>()
                            .lambda()
                            .eq(DataSource::getSourceCode, dataSetDto.getSourceCode())
            );
        }

        //3.参数替换
        //3.1参数校验
        log.debug("参数校验替换前：{}", dto.getContextData());
        boolean verification = dataSetParamService.verification(dataSetDto.getDataSetParamDtoList(), dto.getContextData());
        if (!verification) {
            throw BusinessExceptionBuilder.build("500");
        }
        dynSentence = dataSetParamService.transform(dto.getContextData(), dynSentence);
        log.debug("参数校验替换后：{}", dto.getContextData());
        //4.获取数据
        DataSourceDto dataSourceDto = new DataSourceDto();
        BeanUtils.copyProperties(dataSource, dataSourceDto);
        dataSourceDto.setDynSentence(dynSentence);
        dataSourceDto.setContextData(dto.getContextData());
        //获取total,判断contextData中是否传入分页参数
        if (null != dto.getContextData()
                && dto.getContextData().containsKey("pageNumber")
                && dto.getContextData().containsKey("pageSize")) {
            long total = dataSourceService.total(dataSourceDto, dto);
            originalDataDto.setTotal(total);
        }
        List<JSONObject> data = dataSourceService.execute(dataSourceDto);
        //5.数据转换
        List<JSONObject> transform = dataSetTransformService.transform(dataSetDto.getDataSetTransformDtoList(), data);
        originalDataDto.setData(transform);
        return originalDataDto;
    }

    /**
     * @param dto
     * @return
     */
    @Override
    public OriginalDataDto testTransform(DataSetDto dto) {
        String dynSentence = dto.getDynSentence();

        OriginalDataDto originalDataDto = new OriginalDataDto();
        String sourceCode = dto.getSourceCode();
        //1.获取数据源
        DataSource dataSource;
        if (dto.getSetType().equals(SetTypeEnum.HTTP.getCodeValue())) {
            //http不需要数据源，兼容已有的逻辑，将http所需要的数据塞进DataSource
            dataSource = new DataSource();
            dataSource.setSourceConfig(dynSentence);
            dataSource.setSourceType(JdbcConstants.HTTP);
            String body = JSONObject.parseObject(dynSentence).getString("body");
            if (StringUtils.isNotBlank(body)) {
                dynSentence = body;
            } else {
                dynSentence = "{}";
            }

        } else {
            dataSource = dataSourceMapper.selectOne(
                    new QueryWrapper<DataSource>()
                            .lambda()
                            .eq(DataSource::getSourceCode, sourceCode)
            );
        }

        //3.参数替换
        //3.1参数校验
        boolean verification = dataSetParamService.verification(dto.getDataSetParamDtoList(), null);
        if (!verification) {
            throw BusinessExceptionBuilder.build("500");
        }

        dynSentence = dataSetParamService.transform(dto.getDataSetParamDtoList(), dynSentence);
        //4.获取数据
        DataSourceDto dataSourceDto = new DataSourceDto();
        BeanUtils.copyProperties(dataSource, dataSourceDto);
        dataSourceDto.setDynSentence(dynSentence);
        dataSourceDto.setContextData(setContextData(dto.getDataSetParamDtoList()));

        //获取total,判断DataSetParamDtoList中是否传入分页参数
        Map<String, Object> collect = dto.getDataSetParamDtoList().stream().collect(Collectors.toMap(DataSetParamDto::getParamName, DataSetParamDto::getSampleItem));
        if (collect.containsKey("pageNumber") && collect.containsKey("pageSize")) {
            dto.setContextData(collect);
            long total = dataSourceService.total(dataSourceDto, dto);
            originalDataDto.setTotal(total);
        }

        List<JSONObject> data = dataSourceService.execute(dataSourceDto);
        //5.数据转换
        List<JSONObject> transform = dataSetTransformService.transform(dto.getDataSetTransformDtoList(), data);
        originalDataDto.setData(transform);
        return originalDataDto;
    }


    /**
     * 获取所有数据集
     *
     * @return
     */
    @Override
    public List<DataSet> queryAllDataSet() {
        LambdaQueryWrapper<DataSet> wrapper = Wrappers.lambdaQuery();
        wrapper.select(DataSet::getSetCode, DataSet::getSetName, DataSet::getSetDesc, DataSet::getId)
                .eq(DataSet::getEnableFlag, Enabled.YES.getValue());
        wrapper.orderByDesc(DataSet::getUpdateTime);
        return dataSetMapper.selectList(wrapper);
    }

    @Override
    public void copy(DataSetDto dto) {
        if (null == dto.getId()) {
            throw BusinessExceptionBuilder.build("500", "id");
        }
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(dto.getSetCode())) {
            throw BusinessExceptionBuilder.build("500", "数据集编码");
        }
        DataSet dataSet = dataSetMapper.selectById(dto.getId());
        String setCode = dataSet.getSetCode();
        DataSet dateSetCopy = copyDataSet(dataSet, dto);
        dataSetMapper.insert(dateSetCopy);
        String copySetCode = dateSetCopy.getSetCode();

        List<DataSetParam> dataSetParamList = dataSetParamMapper.selectList(
                new QueryWrapper<DataSetParam>()
                        .lambda()
                        .eq(DataSetParam::getSetCode, setCode)
        );
        if (!CollectionUtils.isEmpty(dataSetParamList)){
            dataSetParamList.forEach(dataSetParam -> {
                dataSetParam.setId(null);
                dataSetParam.setSetCode(copySetCode);
                dataSetParamMapper.insert(dataSetParam);
            });
            //dataSetParamService.insertBatch(dataSetParamList);
        }

        List<DataSetTransform> dataSetTransformList = dataSetTransformMapper.selectList(
                new QueryWrapper<DataSetTransform>()
                        .lambda()
                        .eq(DataSetTransform::getSetCode, setCode)
        );
        if (!CollectionUtils.isEmpty(dataSetTransformList)){
            dataSetTransformList.forEach(dataSetTransform -> {
                dataSetTransform.setId(null);
                dataSetTransform.setSetCode(copySetCode);
                dataSetTransformMapper.insert(dataSetTransform);
            });
            //dataSetTransformService.insertBatch(dataSetTransformList);
        }
    }

    public void dataSetParamBatch(List<DataSetParamDto> dataSetParamDtoList, String setCode) {
        dataSetParamMapper.delete(
                new QueryWrapper<DataSetParam>()
                        .lambda()
                        .eq(DataSetParam::getSetCode, setCode)
        );
        if (null == dataSetParamDtoList || dataSetParamDtoList.size() <= 0) {
            return;
        }
//        List<DataSetParam> dataSetParamList = new ArrayList<>();
        dataSetParamDtoList.forEach(dataSetParamDto -> {
            DataSetParam dataSetParam = new DataSetParam();
            BeanUtils.copyProperties(dataSetParamDto, dataSetParam);
            dataSetParam.setSetCode(setCode);
            //不采用批量
            dataSetParamMapper.insert(dataSetParam);
//            dataSetParamList.add(dataSetParam);
        });
//        dataSetParamService.insertBatch(dataSetParamList);

    }

    public void dataSetTransformBatch(List<DataSetTransformDto> dataSetTransformDtoList, String setCode) {
        dataSetTransformMapper.delete(
                new QueryWrapper<DataSetTransform>()
                        .lambda()
                        .eq(DataSetTransform::getSetCode, setCode)
        );
        if (null == dataSetTransformDtoList || dataSetTransformDtoList.size() <= 0) {
            return;
        }
//        List<DataSetTransform> dataSetTransformList = new ArrayList<>();
        for (int i = 0; i < dataSetTransformDtoList.size(); i++) {
            DataSetTransform dataSetTransform = new DataSetTransform();
            BeanUtils.copyProperties(dataSetTransformDtoList.get(i), dataSetTransform);
            dataSetTransform.setOrderNum(i + 1);
            dataSetTransform.setSetCode(setCode);
            //不采用批量
            dataSetTransformMapper.insert(dataSetTransform);
//            dataSetTransformList.add(dataSetTransform);
        }
//        dataSetTransformService.insertBatch(dataSetTransformList);
    }

    /**
     * dataSetParamDtoList转map
     *
     * @param dataSetParamDtoList
     * @return
     */
    public Map<String, Object> setContextData(List<DataSetParamDto> dataSetParamDtoList) {
        Map<String, Object> map = new HashMap<>();
        if (null != dataSetParamDtoList && dataSetParamDtoList.size() > 0) {
            dataSetParamDtoList.forEach(dataSetParamDto -> map.put(dataSetParamDto.getParamName(), dataSetParamDto.getSampleItem()));
        }
        return map;
    }

    private DataSet copyDataSet(DataSet dataSet, DataSetDto dto){
        //复制主表数据
        DataSet copyDataSet = new DataSet();
        BeanUtils.copyProperties(dataSet, copyDataSet);
        copyDataSet.setSetCode(dto.getSetCode());
        copyDataSet.setSetName(dto.getSetName());
        copyDataSet.setId(null);
        return copyDataSet;
    }

}