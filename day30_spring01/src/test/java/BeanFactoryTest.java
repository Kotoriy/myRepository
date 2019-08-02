import com.xjz.factory.BeanFatory;
import com.xjz.service.AccountService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author xiaoxu
 * @date 2019/8/2 15:43
 */
public class BeanFactoryTest {
    @Test
    public void beanFactoryTest(){
        AccountService accountService = (AccountService) BeanFatory.getBean("accountService");
        accountService.getAccount();
    }

    @Test
    public void beanFactoryPrototypeTest(){
        AccountService accountService1 = (AccountService) BeanFatory.getBeanPrototype("accountService");
        AccountService accountService2= (AccountService) BeanFatory.getBeanPrototype("accountService");
        AccountService accountService3 = (AccountService) BeanFatory.getBeanPrototype("accountService");
        System.out.println(accountService1);
        System.out.println(accountService2);
        System.out.println(accountService3);
    }

    @Test
    public void springIOCTest(){
        //根据bean的资源文件配置不同实现三种实例化bean
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("application.xml");
        AccountService accountService = (AccountService) applicationContext.getBean("accountService");
        accountService.getUser();
    }

    @Test
    public void springDITest(){
        //根据bean的资源文件配置不同实现三种依赖注入
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("application.xml");
        AccountService accountService = (AccountService) applicationContext.getBean("accountService");
        accountService.getUser();
        accountService.getAccount2();
    }

}
