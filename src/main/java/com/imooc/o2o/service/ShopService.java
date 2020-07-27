package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exception.ShopException;

import java.io.File;
import java.io.InputStream;

public interface ShopService {

    /**
     * 注册店铺
     *
     * @param shop
     * @param thumbnail
     * @return
     */
    ShopExecution addShop(Shop shop, ImageHolder thumbnail) throws ShopException;


    /**
     * 修改店铺信息
     * @param shop
     * @param thumbnail
     * @return
     */
    ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopException;

    /**
     * 获取店铺信息
     * @param shopId
     * @return
     */
    Shop getShopById(long shopId);

    /**
     * 查询店铺列表
     * @param shopCondition
     * @param pageIndex 页数
     * @param pageSize 每一页的条数
     * @return
     */
    ShopExecution getShopListAndCount(Shop shopCondition,int pageIndex,int pageSize);
}
