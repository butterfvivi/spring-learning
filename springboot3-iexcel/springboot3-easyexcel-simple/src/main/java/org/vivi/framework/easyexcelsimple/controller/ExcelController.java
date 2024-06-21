package org.vivi.framework.easyexcelsimple.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.easyexcelsimple.common.enums.ResponseCodeEnum;
import org.vivi.framework.easyexcelsimple.common.response.R;
import org.vivi.framework.easyexcelsimple.common.utils.EasyExcelUtils;
import org.vivi.framework.easyexcelsimple.model.dto.UserDto;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class ExcelController {

    @PostMapping(value = "/import")
    public R<?> importExcel(MultipartFile file){
        if (null == file || file.isEmpty()) {
            return R.failed(ResponseCodeEnum.FILE_EMPTY);
        }

        long start = System.currentTimeMillis();
        List<UserDto> list = null;
        try {
            list = EasyExcelUtils.readExcel(file.getInputStream(), UserDto.class, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("导入数据耗时:{} ms", System.currentTimeMillis() - start);
        return R.ok(list);
    }

    @PostMapping(value = "/export")
    public void exportExcel(){

    }

    @PostMapping(value = "/dowload")
    public void dowload(){

    }
}
