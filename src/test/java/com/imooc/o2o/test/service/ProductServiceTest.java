package com.imooc.o2o.test.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductServiceTest extends BaseTest {


    @Autowired
    private ProductService productService;

    @Test
    public void testAddProduct() throws FileNotFoundException {
        Product product = new Product();
        Shop shop = new Shop();
        ProductCategory productCategory = new ProductCategory();
        shop.setShopId(39L);
        productCategory.setProductCategoryId(48L);
        product.setShop(shop);
        product.setProductCategory(productCategory);

        product.setProductName("测试Service");
        product.setPriority(10);

        File file = new File("/Users/a20171105115/Desktop/image/1.jpg");
        FileInputStream is = new FileInputStream(file);
        ImageHolder thumbnail = new ImageHolder(file.getName(),is);


        List<ImageHolder> productImgList = new ArrayList<>();

        File file1 = new File("/Users/a20171105115/Desktop/image/xiaohuangren.jpg");
        FileInputStream is1 = new FileInputStream(file1);
        ImageHolder thumbnail1 = new ImageHolder(file.getName(),is1);
        productImgList.add(thumbnail1);

        File file2 = new File("/Users/a20171105115/Desktop/image/1.jpg");
        FileInputStream is2 = new FileInputStream(file2);
        ImageHolder thumbnail2 = new ImageHolder(file.getName(),is2);
        productImgList.add(thumbnail2);

        ProductExecution pe = productService.addProduct(product,thumbnail,productImgList);
        assertEquals(ProductStateEnum.SUCCESS.getState(),pe.getState());
    }
}
