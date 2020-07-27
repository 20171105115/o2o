package com.imooc.o2o.test.dao;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductImgDaoTest extends BaseTest {

    @Autowired
    private ProductImgDao productImgDao;

    @Test
    public void testBatchInsertProductImg(){

        long productId = 39L;

        List<ProductImg> productImgList = new ArrayList<>();

        ProductImg productImg1 = new ProductImg();
        productImg1.setImgAddr("滚");
        productImg1.setProductId(productId);
        productImg1.setCreateTime(new Date());

        ProductImg productImg2 = new ProductImg();
        productImg2.setImgAddr("滚");
        productImg2.setProductId(productId);
        productImg2.setCreateTime(new Date());

        productImgList.add(productImg1);
        productImgList.add(productImg2);

        assertEquals(2,productImgDao.batchInsertProductImg(productImgList));
    }

}
