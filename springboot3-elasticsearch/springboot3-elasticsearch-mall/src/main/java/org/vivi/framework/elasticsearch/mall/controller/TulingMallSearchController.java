package org.vivi.framework.elasticsearch.mall.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.elasticsearch.mall.common.CommonResult;
import org.vivi.framework.elasticsearch.mall.service.MallSearchService;
import org.vivi.framework.elasticsearch.mall.vo.ESRequestParam;
import org.vivi.framework.elasticsearch.mall.vo.ESResponseResult;


@RestController
public class TulingMallSearchController {

     @Autowired
     private MallSearchService mallSearchService;

    /**
     * 自动将页面提交过来的所有请求参数封装成我们指定的对象
     * 测试： http://192.168.65.103:8054/searchList?price=1_5000&keyword=手机&sort=salecount_asc&hasStock=1&pageNum=1&pageSize=20&categoryId=19&attrs=2_蓝色&attrs=1_2核
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/searchList")
    public CommonResult<ESResponseResult> listPage(ESRequestParam param, HttpServletRequest request) {

        //1、根据传递来的页面的查询参数，去es中检索商品
        ESResponseResult searchResult = mallSearchService.search(param);

        return CommonResult.success(searchResult);
    }


}
