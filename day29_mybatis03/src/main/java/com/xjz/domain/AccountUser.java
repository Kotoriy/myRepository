package com.xjz.domain;

/**
 * @author xiaoxu
 * @date 2019/7/31 16:47
 */
public class AccountUser extends Account{

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "AccountUser{" +
                super.toString() +
                "user=" + user +
                '}';
    }
}
