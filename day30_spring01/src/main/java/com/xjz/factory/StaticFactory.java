package com.xjz.factory;

import com.xjz.service.AccountService;
import com.xjz.service.impl.AccountServiceImpl;

/**
 * @author xiaoxu
 * @date 2019/8/2 16:42
 */
public class StaticFactory {
    public static AccountService getAccountService(){
        return new AccountServiceImpl();
    }
}
