package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ProductCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryDao {

    /**
     * 查询该店铺下的商品类别列表
     * @param shopId
     * @return
     */
    List<ProductCategory> queryProductCategoryList(long shopId);

    /**
     * 删除店铺下的商品分类
     * @param productCategoryId
     * @return
     */
    int deleteProductCategory(long productCategoryId);

    /**
     * 批量添加商品分类  （十分重要）
     * @param productCategoryList
     * @return
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);

}
