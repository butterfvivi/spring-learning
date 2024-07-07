package org.vivi.framework.mapstruct.controller;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.mapstruct.mapper.GradeMapper;
import org.vivi.framework.mapstruct.model.entity.Grade;
import org.vivi.framework.mapstruct.model.vo.GradeVO;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class StudentController {

    @Resource
    private GradeMapper gradeMapper;

    @GetMapping("/mapConvert")
    public String mapConvert() {
        Map<String, Grade> map = new HashMap<String, Grade>(8) {{
            put("1", Grade.builder().id(1).gradeName("一年级").build());
            put("2", Grade.builder().id(2).gradeName("二年级").build());
        }};

        Map<String, GradeVO> dtoMap = gradeMapper.mapConvert(map);
        String result = JSON.toJSONString(dtoMap);
        return result;
    }
}
