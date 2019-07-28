package dao;

import domian.User;
import domian.UserCopy;

import java.util.List;

/**
 * 用户dao层
 */
public interface UserDao {
    /**
     * 查询所有用户
     * @return
     */
    List<User> queryAllUser();

    /**
     * 根据传入用户名进行模糊查询
     * @param name 用户名
     * @return
     */
    List<User> queryFuzzyUser(String name);

    /**
     * 新增数据
     * @param user 用户实体
     */
    void insertUser(User user);

    /**
     * 查询所有用户,处理实体类与数据库字段不对应问题（使用resultMap）
     * @return
     */
    List<UserCopy> queryAllUserCopy();

}
