---
typora-copy-images-to: img
---

# Mybatis

## 配置文件

### 配置文件顺序

```xml
SqlMapConfig.xml中配置的内容和顺序如下： 

properties（属性） 

settings（全局配置参数） 

typeAliases（类型别名） 

typeHandlers（类型处理器） 

objectFactory（对象工厂） 

plugins（插件） 

environments（环境集合属性对象） 

environment（环境子属性对象） 

transactionManager（事务管理） 

dataSource（数据源）

mappers（映射器）
```

- 配置<dataSource>

  - 使用properties文件配置数据库连接

  在mybatis数据源配置文件使用<properties>标签进行配置

  ![1564485358115](.\img\1564485358115.png)

- 使用typeAliases标签创建别名

  ==mybatis本身支持java某些基本数据类型和常用类的别名（详见官方文档）==

  - <typeAlias>标签——指定单个实体类

    ![1564481352105](.\img\1564481352105.png)

  - <package>标签——指定某个包下的所有实体

    ![1564481432960](.\img\1564481432960.png)

- 加载映射文件<mappers>

  - mapper 的resource属性

    从资源文件中读取内容，指定映射文件

    ![1564485425133](.\img\1564485425133.png)

  - mapper 的class属性

    从类文件中读取内容，指定类（==必须保证Dao的类和映射文件放置到同一个报下，而且名称要一致==）应用场景一般在注解中

  - package标签

    将包内的映射器接口实现全部注册为映射器(==必须保证Dao类和映射文件放置在同一个包下，名称要一致==)

    ![1564485494865](.\img\1564485494865.png)

### 小结（mybatis-config.xml）

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<!--mybatis的核心配置文件-->
<configuration>
    <!--properties加载外部的属性文件
        resource：从当前项目加载（读取项目资源路径下的文件），使用${}读取属性文件中的内容
        url：从外部的资源加载文件（需要指定外部资源的绝对路径）
    -->
    <properties resource="jdbc.properties">
    <!--使用url属性时需要注意使用文件协议才能读取到配置文件-->
    <!--<properties url="file:///E:\workspace\day28_mybatis02\src\main\resources\jdbc.properties">-->
    </properties>

    <!--配置别名：不区分大小写
        typeAlias：一次指定一个实体
        package：指定包下的所有实体都可以使用别名，别名的名称就是当前实体的类名
    -->
    <typeAliases>
        <!--<typeAlias type="com.xjz.domain.User" alias="user"/>-->
        <package name="com.xjz.domain"/>
    </typeAliases>

    <!--1:配置连接数据库的环境
        environments default="mysql"：名称随便填写
        environment id="mysql"：id表示当前环境的唯一标识
        transactionManager type="JDBC"：事务管理器，JDBC是固定要求
        dataSource type="POOLED"：数据源的连接池，POOLED固定写法，表示使用连接池
                底层：Opening JDBC Connection
                      Created connection 14348096.
                      Closing JDBC Connection
                      Returned connection 14348096 to pool.

        dataSource type="UNPOOLED"：数据源的连接池，UNPOOLED固定写法，表示不使用连接池
                底层：Opening JDBC Connection
                      Closing JDBC Connection
    -->
    <environments default="mysql">
        <environment id="mysql">
            <transactionManager type="JDBC"></transactionManager>
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>

    <!--2：加载映射文件
        mapper resource：从资源文件中读取内容，指定映射文件
        mapper class：从类文件中读取内容，指定类（必须保证Dao的类和映射文件放置到同一个包下，而且名称要一致），应用场景一般在注解的使用
        package name="com.itheima.dao"：指定com.itheima.dao包下的所有Dao类都会被加载映射（必须保证Dao的类和映射文件放置到同一个包下，而且名称要一致），，应用场景一般在映射文件、注解的使用
    -->
    <mappers>
        <!--<mapper resource="com/xjz/dao/UserDao.xml"/>-->
        <!--<mapper class="com.xjz.dao.UserDao"></mapper>-->
        <package name="com.xjz.dao"/>
    </mappers>
</configuration>
```

## 连接池和事务

### 连接池

### 连接池的特点

1.连接池就是用于存储连接的一个容器

2.连接池其实就是一个集合对象，该集合必须线程安全，保证程序不能让两个线程拿到同一个连接

3.连接池必须实现队列的特性，先进先出

### 连接池的分类

- unpooled

  不适用连接池的数据源，采用传统的获取连接的方式，虽然也实现Javax.sql.DataSource，但是并没有使用池的思想 ==底层：UnpooledDataSource.java==

- ==pooled==

  使用连接池的数据源，采用传统的javax.sql.DataSource规范中的连接池，mybatis中有针对规范的实现。==底层实现：PooledDataSource.java==

- JNDI

  使用JNDI实现的数据源（不了解），采用服务器提供的JNDI技术实现，来获取DataSource对象，不同的服务器所能拿到DataSource是不一样。

  ​			 注意：如果不是web或者maven的war工程，是不能使用的。

  ​			 我们课程中使用的是tomcat服务器，采用连接池就是dbcp连接池。

### 事务

- ACID
  - A：原子性
  - C：一致性
  - I：  隔离性
  - D：持久性

事务隔离级别

- 不考虑事务隔离级别会出现的问题

  - 脏读
  - 不可重复度
  - 幻读/虚读

- 事务隔离级别分类

  - Read - uncommitted
  - Read - committed
  - repeatable Read
  - serializable

  ==mybatis默认手动回滚==

## mybatis映射文件动态查询

### if

if标签中的test属性，其中有条件逻辑关系使用 and or 等关键字

![1564487529668](.\img\1564487529668.png)

### choose

- when

- otherwise

  选择其中条件之一执行

### trim

- where

- set

### foreach

![1564487870320](.\img\1564487870320.png)

## Mybatis多表关联查询

###  一对多

- 使用resultMap高级映射

  使用collection标签，注意使用==ofType属性==值指定下级关联类

  ![1564491432852](.\img\1564491432852.png)

### 一对一

- 使用resultMap高级映射

  使用association标签，property代表加载对象，javaType代表加载对象的数据类型

```xml
<!--定义resultMap对象，用来封装账号信息-->
<resultMap id="accountMap" type="account">
    <id property="id" column="aid"></id>
    <result property="uid" column="uid"></result>
    <result property="money" column="money"></result>
    <!--association用来关联对象，property代表加载对象，javaType代表加载对象的数据类型，可以写成com.itheima.domain.User-->
    <association property="user" javaType="user">
        <id property="id" column="id"></id>
        <result property="username" column="username"></result>
        <result property="birthday" column="birthday"></result>
        <result property="sex" column="sex"></result>
        <result property="address" column="address"></result>
    </association>

</resultMap>
<!-- 查询所有（方案二：直接用Account对象封装） -->
<select id="findByAccountUser2" resultMap="accountMap">
    select u.*,a.id as aid,a.uid,a.money from account a,user u where u.id = a.uid
</select>
```

### 多对多关系查询

