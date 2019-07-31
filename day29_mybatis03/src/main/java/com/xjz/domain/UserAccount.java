package com.xjz.domain;

import java.util.List;

/**
 * @author xiaoxu
 * @date 2019/7/31 16:55
 */
public class UserAccount extends User {
    private List<Account> accounts;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        ;
        return "UserAccount{" + super.toString()+
                "accounts=" + accounts +
                '}';
    }
}
