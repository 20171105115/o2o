package com.imooc.o2o.service;

public interface CacheService {
    /**
     * 通过前缀来删除redis中的数据
     * @param keyPrefix
     */
    void removeFromCache(String keyPrefix);
}
