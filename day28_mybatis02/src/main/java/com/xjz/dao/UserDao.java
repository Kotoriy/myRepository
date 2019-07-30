package com.xjz.dao;

import com.xjz.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xiaoxu
 * @date 2019/7/30 16:07
 */
public interface UserDao {
    /**
     * 获取所有用户信息
     * @return
     */
    List<User> getAllUser();

    /**
     * 根据条件获取用户信息
     * @param username
     * @param sex
     * @return
     */
    List<User> getUserByCondition(@Param("username") String username, @Param("sex") String sex);

    /**
     * 批量插入用户信息
     * @param users
     * @return
     */
    int insertUserByList(@Param("users") List<User> users);

    /**
     * 获取所有用户及所有账户信息
     * @return
     */
    List<User> getUserAndAcounts();
}
