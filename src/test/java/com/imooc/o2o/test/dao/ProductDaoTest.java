package com.imooc.o2o.test.dao;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.ProtectionDomain;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductDaoTest extends BaseTest {

    @Autowired
    private ProductDao productDao;


    @Test
    public void testInsertProduct(){
        //48 39

        Product product = new Product();
        product.setProductName("测试添加");
        product.setProductDesc("测试商品添加");
        product.setPriority(10);
        product.setEnableStatus(0);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(48L);
        Shop shop = new Shop();
        shop.setShopId(39L);

        product.setProductCategory(productCategory);
        product.setShop(shop);
        assertEquals(1,productDao.insertProduct(product));

    }


    @Test
    public void testUpdateProduct(){
        Product product = new Product();
        product.setProductId(42L);
        Shop shop = new Shop();
        shop.setShopId(39L);
        product.setShop(shop);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(33L);
        product.setProductCategory(productCategory);
        product.setProductName("测试DAO的修改");
        product.setPriority(1000);
        product.setProductDesc("测试");
        assertEquals(1,productDao.updateProduct(product));

    }

    @Test
    public void testQueryProductById(){
        Product product = productDao.queryProductById(43L);
        assertEquals("宝马",product.getProductName());
        assertEquals(0,product.getProductImgList().size());


    }

    @Test
    public void testQueryProductListAndCount(){
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(39L);
        productCondition.setShop(shop);

//        productCondition.setProductName("莱");
        assertEquals(6,productDao.queryProductList(productCondition,0,6).size());
//        assertEquals(7,productDao.queryProductListOfCount(productCondition));
        System.out.println(productDao.queryProductListOfCount(productCondition));
    }
}
