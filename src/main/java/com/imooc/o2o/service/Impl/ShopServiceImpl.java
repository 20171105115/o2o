package com.imooc.o2o.service.Impl;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exception.ShopException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    /**
     * 添加店铺信息
     * 1、空值判断
     * 2、添加店铺信息
     * 3、图片空值判断以及添加图片
     * 4、更新店铺信息
     * @param shop
     * @param shopImg
     * @return
     */
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, File shopImg) {
        if (shop == null){//空值判断
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }else{
            //初始值赋值
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            try {
                //更新店铺信息
                int effectedNum = shopDao.insertShop(shop);
                if (effectedNum <= 0){
                    throw new RuntimeException("添加店铺失败");
                }else {
                    if (shopImg != null){//图片空值判断以及添加图片
                        try {
                            addShopImg(shop,shopImg);//添加图片，图片地址会存在shop中
                        }catch (Exception e){
                            throw new ShopException("insert shopImg is error: "+e.getMessage());
                        }
                        //更新店铺信息
                        effectedNum = shopDao.updateShop(shop);
                        if (effectedNum <= 0){
                            throw new ShopException("添加图片地址失败");
                        }
                    }
                }
            }catch (Exception e){
                throw new ShopException("insert shop is error: "+e.getMessage());
            }
            return new ShopExecution(ShopStateEnum.CHECK,shop);
        }
    }

    private void addShopImg(Shop shop, File shopImg) {
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String relativeAddr = ImageUtil.genericThumbnail(shopImg,dest);
        shop.setShopImg(relativeAddr);
    }
}
