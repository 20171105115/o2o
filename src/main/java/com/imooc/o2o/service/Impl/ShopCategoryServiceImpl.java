package com.imooc.o2o.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.cache.JedisUtil;
import com.imooc.o2o.dao.ShopCategoryDao;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;



    @Override
    @Transactional
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        String key = SHOPCATEGORYKEY;
        List<ShopCategory> shopCategoryList = null;
        ObjectMapper mapper = new ObjectMapper();
        //列出所有一级分类
        if (shopCategoryCondition == null){
            key = key + "_allfirstlevel";
        }else if (shopCategoryCondition != null && shopCategoryCondition.getParent()!=null
        && shopCategoryCondition.getParent().getShopCategoryId()!=null){
            //查一个一级分类下的所有二级分类
            key = key + "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
        }else if (shopCategoryCondition != null){
            //列出所有的子类别
            key = key + "_allsecondlevel";
        }

        if (!jedisKeys.exists(key)){
            shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
            String jsonString;
            try {
                jsonString = mapper.writeValueAsString(shopCategoryList);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("转换字符串失败"+e.getMessage());
            }
            jedisStrings.set(key,jsonString);
        }else {
            String jsonString = jedisStrings.get(key);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class,ShopCategory.class);
            try {
                shopCategoryList = mapper.readValue(jsonString,javaType);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("转换字符串失败"+e.getMessage());
            }
        }
        return shopCategoryList;

    }
}
