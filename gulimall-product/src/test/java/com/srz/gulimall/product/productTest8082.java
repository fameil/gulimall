package com.srz.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.srz.gulimall.product.entity.PmsBrandEntity;
import com.srz.gulimall.product.service.PmsBrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * @author srz
 * @create 2022/9/8 16:23
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class

productTest8082 {

    @Autowired
    PmsBrandService brandService;



    @Test
    public void Test1() throws IOException {
        PmsBrandEntity brandEntity = new PmsBrandEntity();
/*       //测试保存
        brandEntity.setName("华为");
        brandService.save(brandEntity);*/
        brandEntity.setBrandId(1L);
        brandEntity.setDescript("测试修改");
        brandService.updateById(brandEntity);
        List<PmsBrandEntity> list = brandService.list(new QueryWrapper<PmsBrandEntity>().eq("brand_id", 1L));
        list.forEach((item)->{
            System.out.println(item);
        });


        System.out.println("====================");;
        System.out.println(brandEntity.toString());
    }

}



