import dao.UserDao;
import domian.User;
import domian.UserCopy;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class UserDaoTest {
    InputStream inputStream;
    SqlSession sqlSession;
    UserDao userDao;

    @Before
    public void getUserDao() throws IOException {
        // 1.读取配置文件
        inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        // 2.SqlSessionFactoryBuilder 创建factory
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        // 3.获取sqlSession对象
        sqlSession = factory.openSession();
        // 4.使用SqlSession获取dao代理对象
        userDao = sqlSession.getMapper(UserDao.class);
    }
    @After
    public void release() throws IOException {
        // 6.释放资源
        sqlSession.close();
        inputStream.close();
    }

    @Test
    public void queryAllUser(){
        // 5.执行sql获取查询结果
        List<User> users = userDao.queryAllUser();

        for (User user: users) {
            System.out.println(user);
        }
    }

    @Test
    public void queryFuzzyUser(){
        List<User> users = userDao.queryFuzzyUser("王");
        for (User user: users) {
            System.out.println(user);
        }
    }

    @Test
    public void insertUser(){
        User user = new User();
        user.setUsername("小鱿不可爱");
        user.setSex("女");
        user.setBirthday(new Date());
        user.setAddress("深圳");
        System.out.println(user);
        userDao.insertUser(user);
        System.out.println(user);
    }

    @Test
    public void queryAllUserCopy(){
        List<UserCopy> users = userDao.queryAllUserCopy();

        for (UserCopy user: users) {
            System.out.println(user);
        }
    }
}
