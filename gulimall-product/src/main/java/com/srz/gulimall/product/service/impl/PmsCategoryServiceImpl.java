package com.srz.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.srz.gulimall.product.service.PmsCategoryBrandRelationService;
import com.srz.gulimall.product.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.Query;

import com.srz.gulimall.product.dao.PmsCategoryDao;
import com.srz.gulimall.product.entity.PmsCategoryEntity;
import com.srz.gulimall.product.service.PmsCategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("pmsCategoryService")
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryDao, PmsCategoryEntity> implements PmsCategoryService {

//    @Autowired
//    PmsCategoryDao categoryDao;

    @Autowired
    PmsCategoryBrandRelationService pmsCategoryBrandRelationService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redisson;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsCategoryEntity> page = this.page(
                new Query<PmsCategoryEntity>().getPage(params),
                new QueryWrapper<PmsCategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<PmsCategoryEntity> listWithTree() {
        //1、查出所有分类
        List<PmsCategoryEntity> entities = baseMapper.selectList(null);

        //2、组装成父子的树形结构
        List<PmsCategoryEntity> collect = entities.stream().filter(e -> e.getParentCid() == 0)
                .map((menu) -> {
                    menu.setChildren(getChilderns(menu, entities));
                    return menu;
                }).sorted((menu1, menu2) -> {
                    return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
                }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 1、检查当前删除的菜单，是否被别的地方引用
        //逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    //2,25,225
    @Override
    public Long[] findCatelogPath(Long attrGroupId1) {
        List<Long> paths = new ArrayList<>();
        findParentPath(attrGroupId1, paths);

        Collections.reverse(paths);

        return (Long[]) paths.toArray(new Long[paths.size()]);
    }

    /**
     * 级联更新所有关联的数据
     *@CacheEvict失效模式使用
     * 1、同时进行多种缓存操作
     * 2、指定删除某个分区下的所有数据    @CacheEvict(value = "category", allEntries = true)
     * 3、存储同一类型的数据，都可以指定成同一个分区。分区名默认就是缓存的前缀
     * @param pmsCategory
     */
//    @Caching(evict = {
//            @CacheEvict(value = "category",key = "'getLevel1Categorys'"),
//            @CacheEvict(value = "category",key = "'getCatalogJson'")
//    })
    //allEntries 清除分区
    @CacheEvict(value = "category", allEntries = true)//失效模式
//    @CachePut//双写模式
    @Transactional
    @Override
    public void updateCascade(PmsCategoryEntity pmsCategory) {
        this.updateById(pmsCategory);
        pmsCategoryBrandRelationService.updateCategory(pmsCategory.getCatId(), pmsCategory.getName());

        //同时修改缓存中的数据
        //redis.del("catalogJSON");等待下次主动查询进行更新
    }

    /*
     1、每一个需要缓存的数据我们都来指定要放到哪个名字的缓存【缓存的分区（按照业务类型分）】
     2、@Cacheable({"category"})
        代表当前方法的结果需要缓存，如果缓存中有，方法不调用，
        如果缓存中没有，会调用方法，最后将方法的结果放入缓存;
     3、默认行为
        1、如果缓存中有，方法不调用
        2、key默认自动生成：缓存的名字::SimpleKey []（自动生成的kdy值）
            category::SimpleKey []
        3、缓存的value的值。默认使用jdk序列化机制存入redis
        4、默认ttl过期时间 -1;
       自定义：
          1、指定生成的缓存使用的key，key属性指定。接受一个spEl
          2、指定缓存的数据的存活时间, 配置文件中修改ttl
          3、将数据保存为json格式:
            CacheAutoConfiguration
            RedisCacheConfiguration
     4、Spring-Cache的不足;
        1、读模式：
            缓存穿透：查询一个null数据，解决：缓存空数据 cache-null-values=true
            缓存击穿：大量并发同时查询一个正好过期的数据。解决：sync = true(加锁)
            缓存雪崩：大量的key同时过期，解决：加上过期时间
        2、写模式：（缓存与数据一致）
            1、读写加锁
            2、引入Canal，感知MySQL的更新去更新数据库
            3、读多写多，直接去数据库查询就行
        总结：
            常规数据（读多写少，即时性，一致性不高要求不高的数据）使用Spring-Cache（只要缓存有过期时间就足够了）
            特殊数据：特殊设计
     原理：
        CacheManager(RedisCacheManager)->Cache(RedisCache)->Cache负责缓存的读写
     */
    @Cacheable(value = {"category"}, key = "#root.method.name",sync = true)
    @Override
    public List<PmsCategoryEntity> getLevel1Categorys() {
        System.out.println("getLevel1Categorys....");
        List<PmsCategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<PmsCategoryEntity>().eq("parent_cid", 0));

        return categoryEntities;
    }

    //TODO 产生堆外内存溢出：OutOfDirectMemoryError
    //(使用远程服务器暂未测出，带宽导致数据量不大)
    //1、springboot2.0默认使用lettuce作为操作redis的客户端，它使用netty进行网络通信
    //2) 、lettuce的bug导致netty堆外内存溢出 - Xmx300m; netty如果没有指定堆外内存，默认使用-Xmx300m
    // 可以通过-Dio.netty.maxDirectMemory进行设置
    // 解决方案:不能使用-Dio. netty. maxDirec tMemory只去调大堆外内存。
    // )、升级lettuce客户端。 2) 、切换使用jedis
    // 弹幕： 5.2.0以后问题解决了
    //使用jedis后read time out
    //    redis Template:
    //    lettuce、jedis操作redis的底层客户端。Spring再次封装redisTemplate
    //@Override
    public Map<String, List<Catelog2Vo>> getCatalogJson2() {

        /**
         * 1、空结果缓存：解决站在穿透
         * 2、设置过期时间（加随机值）：解决缓存雪崩
         * 3、加锁：解决缓存击穿
         */
        //1、加入缓存逻辑、缓存中存的数据是json字符串
        //JSON跨语言，跨平台兼容，
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if (StringUtils.isEmpty(catalogJSON)) {
            System.out.println("缓存不命中，查询数据库");
            //2、缓存中没有，查询数据库
            Map<String, List<Catelog2Vo>> catalogJsonFromDb = getCatalogJsonFromDbWhitRedisLock();

            return catalogJsonFromDb;
        }
        System.out.println("缓存命中，直接返回");
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });


        return result;
    }

    @Cacheable(value = {"category"}, key = "#root.method.name")
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        System.out.println("查询了数据库......");
        List<PmsCategoryEntity> selectList = baseMapper.selectList(null);
        List<PmsCategoryEntity> level1Categorys = getParent_cid(selectList, 0L);
        //2、封装数据
        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1、每一个的一级分类,查到这个一级分类的二级分类
            List<PmsCategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
            //2、封装上面结果
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //baseMapper.selectList(new QueryWrapper<PmsCategoryEntity>().eq("parent_cid", l2.getCatId()));
                    List<PmsCategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());
                    if (level3Catelog != null) {
                        List<Catelog2Vo.Catelog3Vo> collect = level3Catelog.stream().map(l3 -> {
                            //2、封装成指定格式
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());

                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));


        return parent_cid;
    }

    /*
            缓存里面的数据如何和数据库保持一致
            缓存数据一致性
            1、双写模式
            2、失效模式
         */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWhitRedissonLock() {
        //1、//1、占分布式锁。去redis占坑 redis nx

        //1、锁的名字。锁的粒度，越细越快
        //锁的粒度：具体缓存的是某个数据，11-号商品 prduct-11-lock prduct-12f-lock
        RLock lock = redisson.getLock("CatalogJson-lock");
        lock.lock();

        Map<String, List<Catelog2Vo>> dataFromDb;
        try{
            dataFromDb = getDataFromDb();
        } finally {
            lock.unlock();
        }
        return dataFromDb;

    }

    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWhitRedisLock() {
        //1、占分布式锁。去redis占坑
        String uuid = UUID.randomUUID().toString();
        int count = 0;
        //1、占分布式钟锁redis nx
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid,300,TimeUnit.SECONDS);
        if (lock) {
            System.out.println("获取分布锁成功...");
            //加锁成功
            //设置过期时间,防止死锁,必须和
            //error/redisTemplate.expire("lock",30,TimeUnit.SECONDS);
            Map<String, List<Catelog2Vo>> dataFromDb;
            try{
                dataFromDb = getDataFromDb();
            } finally {
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                //删除锁
                redisTemplate.execute(new DefaultRedisScript<Long>(script,Long.class),Arrays.asList("lock"),uuid);
            }

            //删除锁
//            String lockValue = redisTemplate.opsForValue().get("lock");
//            if (uuid.equals(lockValue)){
//                redisTemplate.delete("lock");
//            }

            return dataFromDb;
        }else {
            System.out.println("获取分布锁失败---等待重试！");
            //重试10次
            if (count>10){
                return null;
            }
            //加锁失败。。休眠200ms重试
            try {
                Thread.sleep(200);
                count++;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return getCatalogJsonFromDbWhitRedisLock();
        }
    }

    private Map<String, List<Catelog2Vo>> getDataFromDb() {
        //得到锁以后，我们应该再去缓存中确定一次，如果没有才需要继续查询
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if (!StringUtils.isEmpty(catalogJSON)) {
            //缓存不为null直接返回
            Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
            return result;
        }
        System.out.println("查询了数据库......");

        List<PmsCategoryEntity> selectList = baseMapper.selectList(null);

        List<PmsCategoryEntity> level1Categorys = getParent_cid(selectList, 0L);

        //2、封装数据
        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1、每一个的一级分类,查到这个一级分类的二级分类
            List<PmsCategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
            //2、封装上面结果
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //baseMapper.selectList(new QueryWrapper<PmsCategoryEntity>().eq("parent_cid", l2.getCatId()));
                    List<PmsCategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());
                    if (level3Catelog != null) {
                        List<Catelog2Vo.Catelog3Vo> collect = level3Catelog.stream().map(l3 -> {
                            //2、封装成指定格式
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());

                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));

        //3、查到的数据再放入缓存,将对象转为json放在缓存中
        String jsonString = JSON.toJSONString(parent_cid);
        redisTemplate.opsForValue().set("catalogJSON", jsonString, 1, TimeUnit.DAYS);

        return parent_cid;
    }

    //从数据库查询并封装分类数据
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWhitLocalLock() {
        //只要是同一把锁，就能锁住需要这个锁的所有线程
        //1、synchronized (this): SpringBoot所有 的组件在容器中都是单例的。
        //TODO 本地锁synchronized JUC(Lock)在分布情况下，必须使用分布式锁

        synchronized (this) {
            //得到锁以后，我们应该再去缓存中确定一次，如果没有才需要继续查询
            return getDataFromDb();
        }
    }

    private List<PmsCategoryEntity> getParent_cid(List<PmsCategoryEntity> selectList, Long parent_cid) {
        //return baseMapper.selectList(new QueryWrapper<PmsCategoryEntity>().eq("parent_cid", v.getCatId()));
        List<PmsCategoryEntity> collect = selectList.stream().filter(item -> item.getParentCid() == parent_cid).collect(Collectors.toList());
        return collect;
    }

    //225,25,2
    private List<Long> findParentPath(Long attrGroupId1, List<Long> paths) {
        //1、 收集当前节点id
        paths.add(attrGroupId1);
        PmsCategoryEntity byId = this.getById(attrGroupId1);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }

        return paths;
    }

    //递归查找所有的子菜单
    private List<PmsCategoryEntity> getChilderns(PmsCategoryEntity root, List<PmsCategoryEntity> all) {

        List<PmsCategoryEntity> children = all.stream().filter(categoryEntity ->
                        categoryEntity.getParentCid() == root.getCatId())
                .map(categoryEntity -> {
                    //1、找到子菜单
                    categoryEntity.setChildren(getChilderns(categoryEntity, all));
                    return categoryEntity;
                }).sorted((menu1, menu2) -> {
                    //2、菜单的排序
                    return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
                }).collect(Collectors.toList());

        return children;
    }

}