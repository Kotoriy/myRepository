package com.xjz.test;

import com.xjz.dao.UserDao;
import com.xjz.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xiaoxu
 * @date 2019/7/30 16:22
 */
public class UserDaoTest {
    private InputStream inputStream;
    private UserDao userDao;
    private SqlSession sqlSession;


    @Before
    public void getUserDao() throws IOException {
        // 1.获取配置文件输入流
        inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        // 2.创建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        // 3.获取SqlSession对象
        sqlSession = sqlSessionFactory.openSession();
        // 4.获取dao代理对象
        userDao = sqlSession.getMapper(UserDao.class);
    }

    @After
    public void release() throws IOException {
        // 5.手动提交事务
        sqlSession.commit();
        inputStream.close();
        sqlSession.close();
    }

    @Test
    public void getAllUserTest(){
        List<User> users = userDao.getAllUser();
        for (User user: users) {
            System.out.println(user);
        }
    }

    @Test
    public void getUserByConditionTest(){
        List<User> users = userDao.getUserByCondition("", "男");
        for (User user: users) {
            System.out.println(user);
        }
    }

    @Test
    public void insertUserByListTest(){
        List<User> users = new ArrayList<User>();
        User user = new User("小鱿",new Date(),"女","深圳");
        User user1 = new User("小许",new Date(),"男","深圳");
        User user2 = new User("樱花",new Date(),"男","北京");
        users.add(user);
        users.add(user1);
        users.add(user2);
        int i = userDao.insertUserByList(users);
    }

    @Test
    public void getUserAndAcountsTest(){
        List<User> userAndAcounts = userDao.getUserAndAcounts();
        for (User user: userAndAcounts) {
            System.out.println(user);
        }
    }
}
