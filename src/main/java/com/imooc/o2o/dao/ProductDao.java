package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDao {
    /**
     * 添加商品
     * @param product
     * @return
     */
    int insertProduct(Product product);
}
