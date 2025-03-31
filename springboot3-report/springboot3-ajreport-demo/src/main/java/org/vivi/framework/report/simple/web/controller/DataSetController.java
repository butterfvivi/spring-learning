
package org.vivi.framework.report.simple.web.controller;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.report.simple.common.response.ResponseBean;
import org.vivi.framework.report.simple.web.dto.DataSetDto;
import org.vivi.framework.report.simple.modules.dataset.service.DataSetService;
import org.vivi.framework.report.simple.web.param.DataSetTestTransformParam;

/**
* @desc 数据集 controller
**/
@Slf4j
@RestController
@RequestMapping("/dataSet")
public class DataSetController  {

    @Autowired
    private DataSetService dataSetService;


    @GetMapping("/detailBysetId/{id}")
    public ResponseBean detailBysetId(@PathVariable("id") Long id) {
        log.info("{}根据ID查询服务开始，id为：{}", this.getClass().getSimpleName(), id);
        ResponseBean responseBean = ResponseBean.success(dataSetService.detailSet(id));
        log.info("{}根据ID查询结束，结果：{}", this.getClass().getSimpleName(), JSON.toJSONString(responseBean));
        return responseBean;
    }

    @GetMapping({"/detailBysetCode/{setCode}"})
    public ResponseBean detailBysetCode(@PathVariable("setCode") String setCode) {
        log.info("{}根据setCode查询服务开始，setCode为：{}", this.getClass().getSimpleName(), setCode);
        ResponseBean responseBean = ResponseBean.success(dataSetService.detailSet(setCode));
        log.info("{}根据setCode查询结束，结果：{}", this.getClass().getSimpleName(), JSON.toJSONString(responseBean));
        return responseBean;
    }

    @PostMapping
    public ResponseBean insert(@RequestBody DataSetDto dto) {
        log.info("{}新增服务开始，参数：{}", this.getClass().getSimpleName(), JSON.toJSONString(dto));
        DataSetDto dataSetDto = dataSetService.insertSet(dto);
        log.info("{}新增服务结束，结果：{}", this.getClass().getSimpleName(), JSON.toJSONString(dataSetDto));
        return ResponseBean.builder().data(dataSetDto).build();
    }

    @DeleteMapping({"/{id}"})
    public ResponseBean deleteById(@PathVariable("id") Long id) {
        log.info("{}删除服务开始，参数ID：{}", this.getClass().getSimpleName(), id);
        dataSetService.deleteSet(id);
        log.info("{}删除服务结束", this.getClass().getSimpleName());
        return ResponseBean.success();
    }

    /**
     * 测试 数据转换是否正确
     * @param param
     * @return
     */
    @PostMapping("/testTransform")
    public ResponseBean testTransform(@Validated @RequestBody DataSetTestTransformParam param) {
        DataSetDto dto = new DataSetDto();
        BeanUtils.copyProperties(param, dto);
        return ResponseBean.success(dataSetService.testTransform(dto));
    }

    /**
     * 获取所有数据集
     * @return
     */
    @GetMapping("/queryAllDataSet")
    public ResponseBean queryAllDataSet() {
        return ResponseBean.success(dataSetService.queryAllDataSet());
    }


    @PostMapping("/copy")
    public ResponseBean copy(@RequestBody DataSetDto dto) {
        dataSetService.copy(dto);
        return ResponseBean.builder().build();
    }

//    @PutMapping
//    public ResponseBean update(@RequestBody DataSetDto dto) {
//        String username = UserContentHolder.getContext().getUsername();
//        log.info("{}更新服务开始,更新人：{}，参数：{}", this.getClass().getSimpleName(), username, JSON.toJSONString(dto));
//        ResponseBean responseBean = this.responseSuccess();
//        dataSetService.updateSet(dto);
//        log.info("{}更新服务结束，结果：{}", this.getClass().getSimpleName(), JSON.toJSONString(responseBean));
//        return this.responseSuccess();
//    }
}
