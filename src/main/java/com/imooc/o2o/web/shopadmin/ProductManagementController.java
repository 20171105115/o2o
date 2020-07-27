package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/shopadmin")
public class ProductManagementController {

    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductService productService;


    private static final  int MAXIMAGECOUNT = 6;

    /**
     * 删除商品类别
     * @param productCategoryId
     * @param request
     * @return
     */
    @RequestMapping(value = "/removeproductcategory",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> removeProductCategory(Long productCategoryId, HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (productCategoryId != null && productCategoryId >0){
            try{
                ProductCategoryExecution pe = productCategoryService.removeProductCategory(productCategoryId,currentShop.getShopId());
                if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                //抛出异常时不会执行到最下面，只能手动return
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请传入正确的分类信息");
        }
        return modelMap;
    }

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

    /**
     * 添加商品
     * 一、验证码校验
     * 二、JSON字符串转化
     * 三、缩略图接收并校验并操作
     * 四、详情图接收并校验并操作
     * 五、调用service层方法
     * @param request
     * @return
     */
    @RequestMapping(value = "/addproduct", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addProduct(HttpServletRequest request) {
        Map<String,Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入验证码");
            return modelMap;
        }

        String productStr = HttpServletRequestUtil.getString(request,"productStr");
        //接收参数
        ObjectMapper mapper = new ObjectMapper();
        Product product;
        try {
            product = mapper.readValue(productStr,Product.class);
        } catch (JsonProcessingException e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        //获取文件
        ImageHolder thumbnail;
        List<ImageHolder> productImgList;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
            try {
                //取出文件流并且存放在文件封装类中(详情图)
                CommonsMultipartFile multipartFile = (CommonsMultipartFile)multipartRequest.getFile("thumbnail");
                thumbnail = new ImageHolder(multipartFile.getOriginalFilename(), multipartFile.getInputStream());
                //取出文件流存放在List中
                productImgList =  new ArrayList<>();
                for (int i=0;i<MAXIMAGECOUNT;i++){
                    CommonsMultipartFile productImgFile = (CommonsMultipartFile)multipartRequest.getFile("productImg"+i);
                    if (productImgFile != null){//若不为空，则取出来
                        ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(), productImgFile.getInputStream());
                        productImgList.add(productImg);
                    }else {//为空就跳出，说明已经循环完了
                        break;
                    }
                }
            } catch (Exception e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","店铺图片不能为空");
            return modelMap;
        }
        //处理参数

        try {
            Shop shop = (Shop)request.getSession().getAttribute("currentShop");
            product.setShop(shop);
            ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
            if (pe.getState() == ProductStateEnum.SUCCESS.getState()){
                modelMap.put("success",true);
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg","添加失败");
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg","添加失败");
            return modelMap;
        }

        //返回响应
        return modelMap;
    }
}
