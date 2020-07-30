package com.imooc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/frontend", method = RequestMethod.GET)
@Controller
public class FrontendController {

    @RequestMapping(value = "/index")
    public String index(){
        return "frontend/index";
    }

    @RequestMapping(value = "/shoplist")
    public String shopList(){
        return "frontend/shoplist";
    }

    @RequestMapping(value = "/shopdetail")
    public String shopDetail(){
        return "frontend/shopdetail";
    }

    @RequestMapping(value = "/productdetail")
    public String productDetail(){
        return "frontend/productdetail";
    }
}
