package com.exz.carprofitmuch.bean;

/**
 * Created by pc on 2017/12/1.
 */

public class BalanceBean {

    /**
     * balance : 100
     * limitMoney : 20
     */

    private String balance;
    private String limitMoney;//最低提现额度
    private String minRecharge;//最低充值额度

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(String limitMoney) {
        this.limitMoney = limitMoney;
    }

    public String getMinRecharge() {
        return minRecharge;
    }

    public void setMinRecharge(String minRecharge) {
        this.minRecharge = minRecharge;
    }
}
