package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Shop;
import org.springframework.stereotype.Repository;

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



}
