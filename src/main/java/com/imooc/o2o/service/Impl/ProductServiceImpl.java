package com.imooc.o2o.service.Impl;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductImgDao productImgDao;

    /**
     * 一、处理缩略图，将相对地址存储到product中
     * 二、添加商品信息，获取shopId
     * 三、通过productId来处理详情图
     * 四、将商品详情图存储到数据库中
     *
     * @param product
     * @param imageHolder
     * @param imageHolderList
     * @return
     */
    @Override
    @Transactional
    public ProductExecution addProduct(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolderList) {
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            product.setEnableStatus(1);
            //处理缩略图
            if (imageHolder != null) {//商品缩略图不为空添加
                addImageHolder(product, imageHolder);
            }
            //添加商品信息
            try {
                int effectedNum = productDao.insertProduct(product);
                if (effectedNum <= 0) {
                    throw new RuntimeException("添加商品信息失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("errMsg :" + e.getMessage());
            }
            //处理详情图
            if (imageHolderList != null && imageHolderList.size() > 0){//商品详情图不为空添加
                addProductImgList(product,imageHolderList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS);
        } else {
            return new ProductExecution(ProductStateEnum.NOT_PRODUCT);
        }
    }

    /**
     * 处理详情图
     * @param product
     * @param imageHolderList
     */
    private void addProductImgList(Product product, List<ImageHolder> imageHolderList) {
        String relativePath = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgList = new ArrayList<>();
        for (ImageHolder imageHolder : imageHolderList){
            ProductImg productImg = new ProductImg();
            String dest = ImageUtil.genericThumbnailOfBigImg(imageHolder, relativePath);
            productImg.setImgAddr(dest);
            productImg.setCreateTime(new Date());
            productImg.setProductId(product.getProductId());

            productImgList.add(productImg);
        }
        if (productImgList.size() > 0){
            try {
                int effectedNum = productImgDao.batchInsertProductImg(productImgList);
                if (effectedNum <= 0){
                    throw new RuntimeException("创建详情图失败");
                }
            }catch (Exception e){
                throw new RuntimeException("创建详情图失败"+e.getMessage());
            }
        }

    }

    /**
     * 处理缩略图
     * @param product
     * @param imageHolder
     */
    private void addImageHolder(Product product, ImageHolder imageHolder) {
        String relativePath = PathUtil.getShopImagePath(product.getShop().getShopId());
        String dest = ImageUtil.genericThumbnail(imageHolder,relativePath);
        product.setImgAddr(dest);
    }


}
