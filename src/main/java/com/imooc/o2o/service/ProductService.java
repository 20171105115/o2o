package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;

import java.util.List;

public interface ProductService {

    /**
     * 处理商品信息 添加商品信息 处理缩略图 处理详情图
     *
     * @param product
     * @param thumbnail
     * @param productImgList
     * @return
     */
    ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList);

    /**
     * 修改商品信息
     *
     * @param product
     * @param thumbnail
     * @param productImgList
     * @return
     */
    ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList);

    /**
     * 获取商品初始值，便于商品编辑
     * @param productId
     * @return
     */
    Product getProductById(long productId);


    /**
     * 获取商品列表 通过商品条件
     * @param productCondition
     * @return
     */
    ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize);

}
