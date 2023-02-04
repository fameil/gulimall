package com.srz.gulimall.seartch.service.impl;

import com.srz.gulimall.seartch.service.MallSearchService;
import com.srz.gulimall.seartch.vo.SearchParam;
import com.srz.gulimall.seartch.vo.SearchResult;
import org.springframework.stereotype.Service;

/**
 * @author srz
 * @create 2023/2/1 10:56
 */

@Service
public class MallSearchServiceImpl implements MallSearchService {
    //去es进行检索
    @Override
    public SearchResult search(SearchParam parm) {
        return null;
    }
}
