package com.imooc.o2o.service.Impl;

import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Override
    public List<ProductCategory> getProductCategoryList(long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }

    @Override
    public int removeProductCategory(long productCategoryId) {
        return productCategoryDao.deleteProductCategory(productCategoryId);
    }

    @Override
    @Transactional
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) {
        if (productCategoryList != null || productCategoryList.size() > 0){
            try {
                int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
                if (effectedNum <= 0){
                    throw new RuntimeException("增加店铺失败");
                }else {
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
    }
}
