package com.imooc.o2o.service.Impl;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageUtil;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductImgDao productImgDao;

    /**
     * 一、处理缩略图，将相对地址存储到product中
     * 二、添加商品信息，获取shopId
     * 三、通过productId来处理详情图
     * 四、将商品详情图存储到数据库中
     *
     * @param product
     * @param thumbnail
     * @param productImgList
     * @return
     */
    @Override
    @Transactional
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) {
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            product.setEnableStatus(1);
            //处理缩略图
            if (thumbnail != null) {//商品缩略图不为空添加
                addThumbnail(product, thumbnail);
            }
            //添加商品信息
            try {
                int effectedNum = productDao.insertProduct(product);
                if (effectedNum <= 0) {
                    throw new RuntimeException("添加商品信息失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("errMsg :" + e.getMessage());
            }
            //处理详情图
            if (productImgList != null && productImgList.size() > 0) {//商品详情图不为空添加
                addProductImgList(product, productImgList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS);
        } else {
            return new ProductExecution(ProductStateEnum.NOT_PRODUCT);
        }
    }

    /**
     * 一、判断product
     * 二、判断缩略图是否传入以及处理
     * 三、判断详情图是否传入以及处理
     * 四、调用DAO处理商品详情以及图片
     *
     * @param product
     * @param thumbnail
     * @param productImgList
     * @return
     */
    @Override
    @Transactional
    public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) {
        if (product != null && product.getProductId() != null && product.getShop() != null && product.getShop().getShopId() != null) {
            product.setLastEditTime(new Date());
            Product productTemp = productDao.queryProductById(product.getProductId());
            //图片不为空
            if (thumbnail != null && thumbnail.getFileName() != null && !thumbnail.getFileName().equals("")) {
                if (productTemp.getImgAddr() != null) {
                    ImageUtil.deleteFileOrPath(productTemp.getImgAddr());
                }
                try {
                    addThumbnail(product, thumbnail);
                } catch (Exception e) {
                    throw new RuntimeException("图片添加失败");
                }
            }
            //处理详情图
            if (productImgList != null && productImgList.size() > 0) {
                if (productTemp.getProductImgList() != null && productTemp.getProductImgList().size() > 0){
                    for (ProductImg productImg : productTemp.getProductImgList()) {
                        ImageUtil.deleteFileOrPath(productImg.getImgAddr());
                    }
                    try {
                        int effectedNum = productImgDao.deleteProductImg(product.getProductId());
                        if (effectedNum <= 0) {
                            throw new RuntimeException("删除详情图失败");
                        }

                    } catch (Exception e) {
                        throw new RuntimeException("添加详情图失败:" + e.getMessage());
                    }
                }
                addProductImgList(product, productImgList);
            }

            try {
                int effectedNum = productDao.updateProduct(product);
                if (effectedNum <= 0) {
                    throw new RuntimeException("更新商品详情失败");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS, product);

            } catch (Exception e) {
                throw new RuntimeException("添加商品详情失败");
            }

        } else {
            return new ProductExecution(ProductStateEnum.NOT_PRODUCT);
        }

    }

    /**
     * 获取商品信息 （单个）
     *
     * @param productId
     * @return
     */
    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductById(productId);
    }

    /**
     * 获取商品列表  带条件的
     * @param productCondition
     * @return
     */
    @Override
    public ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize) {
        int rowIndex = PageUtil.pageToRow(pageIndex,pageSize);
        ProductExecution pe = new ProductExecution();
        pe.setProductList(productDao.queryProductList(productCondition,rowIndex,pageSize));
        pe.setCount(productDao.queryProductListOfCount(productCondition));
        return pe;
    }


    /**
     * 处理详情图
     *
     * @param product
     * @param imageHolderList
     */
    private void addProductImgList(Product product, List<ImageHolder> imageHolderList) {
        String relativePath = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgList = new ArrayList<>();
        for (ImageHolder imageHolder : imageHolderList) {
            ProductImg productImg = new ProductImg();
            String dest = ImageUtil.genericThumbnailOfBigImg(imageHolder, relativePath);
            productImg.setImgAddr(dest);
            productImg.setCreateTime(new Date());
            productImg.setProductId(product.getProductId());

            productImgList.add(productImg);
        }
        if (productImgList.size() > 0) {
            try {
                int effectedNum = productImgDao.batchInsertProductImg(productImgList);
                if (effectedNum <= 0) {
                    throw new RuntimeException("创建详情图失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("创建详情图失败" + e.getMessage());
            }
        }

    }

    /**
     * 处理缩略图
     *
     * @param product
     * @param imageHolder
     */
    private void addThumbnail(Product product, ImageHolder imageHolder) {
        String relativePath = PathUtil.getShopImagePath(product.getShop().getShopId());
        String dest = ImageUtil.genericThumbnail(imageHolder, relativePath);
        product.setImgAddr(dest);
    }


}
