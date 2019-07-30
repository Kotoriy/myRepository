package com.xjz.dao;

import com.xjz.domain.Account;

import java.util.List;

/**
 * @author xiaoxu
 * @date 2019/7/30 20:05
 */
public interface AccountDao {
    List<Account> getAllAccounts();
}
