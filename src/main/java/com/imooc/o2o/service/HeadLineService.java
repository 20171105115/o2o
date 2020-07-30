package com.imooc.o2o.service;

import com.imooc.o2o.entity.HeadLine;

import java.util.List;

public interface HeadLineService {

    public static final String HEADLINEKEY = "headlindlist";

    /**
     * 获取头条列表(通过条件）
     * @param headLineCondition
     * @return
     */
    List<HeadLine> getHeadLine(HeadLine headLineCondition);
}
