package com.imooc.o2o.web.frontend;

import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/frontend")
public class ShopDetailController {


    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductService productService;


    /**
     * 通过shopId获取商品分类、店铺信息、展示在店铺详情页
     *
     * @return
     */
    @RequestMapping(value = "/listshopdetailpageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShopDetailPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        Shop shop = null;
        List<ProductCategory> productCategoryList = null;
        if (shopId != -1L) {
            shop = shopService.getShopById(shopId);
            productCategoryList = productCategoryService.getProductCategoryList(shopId);
            modelMap.put("success",true);
            modelMap.put("shop",shop);
            modelMap.put("productCategoryList",productCategoryList);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "无效的shopId");
        }
        return modelMap;
    }

    /**
     * 通过条件获取商铺下面的商品
     * @param request
     * @return
     */
    @RequestMapping(value = "/listproductbyshop",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listProductByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        if ((shopId > -1) && (pageIndex > -1) && (pageSize > -1)){
            //尝试获取其他的条件
            long productCategoryId = HttpServletRequestUtil.getLong(request,"productCategoryId");
            String productName = HttpServletRequestUtil.getString(request,"productName");
            Product productCondition = getCondition4Search(shopId,productName,productCategoryId);
            ProductExecution pe = productService.getProductList(productCondition,pageIndex,pageSize);
            modelMap.put("success",true);
            modelMap.put("productList",pe.getProductList());
            modelMap.put("count",pe.getCount());
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "无效的shopId、pageIndex、pageSize");
        }
        return modelMap;
    }

    /**
     * 组合条件
     *
     * @param shopId
     * @param productName
     * @param productCategoryId
     * @return
     */
    private Product getCondition4Search(long shopId, String productName, long productCategoryId) {
        Product productCondition = new Product();
        if (shopId != -1L){
            Shop shop = new Shop();
            shop.setShopId(shopId);
            productCondition.setShop(shop);
        }
        if (productName != null){
            productCondition.setProductName(productName);
        }
        if (productCategoryId != -1L){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        productCondition.setEnableStatus(1);
        return productCondition;
    }

}
