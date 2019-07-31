import com.xjz.dao.UserDao;
import com.xjz.domain.User;
import com.xjz.domain.UserAccount;
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

/**
 * @author xiaoxu
 * @date 2019/7/31 14:51
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
        List<UserAccount> users = userDao.getAllUser();
        for (UserAccount user: users){
            System.out.println(user);
        }
    }

    @Test
    public void getUserByIdTest(){
        User user = userDao.getUserById(41);
    }

    @Test
    public void insertUserTest(){
        User user = new User("小王", new Date(), "女" ,"深圳");
        userDao.insertUser(user);
    }
    @Test
    public void deleteUserTest(){
        userDao.deleteUser(57);
    }

    @Test
    public void updateUserTest(){
        User user = new User("小王小廖", new Date(), "女" ,"深圳");
        user.setId(56);
        userDao.updateUser(user);
    }
}
