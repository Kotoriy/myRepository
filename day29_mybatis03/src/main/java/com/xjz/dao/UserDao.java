package com.xjz.dao;

import com.xjz.domain.User;
import com.xjz.domain.UserAccount;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * @author xiaoxu
 * @date 2019/7/31 14:49
 */
public interface UserDao {
    /**
     * 获取所有user用户信息
     * @return
     */
    @Select("select * from user")
    @Results(id = "userMap", value = {
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "username",column = "username"),
            @Result(property = "birthday",column = "birthday"),
            @Result(property = "sex",column = "sex"),
            @Result(property = "address",column = "address"),
            @Result(property = "accounts", column = "id", many = @Many(
                    select = "com.xjz.dao.AccountDao.getAccountsByUId", fetchType = FetchType.EAGER
            ))
    })
    List<UserAccount> getAllUser();

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    @Select("select * from user where id = #{id}")
    User getUserById(int id);

    /**
     * 插入用户信息
     * @param user
     * @return
     */
    @Insert("insert into user values (null, #{username}, #{birthday}, #{sex}, #{address})")
    int insertUser(User user);

    @Delete("delete from user where id = #{id}")
    int deleteUser(int id);

    @Update("update user set username = #{username}, birthday = #{birthday}, sex = #{sex}, address = #{address} where id = #{id}")
    int updateUser(User user);
}
