# Mybatis

## mybatis延迟加载策略

### 概述

就是再需要用到数据时才进行加载，不需要用到数据时就不加载数据，延迟加载也成为懒加载

### 延迟加载实例

延迟加载配置

```xml
<settings>
     <!--打开延迟加载的开关-->
     <setting name="lazyLoadingEnabled" value="true"/>
     <!--将立即加载改为按需加载，即延迟加载-->
     <setting name="aggressiveLazyLoading" value="false"/>
</settings>
```



#### 多对一的延迟加载（association）

- AccountDao.xml(dao映射文件)

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.xjz.dao.AccountDao">
      <select id="getAccountsByUId" resultType="account">
          select * from account where uid = #{uid}
      </select>
  
      <resultMap id="accountMap" type="AccountUser">
          <id column="id" property="id"/>
          <result column="uid" property="uid"/>
          <result column="money" property="money"/>
          <!--association用来关联对象，
              property代表加载对象，
              javaType代表加载对象的数据类型，可以写成com.itheima.domain.User
              select 属性指定的内容：查询用户的唯一标识，指延迟加载要执行的statement的id
                                      要使用UserDao.xml中的findById完成根据用户id查询用户信息
              column 属性指定的内容：用户根据id查询时，所需要的参数的值
              fetchType:
                  eager：立即检索
                  lazy：延迟检索-->
          <association property="user" javaType="user" column="uid" select="com.xjz.dao.UserDao.getUserById"/>
      </resultMap>
      <select id="getAllAccountUser" resultMap="accountMap">
          select * from account
      </select>
  </mapper>
  ```

  

- AccountDao.java(dao层)

  ```java
  package com.xjz.dao;
  
  import com.xjz.domain.Account;
  import com.xjz.domain.AccountUser;
  
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
      List<Account> getAccountsByUId(int uid);
  
      /**
       * 获取所有的账户信息级账户所属用户信息
       * @return
       */
      List<AccountUser> getAllAccountUser();
  }
  ```

  

- AccountDaoTest.java(测试代码)

  ```java
  import com.xjz.dao.AccountDao;
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
  }
  ```

- 运行结果及分析

  - 未配置延时加载

    直接查询完所有数据，包含子集中的数据

    ![1564565523649](.\img\1564565523649.png)

  - 配置延时加载设置

    未使用数据是，未查询子集数据

    ![1564565628707](.\img\1564565628707.png)

    使用数据时，再进行子集数据查询

    ![1564565736097](.\img\1564565736097.png)

#### 一对多的延迟加载（collection）

- UserDao.xml（dao映射文件）

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.xjz.dao.UserDao">
      <resultMap id="UserMap" type="UserAccount">
          <id column="id" property="id"/>
          <result column="username" property="username"/>
          <result column="birthday" property="birthday"/>
          <result column="sex" property="sex"/>
          <result column="address" property="address"/>
          <collection property="accounts" ofType="account" select="com.xjz.dao.AccountDao.getAccountsByUId" column="id">
              <id column="aid" property="id"/>
              <result column="uid" property="uid"/>
              <result column="money" property="money"/>
          </collection>
      </resultMap>
      <select id="getAllUser" resultMap="UserMap">
          select * from user
      </select>
  </mapper>
  ```

  

- UserDao.java（dao层）

  ```java
  package com.xjz.dao;
  
  import com.xjz.domain.UserAccount;
  
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
      List<UserAccount> getAllUser();
  }
  ```

  

- UserDaoTest.java(测试代码)

  ```java
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
  }
  ```

- 运行结果及分析
  - 未配置延时加载

  ![1564559444118](.\img\1564559444118.png)

  ==上图显示，断点处未使用数据，mybatis日志详情显示子集数据已查询完成==

  执行完断点，查看日志如下图，未再次进行查询

  ![1564559849957](.\img\1564559849957.png)

  - 配置延时加载设置

  如下图未进行子集数据查询

  ![1564559980210](.\img\1564559980210.png)

  进入循环打印数据

  ![1564560132640](.\img\1564560132640.png)

### 延时加载实例总结

1.先在配置mybatis配置文件中进行全局延时加载配置

```xml
<settings>
    <!--打开延迟加载的开关-->
    <setting name="lazyLoadingEnabled" value="true"/>
    <!--将立即加载改为按需加载，即延迟加载-->
    <setting name="aggressiveLazyLoading" value="false"/>
</settings>
```

2.可以通过association标签或collection标签的fetchType属性进行设置，优先级高于全局延时加载配置

```xml
<resultMap id="accountMap" type="AccountUser">
        <id column="id" property="id"/>
        <result column="uid" property="uid"/>
        <result column="money" property="money"/>
        <!--association用来关联对象，
            property代表加载对象，
            javaType代表加载对象的数据类型，可以写成com.itheima.domain.User
            select 属性指定的内容：查询用户的唯一标识，指延迟加载要执行的statement的id
                                    要使用UserDao.xml中的findById完成根据用户id查询用户信息
            column 属性指定的内容：用户根据id查询时，所需要的参数的值
            fetchType:
                eager：立即检索
                lazy：延迟检索-->
        <association property="user" javaType="user" column="uid" select="com.xjz.dao.UserDao.getUserById" fetchType="eager"/>
    </resultMap>
    <select id="getAllAccountUser" resultMap="accountMap">
        select * from account
    </select>
```

3.延迟检索会对系统查询数据库做性能优化：

==开发过程主要遵循以下几点== ：

==1.可以使用联合查询语句，完成多表的查询（用的比较多，联合查询的sql语句不会出现立即检索和延迟检索）==

==2.可以使用select的单独查询（通过主外键），完成多表的查询（考虑立即检索和延迟检索）==

## mybatis缓存

### 一级缓存

mybatis默认使用一级缓存

####  一级缓存的特点

- 当调用sqlsession修改、添加、删除、commit（）、close（）等方法，就会清除一级缓存
- 当sqlsession（更新、修改、删除）提交，都需要刷新一级缓存，避免脏读
- 一级缓存存储的时对象，所以在一级缓存存在时，再次发生相同检索，返回的是同一对象地址
- 一级缓存是数据SqlSession对象的，根据SqlSession的特定操作进行创建和销毁

### 二级缓存

使用二级缓存需要配置，配置以下三步

- 配置二级缓存
  - 在mybatis配置文件中配置二级缓存

    ```xml
    <!--需要配置延迟加载策略-->
    <settings>
        <!--开启二级缓存-->
        <setting name="cacheEnabled" value="true"/>
    </settings>
    ```

  - 在映射文件中配置二级缓存

    ```xml
    <mapper namespace="com.itheima.dao.UserDao">
        <cache/>
    </mapper>
    ```

  - 在指定映射文件中的sql指定二级缓存开关

    ```xml
    <select id="getAllAccountUser" resultMap="accountMap" useCache="true">
        select * from account
    </select>
    
    ```

#### 二级缓存的特点

- 二级缓存中存放的是对象的散装数据，再次查询的时候需要对序列化的实体进行反序列化，而反序列化时是创建新的实体对象
- 二级缓存的作用范围是SqlSessionFactory
- ==二级缓存存在一定缺陷，相关配置下，不同mapper会创建不同的二级缓存对象，在不同进程对相关缓存进行修改，会导致脏读问题==                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             

## Mybatis注解开发

==使用注解开发需要注意不能存在同名的mapper映射文件==

### 常用注解

==@Insert:实现新增== 

==@Update:实现更新== 

==@Delete:实现删除==

==@Select:实现查询==

==@Result:实现结果集封装==

==@Results:可以与@Result一起使用，封装多个结果集==

==@One:实现一对一结果集封装== 

==@Many:实现一对多结果集封装== 

@SelectProvider: 实现动态SQL映射 

### 注解练习相关代码

- UserDao.java

  ```java
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
  ```

  

- UserDaoTest.java

  ```java
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
  ```

  

- AccountDao.java

  ```java
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
  
  ```

  

- AccountDaoTest.java

  ```java
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
  ```

  