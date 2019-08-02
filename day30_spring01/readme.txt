# Spring

## spring概述

​	Spring是分层的JavaEE工程的轻量级框架（只对需要使用的组件进行加载运行，不加载已集成但未使用的组件）

IOC与AOP是spring的核心特点

## Spring的优势

1.方便解耦，简化开发（工厂模式）

2.AOP编程的支持（jdk动态代理、cglib动态代理）

3.测试方便（整合junit）

4.整合其他框架

5.降低javaee api的使用难度

## SpringIOC（控制反转）

### IOC基本实现思路

- 工厂模式+反射

  实现代码

  ```java
  package com.xjz.factory;
  
  import java.io.InputStream;
  import java.util.*;
  
  /**
   * @author xiaoxu
   * @date 2019/8/2 15:22
   */
  public class BeanFatory {
      static Properties properties;
      static Map<String, Object> beanMap;
      // 静态代码块只执行一次
      static{
          // 1.获取bean配置文件输入流
          InputStream inputStream = BeanFatory.class.getClassLoader().getResourceAsStream("bean.properties");
          properties = new Properties();
          beanMap = new HashMap<String, Object>();
          try {
              // 2.读取配置文件
              properties.load(inputStream);
              Enumeration keys = properties.keys();
              while(keys.hasMoreElements()){
                  String key = keys.nextElement().toString();
                  String path = properties.getProperty(key);
                  // 3.反射创建对象实例
                  Object bean = Class.forName(path).newInstance();
                  beanMap.put(key, bean);
              }
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
  
      /**
       * 根据Bean中配置获取对象实例，单例模式
       * @param name
       * @return
       */
      public static Object getBean(String name){
          return beanMap.get(name);
      }
  	/**
  	 * 多例模式创建对象
  	 *
  	 */
      public static Object getBeanPrototype(String name){
          String path = properties.getProperty(name);
          Object obj = null;
          try {
              obj = Class.forName(path).newInstance();
          } catch (Exception e) {
              e.printStackTrace();
          }
          return obj;
      }
  }
  
  ```

  

### 使用IOC实现程序解耦

#### 配置文件入门

导入依赖

- pom.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>
  
      <groupId>com.xjz.spring</groupId>
      <artifactId>day30_spring01</artifactId>
      <version>1.0-SNAPSHOT</version>
      <dependencies>
          <dependency>
              <groupId>org.springframework</groupId>
              <artifactId>spring-context</artifactId>
              <version>5.0.8.RELEASE</version>
          </dependency>
          <dependency>
              <groupId>junit</groupId>
              <artifactId>junit</artifactId>
              <version>4.12</version>
          </dependency>
      </dependencies>
  </project>
  ```

  

#### 实例化Bean的三种方式

beans配置文件

- 构造方法实例化
- 静态工厂实例化
- 实例工厂实例化

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
	<!-- bean的作用范围调整
    bean标签的scope属性：
        作用：用于指定bean的作用范围
        取值： 常用的就是单例的和多例的
            singleton：单例的（默认值）
            prototype：多例的
            request：作用于web应用的请求范围
            session：作用于web应用的会话范围
            global-session：作用于集群环境的会话范围（全局会话范围），当不是集群环境时，它就是session
	-->
    <bean id="accountDao" class="com.xjz.dao.impl.AccountDaoImpl"></bean>
    <bean id="birthday" class="java.util.Date"></bean>

    <!--构造方法实例化bean-->
    <bean id="accountService" class="com.xjz.service.impl.AccountServiceImpl">
        <!--构造方法注入-->
        <!--<constructor-arg name="name" value="小鱿不可爱"></constructor-arg>-->
        <!--<constructor-arg name="age" value="18"></constructor-arg>-->
        <!--<constructor-arg name="birthday" ref="birthday"></constructor-arg>-->

        <!--set方法注入-->
        <property name="name" value="小鱿不可爱"></property>
        <property name="age" value="18"></property>
        <property name="birthday" ref="birthday"></property>
        <property name="list">
            <list>
                <value>小鱿不可爱</value>
                <value>18</value>
                <ref bean="birthday"></ref>
            </list>
        </property>
        <property name="accountDao" ref="accountDao"></property>
    </bean>

    <!--静态工厂实例化bean，第三方提供的类，无法对构造函数进行修改时使用-->
    <!--<bean id="accountService" class="com.xjz.factory.StaticFactory" factory-method="getAccountService"></bean>-->

    <!--实例工厂实例化bean-->
    <!--<bean id="instanceFactory" class="com.xjz.factory.InstanceFactory"></bean>-->
    <!--<bean id="accountService" factory-bean="instanceFactory" factory-method="getAccountService"></bean>-->


</beans>
```



#### Bean作用访问范围设置

- **singleton：默认值，单例的。**

- **prototype：多例的。**

- request：应用在web应用中，将创建的对象存入到request域中。
  session	：应用在web应用中，将创建的对象存入到session域中。

- globalsession：应用在集群环境下使用。将创建的对象存入到全局的session中

  ```xml
  <!-- bean的作用范围调整
      bean标签的scope属性：
          作用：用于指定bean的作用范围
          取值： 常用的就是单例的和多例的
              singleton：单例的（默认值）
              prototype：多例的
              request：作用于web应用的请求范围
              session：作用于web应用的会话范围
              global-session：作用于集群环境的会话范围（全局会话范围），当不是集群环境时，它就是session
  	-->
      <bean id="accountDao" class="com.xjz.dao.impl.AccountDaoImpl"></bean>
  ```

  

#### Bean的生命周期的配置

- 单例对象

  - 出生：当容器创建时对象出生
  - 活着：只要容器还在，对象一直活着
  - 死亡：容器销毁，对象消亡

  ​ ==总结：单例对象的生命周期和容器相同==

- 多例对象

  - 出生：当我们使用对象时spring框架为我们创建
  - 活着：对象只要是在使用过程中就一直活着。
  - 死亡：当对象长时间不用，且没有别的对象引用时，由Java的垃圾回收器回收

​    ==总结：多例对象的生命周期和对象是否被使用有关。与容器是否被销毁无关。==

```xml
<bean id="birthday" class="java.util.Date"></bean>
    <!-- bean对象的生命周期
                单例对象
                    出生：当容器创建时对象出生
                    活着：只要容器还在，对象一直活着
                    死亡：容器销毁，对象消亡
                    总结：单例对象的生命周期和容器相同
                多例对象
                    出生：当我们使用对象时spring框架为我们创建
                    活着：对象只要是在使用过程中就一直活着。
                    死亡：当对象长时间不用，且没有别的对象引用时，由Java的垃圾回收器回收
                    总结：多例对象的声明周期和对象是否被使用有关
                -->

    <!--构造方法实例化bean-->
    <bean id="accountService" class="com.xjz.service.impl.AccountServiceImpl" scope="singleton" init-method="init" destroy-method="destroy">
	</bean>
```

## Spring依赖注入（DI）

依赖注入：
​     能注入的数据：有三类
​        （1）基本类型和String类型（值的注入）
​        （2）其他bean对象类型（在配置文件中或者注解配置过的bean）（对象的注入）
​        （3）复杂类型/集合类型（集合的注入）
​     能注入的方式：有三种
​        （1）第一种：使用构造函数提供
​        （2）第二种：使用set方法提供（使用p名称空间注入）（用的最多）
​        （3）第三种：使用注解提供（明天的内容）

### 构造函数注入

```xml
<!--构造函数注入：
    使用的标签:constructor-arg
    标签出现的位置：bean标签的内部
    标签中的属性
        type：用于指定要注入的数据的数据类型，该数据类型也是构造函数中某个或某些参数的类型
        index：用于指定要注入的数据给构造函数中指定索引位置的参数赋值。索引的位置是从0开始
        name：用于指定给构造函数中指定名称的参数赋值                                        常用的
        =============以上三个用于指定给构造函数中哪个参数赋值===============================
        value：用于提供基本类型和String类型的数据
        ref：用于指定其他的bean类型数据。它指的就是在spring的Ioc核心容器中出现过的bean对象

    优势：
        在获取bean对象时，注入数据是必须的操作，否则对象无法创建成功。
    弊端：
        改变了bean对象的实例化方式，使我们在创建对象时，如果用不到这些数据，也必须提供。
-->
<bean id="accountService" class="com.itheima.service.impl.AccountServiceImpl">
    <constructor-arg name="name" value="泰斯特"></constructor-arg>
    <constructor-arg name="age" value="18"></constructor-arg>
    <constructor-arg name="birthday" ref="now"></constructor-arg>
</bean>

<!-- 配置一个日期对象 -->
<bean id="now" class="java.util.Date"></bean>
```

### set方法注入

- 普通方式注入

  注入需要该属性实现get/set方法编译才能通过

```xml
<!-- set方法注入                更常用的方式
    涉及的标签：property
    出现的位置：bean标签的内部
    标签的属性
        name：用于指定注入时所调用的set方法名称
        value：用于提供基本类型和String类型的数据
        ref：用于指定其他的bean类型数据。它指的就是在spring的Ioc核心容器中出现过的bean对象
    优势：
        1：使用set方法创建对象时没有明确的限制，可以直接使用默认构造函数；
2：使用set方法注入值或者对象，需要哪个属性只需要注入哪个属性
-->
<bean id="accountService2" class="com.itheima.service.impl.AccountServiceImpl2">
    <property name="name" value="小明" ></property>
    <property name="age" value="21"></property>
    <property name="birthday" ref="now"></property>
</bean>

<!-- 配置一个日期对象 -->
<bean id="now" class="java.util.Date"></bean>
```

p名称空间注入数据——本质事set方法

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!--
   使用p空间完成注入
   语法：普通属性    p:属性名=””      对象类型  p:属性名-ref=””
-->
   <bean id="accountService2" class="com.itheima.service.impl.AccountServiceImpl2" p:name="小刚" p:age="25" p:birthday-ref="now"></bean>
   <!-- 配置一个日期对象 -->
   <bean id="now" class="java.util.Date"></bean>
```

- 复杂类型注入

  ```xml
  <!-- 复杂类型的注入/集合类型的注入
      用于给List结构集合注入的标签：
          list array set
      用于个Map结构集合注入的标签:
          map  props
      结构相同，标签可以互换
  -->
      <bean id="accountService3" class="com.itheima.service.impl.AccountServiceImpl3">
      <!--数组-->
      <!--在spring的集合注入中，array，list，set是可以通用的-->
      <property name="arrays">
          <set>
              <value>张三</value>
              <value>22</value>
              <ref bean="date"></ref>
          </set>
      </property>
      <!--list集合-->
      <property name="list">
          <set>
              <value>李四</value>
              <value>20</value>
              <ref bean="date"></ref>
          </set>
      </property>
      <!--set集合-->
      <property name="set">
          <set>
              <value>王五</value>
              <value>25</value>
              <ref bean="date"></ref>
          </set>
      </property>
      <!--map集合-->
      <property name="map">
          <map>
              <entry key="key001">
                  <value>赵六</value>
              </entry>
              <entry key="key002" value="23"></entry>
              <entry key="key003">
                  <ref bean="date"></ref>
              </entry>
          </map>
      </property>
      <!--properties集合，和map集合很相似，也是键值对，键和值只能是String-->
      <!--集合属性的应用场景：初始化系统中使用常量-->
      <property name="properties">
          <props>
              <prop key="driver">com.mysql.jdbc.Driver</prop>
              <prop key="url">jdbc:mysql:///itcastspring</prop>
              <prop key="username">root</prop>
              <prop key="password">root</prop>
          </props>
      </property>
  </bean>
  ```

  