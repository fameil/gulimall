package com.srz.gulimall.product.vo;

import lombok.Data;

/**
 * @author srz
 * @create 2022/11/13 15:57
 */
@Data
public class AttrRespVo extends AttrVo{
/*          "catelogName": "手机/数码/手机", //所属分类名字
            "groupName": "主体", //所属分组名字*/

        private String catelogName;
        private String groupName;

        private Long[] catelogPath;
}
