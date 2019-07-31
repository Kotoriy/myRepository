import com.xjz.dao.AccountDao;
import com.xjz.domain.Account;
import com.xjz.domain.AccountUser;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author xiaoxu
 * @date 2019/7/31 14:51
 */
public class AcountDaoTest {
    private InputStream inputStream;
    private AccountDao accountDao;
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
        accountDao = sqlSession.getMapper(AccountDao.class);
    }

    @After
    public void release() throws IOException {
        // 5.手动提交事务
        sqlSession.commit();
        inputStream.close();
        sqlSession.close();
    }

    @Test
    public void getAllAccountUserTest(){
        List<AccountUser> accountUsers = accountDao.getAllAccountUser();
        for (AccountUser a: accountUsers
             ) {
            System.out.println(a);
        }
    }

    @Test
    public void getAllAccountUserTest2(){
        List<AccountUser> accountUsers = accountDao.getAllAccountUser();
        for (AccountUser a: accountUsers
                ) {
            System.out.println(a);
        }
    }

    @Test
    public void getAccountByUid(){
        List<Account> accounts = accountDao.getAccountsByUId(46);
        for (Account a: accounts
                ) {
            System.out.println(a);
        }
    }
}
