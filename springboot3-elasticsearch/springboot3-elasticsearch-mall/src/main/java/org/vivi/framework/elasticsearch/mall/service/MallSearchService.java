package org.vivi.framework.elasticsearch.mall.service;

import org.vivi.framework.elasticsearch.mall.vo.ESRequestParam;
import org.vivi.framework.elasticsearch.mall.vo.ESResponseResult;

public interface MallSearchService {


    /**
     * @param param 检索的所有参数
     * @return  返回检索的结果，里面包含页面需要的所有信息
     */
    ESResponseResult search(ESRequestParam param);
}
