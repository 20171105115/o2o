package com.imooc.o2o.dto;

import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;

import java.util.List;

public class ShopExecution {
    //结果状态
    private int state;
    //状态标识
    private String stateInfo;
    //店铺数量
    private int count;
    //操作的店铺
    private Shop shop;
    //查询时候店铺的列表
    private List<Shop> shopList;

    public ShopExecution(){

    }

    /**
     * 操作失败时的构造器，只返回状态，不返回shop对象
     * @param stateEnum
     */
    public ShopExecution(ShopStateEnum stateEnum){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 操作成功后的构造器，一并返回shop对象
     * @param stateEnum
     * @param shop
     */
    public ShopExecution(ShopStateEnum stateEnum,Shop shop){
        this.shop = shop;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 操作成功后返回的构造器
     * @param stateEnum
     * @param shopList
     */
    public ShopExecution(ShopStateEnum stateEnum,List<Shop> shopList){
        this.shopList = shopList;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
