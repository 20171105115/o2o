package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopDao {

    /**
     * 插入店铺
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺
     * @param shop
     * @return
     */
    int updateShop(Shop shop);

    /**
     * 通过shopId查询店铺信息，为修改做准备
     * @param shopId
     * @return
     */
    Shop queryShopById(long shopId);

    /**
     * 通过条件查询商铺列表 一、店铺名 二、区域 三、店铺分类 四、人员信息
     * 分页查询商铺列表 rowIndex起始位置 pageSize接下来查询的行数
     * @param shopCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,@Param("rowIndex") int rowIndex
    ,@Param("pageSize") int pageSize);

    /**
     * 查询相同条件下的店铺总数,为了之后分页做准备
     * @param shopCondition
     * @return
     */
    int queryShopCount(@Param("shopCondition") Shop shopCondition);



}
