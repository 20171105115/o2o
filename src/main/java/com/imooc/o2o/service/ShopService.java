package com.imooc.o2o.service;

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
     * @param shopImgInputStream
     * @param fileName
     * @return
     */
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopException;


    /**
     * 修改店铺信息
     * @param shop
     * @param shopImg
     * @param fileName
     * @return
     */
    ShopExecution modifyShop(Shop shop,InputStream shopImg,String fileName) throws ShopException;

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
    ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);
}
