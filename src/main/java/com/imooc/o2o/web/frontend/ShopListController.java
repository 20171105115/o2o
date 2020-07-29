package com.imooc.o2o.web.frontend;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;
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
public class ShopListController {

    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private AreaService areaService;

    /**
     * 通过是否传入parentId（一级分类的Id）显示对应的商品列表
     * 如果是点击全部商品，则parentId为空，会显示所有一级类别，之后点击不同的类别，显示的是该一级分类下所有二级分类的店铺
     * 如果是点击某个一级分类，则会显示该一级分类下的所有二级分类
     * @param request
     * @return
     */
    @RequestMapping(value = "/listshopspageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShopsPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //所有的一级分类都是parentId
        long parentId = HttpServletRequestUtil.getLong(request, "parentId");
        List<ShopCategory> shopCategoryList = null;
        if (parentId != -1L){
            //获取二级商铺分类
            ShopCategory shopCategory = new ShopCategory();
            ShopCategory parent = new ShopCategory();
            parent.setShopCategoryId(parentId);
            shopCategory.setParent(parent);
            shopCategoryList = shopCategoryService.getShopCategoryList(shopCategory);
        }else {
            //没有parentId说明就获取一级商品分类
            shopCategoryList = shopCategoryService.getShopCategoryList(null);
        }
        modelMap.put("shopCategoryList",shopCategoryList);

        //获取区域分类
        List<Area> areaList = areaService.getAreaList();
        modelMap.put("areaList",areaList);
        if (shopCategoryList != null && areaList!=null){
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","商品分类列表Or区域列表为空");
        }
        return modelMap;
    }

    /**
     * 根据传入的条件 （模糊名 区域 店铺分类 查询）
     * 在这里 传入的分类只能是一级或者二级 同时传递的的情况根本没有
     * @param request
     * @return
     */
    @RequestMapping(value = "/listshops",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShops(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        if (pageIndex > -1 && pageSize > -1){
            //parentId  点击全部商品后，出现的小分类是一级分类，所以是parentId
            long parentId = HttpServletRequestUtil.getLong(request,"parentId");
            //shopCategoryId 点击某个大类后，出现的分类是二级分类，所以是这个
            long shopCategoryId = HttpServletRequestUtil.getLong(request,"shopCategoryId");
            //areaId
            int areaId = HttpServletRequestUtil.getInt(request,"areaId");
            //shopName
            String shopName = HttpServletRequestUtil.getString(request,"shopName");
            Shop shopCondition = get4Condition(parentId,shopCategoryId,areaId,shopName);
            ShopExecution se = shopService.getShopListAndCount(shopCondition,pageIndex,pageSize);
            modelMap.put("success",true);
            modelMap.put("shopList",se.getShopList());
            modelMap.put("count",se.getCount());
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","没有传入页码");
        }
        return modelMap;
    }

    /**
     * 根据传入的条件 组合一个筛选对象
     *
     * @param parentId
     * @param shopCategoryId
     * @param areaId
     * @param shopName
     * @return
     */
    private Shop get4Condition(long parentId, long shopCategoryId, int areaId, String shopName) {
        Shop shopCondition = new Shop();
        if (parentId != -1L) {
            ShopCategory shopCategory = new ShopCategory();
            ShopCategory parent = new ShopCategory();
            parent.setShopCategoryId(parentId);
            shopCategory.setParent(parent);
            shopCondition.setShopCategory(shopCategory);
        }
        if (shopCategoryId != -1L){
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if (areaId > -1){
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }
        if (shopName != null){
            shopCondition.setShopName(shopName);
        }
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }
}
