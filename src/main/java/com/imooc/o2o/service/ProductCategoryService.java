package com.imooc.o2o.service;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    /**
     * 通过店铺Id查询下面的商品分类
     * @param shopId
     * @return
     */
    List<ProductCategory> getProductCategoryList(long shopId);

    /**
     * 删除指定的商品分类
     * @param productCategoryId
     * @return
     */
    ProductCategoryExecution removeProductCategory(long productCategoryId,long shopId);

    /**
     * 批量增加商品分类
     * @param productCategoryList
     * @return
     */
    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList);
}
