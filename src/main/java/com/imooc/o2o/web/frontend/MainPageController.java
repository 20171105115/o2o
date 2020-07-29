package com.imooc.o2o.web.frontend;


import com.imooc.o2o.entity.HeadLine;

import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.HeadLineService;

import com.imooc.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/frontend")
public class MainPageController {

    @Autowired
    private HeadLineService headLineService;
    @Autowired
    private ShopCategoryService shopCategoryService;


    @RequestMapping(value = "/listmainpageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listMainPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        List<HeadLine> headLineList = new ArrayList<>();
        try {
            HeadLine headLineCondition = new HeadLine();
            headLineCondition.setEnableStatus(1);
            headLineList = headLineService.getHeadLine(headLineCondition);
            if (headLineList != null){
                modelMap.put("headLineList",headLineList);
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg","头条列表为空");
                return modelMap;
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg","头条获取失败"+e.getMessage());
            return modelMap;
        }
        List<ShopCategory> shopCategoryList = new ArrayList<>();
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(null);
            if (headLineList != null){
                modelMap.put("shopCategoryList",shopCategoryList);
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg","一级分类列表为空");
                return modelMap;
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg","一级分类列表为空"+e.getMessage());
            return modelMap;
        }
        modelMap.put("success",true);
        return modelMap;
    }
}
