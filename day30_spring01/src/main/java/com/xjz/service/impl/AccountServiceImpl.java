package com.xjz.service.impl;

import com.xjz.dao.AccountDao;
import com.xjz.factory.BeanFatory;
import com.xjz.service.AccountService;

import java.util.Date;
import java.util.List;

/**
 * @author xiaoxu
 * @date 2019/8/2 15:19
 */
public class AccountServiceImpl implements AccountService{

    /**
     * 基本类型及应用类型依赖注入
     */
    private String name;
    private Integer age;
    private Date birthday;

    /**
     * 集合类型注入,list ,数组,set集合 的注入方式通用
     */
    private List<Object> list;

    private AccountDao accountDao;

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    //构造方法注入
//    public AccountServiceImpl(String name, Integer age, Date birthday) {
//        this.name = name;
//        this.age = age;
//        this.birthday = birthday;
//    }

    public AccountServiceImpl(){
        System.out.println("AccountServiceImpl构造方法执行了");
    }


    public void getAccount() {
        accountDao = (AccountDao) BeanFatory.getBean("accountDao");
        accountDao.getAccount();
        System.out.println("我是Service实现类");
    }

    public void getAccount2() {
        accountDao.getAccount();
        System.out.println("我是Service实现类");
    }

    public void getUser(){
        System.out.println("Service实现类的getUser方法");
        System.out.println("name:" + this.name + "  ,age:" + this.age + "   ,birthday:" + this.birthday + "list:" + this.list);
    }

    public void  init(){
        System.out.println("对象初始化了。。。");
    }
    public void  destroy(){
        System.out.println("对象销毁了。。。");
    }
}
