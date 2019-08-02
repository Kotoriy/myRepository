package com.xjz.factory;

import com.xjz.service.AccountService;
import com.xjz.service.impl.AccountServiceImpl;

/**
 * @author xiaoxu
 * @date 2019/8/2 17:01
 */
public class InstanceFactory {
    public AccountService getAccountService(){
        return new AccountServiceImpl();
    }
}
