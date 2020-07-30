package com.imooc.o2o.dao;

import com.imooc.o2o.entity.LocalAuth;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface LocalAuthDao {

    /**
     * 通过账号密码，获取用户信息
     *
     * @param username
     * @param password
     * @return
     */
    LocalAuth queryLocalAuthByUserAndPwd(@Param("username") String username, @Param("password") String password);

    /**
     * 通过用户Id获取用户信息
     * @param userId
     * @return
     */
    LocalAuth queryLocalAuthByUserId(long userId);

    /**
     * 注册本地账号
     * @param localAuth
     * @return
     */
    int insertLocalAuth(LocalAuth localAuth);

    /**
     * 修改密码
     * @param username
     * @param password
     * @param newPassword
     * @param userId
     * @param lastEditTime
     * @return
     */
    int updateLocalAuth(@Param("username") String username, @Param("password") String password,
                        @Param("newPassword") String newPassword, @Param("userId") long userId, @Param("lastEditTime")Date lastEditTime);
}
