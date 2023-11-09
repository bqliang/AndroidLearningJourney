package com.bqliang.generics.pecs;

import java.util.List;

/**
 * 工商银行
 */
public class ICBC extends ChinaUnionPay {
    public void addToList(List<? super ICBC> accounts) {
        accounts.add(this);
    }
}
