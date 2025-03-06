package org.vivi.framework.easyexcel.simple.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.easyexcel.simple.common.enums.ResponseCodeEnum;
import org.vivi.framework.easyexcel.simple.common.response.R;
import org.vivi.framework.easyexcel.simple.common.utils.BeanTransformUtils;
import org.vivi.framework.easyexcel.simple.common.utils.EasyExcelUtils;
import org.vivi.framework.easyexcel.simple.service.UserService;
import org.vivi.framework.easyexcel.simple.model.dto.UserDto;
import org.vivi.framework.easyexcel.simple.model.entity.User;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class ExcelController {

    @Autowired
    private UserService userService;

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

    @PostMapping(value = "/import_2")
    public R<?> importAndSave(MultipartFile file){
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
        List<User> orderList = BeanTransformUtils.transformList(list, User.class);
        userService.saveBatch(orderList);
        log.info("导入数据耗时:{} ms", System.currentTimeMillis() - start);
        return R.ok();
    }

    @PostMapping(value = "/export")
    public void exportExcel(HttpServletResponse response){
        long start = System.currentTimeMillis();
        EasyExcelUtils.writeExcel(response, ("User-" + start), "Sheet1", userService.getAllUser());
        log.info("导出数据耗时:{} ms", System.currentTimeMillis() - start);
    }

    @PostMapping(value = "/dowload")
    public void dowload(){

    }
}
