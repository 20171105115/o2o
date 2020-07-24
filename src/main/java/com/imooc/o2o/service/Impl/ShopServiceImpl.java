package com.imooc.o2o.service.Impl;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exception.ShopException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageUtil;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

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
     *
     * @param shop
     * @param fileName
     * @param shopImgInputStream
     * @return
     */
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) {
        if (shop == null) {//空值判断
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        } else {
            try {
                //初始值赋值
                shop.setEnableStatus(0);
                shop.setCreateTime(new Date());
                shop.setLastEditTime(new Date());
                //更新店铺信息,获取主键，之后插入图片
                int effectedNum = shopDao.insertShop(shop);

                if (effectedNum <= 0) {
                    throw new ShopException("添加店铺失败");
                } else {
                    if (shopImgInputStream != null) {//图片空值判断以及添加图片
                        try {
                            addShopImg(shop, shopImgInputStream, fileName);//添加图片，图片地址会存在shop中
                        } catch (Exception e) {
                            throw new ShopException("insert shopImg is error: " + e.getMessage());
                        }
                        //更新店铺信息
                        effectedNum = shopDao.updateShop(shop);
                        if (effectedNum <= 0) {
                            throw new ShopException("添加图片地址失败");
                        }
                    }
                }
            } catch (Exception e) {
                throw new ShopException("insert shop is error: " + e.getMessage());
            }
            return new ShopExecution(ShopStateEnum.CHECK, shop);
        }
    }

    /**
     *修改店铺信息，
     * 1、删除之前的路径（ImageUtil中的方法)
     * 2、判断有没有图片流传入
     * 3、修改信息
     * @param shop
     * @param shopImg
     * @param fileName
     * @return
     */
    @Override
    @Transactional
    public ShopExecution modifyShop(Shop shop, InputStream shopImg, String fileName) {
        if (shop == null || shop.getShopId() == null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }else{
            try{
                //1、判断是否需要修改图片
                if (shopImg != null && fileName != null && !"".equals(fileName)){
                    Shop tempShop = shopDao.queryShopById(shop.getShopId());
                    String relativePath = tempShop.getShopImg();
                    if (relativePath != null){//如果之前的图片地址不为空
                        ImageUtil.deleteFileOrPath(relativePath);
                    }
                    addShopImg(shop,shopImg,fileName);//将新的文件保存到shop里面
                }
                //2、修改店铺信息
                shop.setLastEditTime(new Date());
                int effectedNum = shopDao.updateShop(shop);
                if (effectedNum <= 0){
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                }else {
                    Shop newShop = shopDao.queryShopById(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS,newShop);
                }
            }catch (Exception e){
                throw new ShopException("更新店铺信息失败" + e.getMessage());
            }
        }

    }

    /**
     * 根据店铺Id查询最初的店铺信息，以便于修改
     * @param shopId
     * @return
     */
    @Override
    public Shop getShopById(long shopId) {
        return shopDao.queryShopById(shopId);
    }

    /**
     * 查询店铺列表,分页显示
     * @param shopCondition 判断条件
     * @param pageIndex     页数
     * @param pageSize      每一页的条数
     * @return
     */
    @Override
    public ShopExecution getShopListAndCount(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageUtil.pageToRow(pageIndex,pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition,rowIndex,pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution se = new ShopExecution();
        if (shopList == null){//如果获取失败，则显示内部错误
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }else {
            se.setShopList(shopList);
            se.setCount(count);
        }
        return se;
    }

    /**
     * 添加图片，调用图片处理的方法
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     */
    private void addShopImg(Shop shop, InputStream shopImgInputStream, String fileName) {
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String relativeAddr = ImageUtil.genericThumbnail(shopImgInputStream, fileName, dest);
        shop.setShopImg(relativeAddr);
    }
}
