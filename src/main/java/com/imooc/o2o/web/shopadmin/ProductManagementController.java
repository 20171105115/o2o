package com.imooc.o2o.web.shopadmin;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/shopadmin")
public class ProductManagementController {

    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 获取商品类别列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getProductCategoryList(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //从session中获取shopId
        Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
        long shopId = currentShop.getShopId();
        if (shopId <= 0){
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shop");
        }else {
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(shopId);
            modelMap.put("success",true);
            modelMap.put("productCategoryList",productCategoryList);
        }
        return modelMap;
    }


    /**
     * 批量增加商品分类
     * @param productCategoryList
     * @param request
     * @return
     */
    @RequestMapping(value = "/addproductcategorys",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //判断传入的List是否为空
        if (productCategoryList != null && productCategoryList.size() > 0){
            for (ProductCategory list : productCategoryList){
                list.setShopId(currentShop.getShopId());
            }
            //批量插入 凡是涉及到增删改的都需要增加try-catch
            try {
                ProductCategoryExecution pe = productCategoryService.batchAddProductCategory(productCategoryList);
                //如果成功了之后
                if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                } else {
                    modelMap.put("success",false);
                    modelMap.put("ErrMsg",pe.getStateInfo());
                }
            }catch (RuntimeException e){
                modelMap.put("success",false);
                modelMap.put("ErrMsg",e.getMessage());
                throw new RuntimeException("操作失败");
            }

        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请至少输入一个新的分类");
        }
        return modelMap;
    }
}
