package com.srz.gulimall.seartch.service;

import com.srz.gulimall.seartch.vo.SearchParam;
import com.srz.gulimall.seartch.vo.SearchResult;

/**
 * @author srz
 * @create 2023/2/1 10:55
 */
public interface MallSearchService {

    /**
     * @param parm 检索的所有参数
     * @return 返回检索的结果,里面包含页面的所有信息
     */
    SearchResult search(SearchParam parm);
}
