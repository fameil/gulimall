package com.srz.gulimall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.srz.gulimall.product.entity.PmsAttrEntity;
import com.srz.gulimall.product.service.PmsAttrAttrgroupRelationService;
import com.srz.gulimall.product.service.PmsAttrService;
import com.srz.gulimall.product.service.PmsCategoryService;
import com.srz.gulimall.product.vo.AttrGroupReleationVo;
import com.srz.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.srz.gulimall.product.entity.PmsAttrGroupEntity;
import com.srz.gulimall.product.service.PmsAttrGroupService;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.R;



/**
 * 属性分组
 *
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-09-06 16:28:58
 */
@RestController
@RequestMapping("product/pmsattrgroup")
public class PmsAttrGroupController {
    @Autowired
    private PmsAttrGroupService pmsAttrGroupService;

    @Autowired
    private PmsCategoryService pmsCategoryService;

    @Autowired
    PmsAttrService attrService;

    @Autowired
    PmsAttrAttrgroupRelationService relationService;

    ///product/pmsattrgroup/attr/relation
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupReleationVo> vos){
        relationService.saveBatch(vos);


        return R.ok();
    }

    ///product/attrgroup/{catelogId}/withattr
    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId){

        //1、查出当前分类下的所有属性分组。
        //2、查出每个属性分组的所有属性.
        List<AttrGroupWithAttrsVo> vos =pmsAttrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);

        return R.ok().put("data",vos);
    }


    ///product/pmsattrgroup/attr/relation/delete
    //[{attrId: 2, attrGroupId: 1}]
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupReleationVo[] vos){
        attrService.deleteRelation(vos);


        return R.ok();
    }

    //product/pmsattrgroup/{attrgroupId}/attr/relatio?t=1668648603164
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
        System.out.println("notfound!");
        List<PmsAttrEntity> entities = attrService.getRelationAttr(attrgroupId);

        return R.ok().put("data",entities);
    }

    //product/pmsattrgroup/{attrgroupId}/attr/relatio?t=1668648603164
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                            @RequestParam Map<String, Object> params){
        PageUtils page = attrService.getNoRelationAttr(params,attrgroupId);
        return R.ok().put("page",page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:pmsattrgroup:list")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId){
        //PageUtils page = pmsAttrGroupService.queryPage(params);
        PageUtils page = pmsAttrGroupService.queryPage(params,catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:pmsattrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		PmsAttrGroupEntity pmsAttrGroup = pmsAttrGroupService.getById(attrGroupId);
        Long catelogId = pmsAttrGroup.getCatelogId();

        Long[] path = pmsCategoryService.findCatelogPath(catelogId);

        pmsAttrGroup.setCatelogPath(path);

        return R.ok().put("pmsAttrGroup", pmsAttrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:pmsattrgroup:save")
    public R save(@RequestBody PmsAttrGroupEntity pmsAttrGroup){
		pmsAttrGroupService.save(pmsAttrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:pmsattrgroup:update")
    public R update(@RequestBody PmsAttrGroupEntity pmsAttrGroup){
		pmsAttrGroupService.updateById(pmsAttrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:pmsattrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		pmsAttrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
