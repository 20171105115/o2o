package com.imooc.o2o.web.superadmin;

import com.imooc.o2o.entity.Area;
import com.imooc.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/superadmin")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/arealist",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getAreaList(){
        Map<String,Object> modelMap = new HashMap<>();
        List<Area> areaList = new ArrayList<>();
        try {
            areaList = areaService.getAreaList();
            modelMap.put("success",true);
            modelMap.put("rows",areaList);
            modelMap.put("total",areaList.size());
        }catch (Exception e){
            e.printStackTrace();
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }
}
