package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao {
    /**
     * 添加商品
     * @param product
     * @return
     */
    int insertProduct(Product product);

    /**
     * 修改商品信息
     * @param product
     * @return
     */
    int updateProduct(Product product);

    /**
     * 获取商品信息(单个)
     * @param productId
     * @return
     */
    Product queryProductById(long productId);

    /**
     * 获取商品列表   (通过商品模糊名，商品分类、状态、店铺Id等进行查询)
     * @param productCondition
     * @return
     */
    List<Product> queryProductList(@Param("productCondition") Product productCondition
    ,@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 返回列表所对应的商品条数
     * @param productCondition
     * @return
     */
    int queryProductListOfCount(@Param("productCondition") Product productCondition);
}
