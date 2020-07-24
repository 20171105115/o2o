package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

@RequestMapping(value = "/shopadmin")
@Controller
public class ShopManagementController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    /**
     * 每次从店铺列表进入店铺操作页面时，都要将店铺信息存储到Session中
     * 为了修改或者是其他操作时，可以获取到shop的信息，比如ShopId
     * @param request
     * @return
     */
    @RequestMapping(value = "/getshopmanagementinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopManagementInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        if (shopId <= 0){//如果没传递ShopId,则获取session中的shop对象
            Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
            if (currentShop == null){
                //如果连session中的信息都为空，则说明非法进入，跳转回店铺列表
                modelMap.put("redirect",true);
                modelMap.put("url","/o2o/shopadmin/shoplist");
            }else {
                //不为空则将shopId传递过去
                modelMap.put("redirect",false);
                modelMap.put("shopId",currentShop.getShopId());
            }
        }else {
            //如果shopId不为空，则将其用一个currentShop封装在一起，存储到session中，直到点击到下一次的店铺进行操作
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop", currentShop);
            modelMap.put("redirect",false);
//            modelMap.put("shopId",currentShop.getShopId());
        }
        return modelMap;
    }

    /**
     * 获取店铺列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/getshoplist",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopList(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //TODO 之后要通过Session获取用户信息来显示其所创的店铺
        PersonInfo owner = new PersonInfo();
        owner.setUserId(10L);
        owner.setName("张三");
        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(owner);
            ShopExecution se = shopService.getShopListAndCount(shopCondition,0,100);
            modelMap.put("success",true);
            modelMap.put("shopList",se.getShopList());
            modelMap.put("user",owner);//在店铺列表上方显示 XXX，欢迎您
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    /**
     * 通过ShopId获取店铺信息，显示在修改页面
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getshopbyid",method = RequestMethod.GET)
    private Map<String, Object> getShopById(HttpServletRequest request) {
        Map<String,Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1) {
            Shop shop = shopService.getShopById(shopId);
            List<Area> areaList = areaService.getAreaList();
            modelMap.put("success",true);
            modelMap.put("shop",shop);
            modelMap.put("areaList",areaList);
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shopId");
        }
        return modelMap;

    }

    /**
     * 获取注册店铺时的初始信息，比如区域信息，商铺类别信息等
     * @return
     */
    @RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopInitInfo(){
        Map<String,Object> modelMap = new HashMap<>();
        try {
            List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            List<Area> areaList = areaService.getAreaList();
            if (shopCategoryList != null && areaList != null){
                modelMap.put("success",true);
                modelMap.put("areaList",areaList);
                modelMap.put("shopCategoryList",shopCategoryList);
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg","没有任何信息");
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }



    /**
     * 通过接受图片以及JSON字符串来创建账号
     * @param request
     * @return
     */
    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg", "您输入的验证码有误，请重新输入");
            return modelMap;
        }
        //1、接收参数
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (JsonMappingException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            e.printStackTrace();
            return modelMap;
        } catch (JsonProcessingException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            e.printStackTrace();
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext()
        );
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "图片上传不能为空");
            return modelMap;
        }
        //2、注册店铺
        if (shop != null && shopImg != null) {
            PersonInfo owner = (PersonInfo)request.getSession().getAttribute("user");
//            owner.setUserId(1L);
            shop.setOwner(owner);
            ShopExecution se;
            try {
                se = shopService.addShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                if (se.getState() == ShopStateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                    //该用户可以操作的商铺列表,每个店铺创建时都要将其放在session中，以便操作
                    @SuppressWarnings("unchecked")
                    List<Shop> shopList = (List<Shop>)request.getSession().getAttribute("shopList");
                    if (shopList == null || shopList.size() == 0){
                        shopList = new ArrayList<>();
                    }
                    shopList.add(shop);
                    request.getSession().setAttribute("shopList",shopList);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                e.printStackTrace();
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }

    }


    /**
     * 修改店铺信息，总体流程和注册差不多，接收参数，之后修改信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg", "您输入的验证码有误");
            return modelMap;
        }
        //1、接收参数
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (JsonMappingException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            e.printStackTrace();
            return modelMap;
        } catch (JsonProcessingException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            e.printStackTrace();
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext()
        );
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }
        //2、注册店铺
        if (shop != null && shop.getShopId() != null) {
            PersonInfo owner = new PersonInfo();
            //Session TODO
            owner.setUserId(1L);
            shop.setOwner(owner);
            ShopExecution se;
            try {
                if (shopImg == null){
                    se = shopService.modifyShop(shop,null,null);
                }else{
                    se = shopService.modifyShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                }

                if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                e.printStackTrace();
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }

    }
}
