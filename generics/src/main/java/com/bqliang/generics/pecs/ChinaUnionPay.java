package com.bqliang.generics.pecs;

import java.util.List;

/**
 * 中国银联
 */
public abstract class ChinaUnionPay {

    protected float balance;

    /**
     * 获取银联账户余额
     */
    public float getTotalBalance() {
        return balance;
    }

    /**
     * 存钱
     */
    public void deposit(float amount) {
        balance += amount;
    }

    /**
     * 取钱
     */
    public void withdraw(float amount) {
        balance -= amount;
    }

    /**
     * 计算所有银联账户余额
     */
    public static float getTotalBalance(List<? extends ChinaUnionPay> accounts) {
        float allBalance = 0;
        for (ChinaUnionPay account : accounts) {
            allBalance += account.getTotalBalance();
        }
        return allBalance;
    }
}
