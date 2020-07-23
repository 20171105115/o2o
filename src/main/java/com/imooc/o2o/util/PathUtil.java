package com.imooc.o2o.util;

public class PathUtil {
    private static String separator = System.getProperty("file.separator");

    public static String getImgBasePath(){
        String os = System.getProperty("os.name");
        String basepath= "";
        if (os.toLowerCase().startsWith("win")){
            basepath = "D:/projectdev/image/";
        }else{
            basepath = "/Users/a20171105115/Desktop/image/";
        }
        basepath = basepath.replace("/",separator);
        return basepath;
    }

    public static String getShopImagePath(long shopId){
        String imagePath = "upload/item/shop/" + shopId +"/";
        return imagePath.replace("/",separator);
    }
}
