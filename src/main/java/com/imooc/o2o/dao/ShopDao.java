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



}
