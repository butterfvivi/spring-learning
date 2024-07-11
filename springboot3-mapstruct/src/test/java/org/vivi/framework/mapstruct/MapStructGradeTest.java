package org.vivi.framework.mapstruct;

import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.vivi.framework.mapstruct.mapper.GradeMapper;
import org.vivi.framework.mapstruct.model.entity.Grade;
import org.vivi.framework.mapstruct.model.vo.GradeVO;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class MapStructGradeTest {

    @Test
    void personToPersonDto() {
        Map<String, Grade> map = new HashMap<String, Grade>(8) {{
            put("1", Grade.builder().id(1).gradeName("一年级").build());
            put("2", Grade.builder().id(2).gradeName("二年级").build());
        }};

        Map<String, GradeVO> stringGradeVOMap = GradeMapper.INSTANCE.mapConvert(map);
        // 输出PersonDto(id=1, name=张三, age=25)
        System.out.println(JSON.toJSONString(stringGradeVOMap));
    }

}
