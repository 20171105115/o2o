package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;

import java.util.List;

public interface ProductService {

    /**
     * 处理商品信息 添加商品信息 处理缩略图 处理详情图
     * @param product
     * @param imageHolder
     * @param imageHolderList
     * @return
     */
    ProductExecution addProduct(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolderList);
}
