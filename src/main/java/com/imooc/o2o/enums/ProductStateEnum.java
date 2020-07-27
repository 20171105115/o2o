package com.imooc.o2o.enums;

public enum  ProductStateEnum {
    SUCCESS(1,"操作成功"),INNER_ERROR(-1001,"内部系统错误"),NOT_PRODUCT(-1002,"没有商品信息"),NOT_IMAGE(-1003,"没有缩略图信息");

    private int state;

    private String stateInfo;

    ProductStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }


    /**
     * 根据状态值返回枚举对象
     *
     * @param state
     * @return
     */
    public static ProductStateEnum stateOf(int state) {
        for (ProductStateEnum stateEnum : values()) {
            if (stateEnum.getState() == state) {
                return stateEnum;
            }
        }
        return null;
    }
}
