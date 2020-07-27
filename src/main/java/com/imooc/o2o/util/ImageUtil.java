package com.imooc.o2o.util;

import com.imooc.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {

    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddHHmmss");
    private static Random random = new Random();
    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 将CommonsMultipartFile转化成File，因为Spring用于接收File的是multipartFile接口
     * 没用上，最后是将CommonsMultipartFile的IO流传入了
     * @param cFile
     * @return
     */
    public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile){
        File newFile = new File(cFile.getOriginalFilename());
        try {
            cFile.transferTo(newFile);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return newFile;
    }

    /**
     * 处理缩略图并且返回图片的相对子路径
     * @param thumbnail
     * @param targetAddr 要存入的路径
     * @return
     */
    public static String genericThumbnail(ImageHolder thumbnail, String targetAddr){
        String realFileName = getRandomFileName();//取随机文件名
        String extension = getFileExtension(thumbnail.getFileName());//获取文件后缀
        makeDirPath(targetAddr);//创建目标路径
        String relativeAddr = targetAddr + realFileName + extension;//相对路径
        logger.debug("current relativeAddr is:" + relativeAddr);
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);//绝对路径
        logger.debug("current compltet addr is :" + dest.toString());
        try {
            Thumbnails.of(thumbnail.getFilePath()).size(200,200)
                    .watermark(Positions.BOTTOM_LEFT, ImageIO.read(new File(basePath + "/watermark.jpg")),0.8f)
                    .outputQuality(0.8f).toFile(dest);//存储图片
        }catch (IOException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 创建目标文件夹,实际路径
     *
     * @param targetAddr
     * @return
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()){
            dirPath.mkdirs();//创建路径上的所有文件夹
        }
    }

    /**
     * 获取输入文件名的扩展名
     * @param fileName
     * @return
     */
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成随机文件名
     * @return
     */
    private static String getRandomFileName() {
        int rannum = random.nextInt(89999) + 10000;
        String nowTime = simple.format(new Date());
        return nowTime+rannum;

    }

    public static void deleteFileOrPath(String filePath){
        File fileOrPath = new File(PathUtil.getImgBasePath() + filePath);
        if (fileOrPath.exists()){
            if (fileOrPath.isDirectory()){
                File[] files = fileOrPath.listFiles();//如果是文件，则找出下面所有的文件
                for (int i=0 ;i < files.length ; i++){
                    files[i].delete();
                }
            }
            fileOrPath.delete();
        }
    }


    /**
     * 处理详情图，因为大小不一样
     * @param thumbnail
     * @param targetAddr
     * @return
     */
    public static String genericThumbnailOfBigImg(ImageHolder thumbnail, String targetAddr){
        String realFileName = getRandomFileName();//取随机文件名
        String extension = getFileExtension(thumbnail.getFileName());//获取文件后缀
        makeDirPath(targetAddr);//创建目标路径
        String relativeAddr = targetAddr + realFileName + extension;//相对路径
        logger.debug("current relativeAddr is:" + relativeAddr);
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);//绝对路径
        logger.debug("current img compltet addr is :" + dest.toString());
        try {
            Thumbnails.of(thumbnail.getFilePath()).size(337,640)
                    .watermark(Positions.BOTTOM_LEFT, ImageIO.read(new File(basePath + "/watermark.jpg")),0.9f)
                    .outputQuality(0.8f).toFile(dest);//存储图片
        }catch (IOException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return relativeAddr;
    }


}
