package com.imooc.o2o.util;

public class PageUtil {

    /**
     * 通过页数和条数计算出数据库分页时所需要的行数
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static int pageToRow(int pageIndex, int pageSize) {
        //如果是第一页，则返回0行开始
        //如果是第二页，则返回pageIndex行
        return (pageIndex > 0) ? ((pageIndex - 1) * pageSize) : 0;
    }
}
