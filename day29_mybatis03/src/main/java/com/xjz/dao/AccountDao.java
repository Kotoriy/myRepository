package com.xjz.dao;

import com.xjz.domain.Account;
import com.xjz.domain.AccountUser;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * @author xiaoxu
 * @date 2019/7/31 14:51
 */
public interface AccountDao {
    /**
     * 根据用户编号获取所有的用
     * @param uid
     * @return
     */
    @Select("select * from account where uid = #{uid}")
    List<Account> getAccountsByUId(int uid);

    /**
     * 获取所有的账户信息级账户所属用户信息
     * @return
     */
    @Select("select * from account")
    @Results(id = "accountMap", value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "uid", column = "uid"),
            @Result(property = "money", column = "money"),
            @Result(property = "user", column = "uid" , one = @One(
                    select = "com.xjz.dao.UserDao.getUserById", fetchType = FetchType.EAGER
            ))
    })
    List<AccountUser> getAllAccountUser();
    @Select("select u.*,a.id AS aid,a.uid,a.money FROM account a,USER u WHERE a.uid = u.id")
    @Results(id = "ResultMap", value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "birthday", column = "birthday"),
            @Result(property = "sex", column = "sex"),
            @Result(property = "address", column = "address")
    })
    List<AccountUser> getAllAccountUser2();
}
