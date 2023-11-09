package com.bqliang.generics.pecs

fun main() {
    // ? extends, 限制上界, 协变. 只能读取, 不能写入 -> 生产者. Producer Extends
    val boc1 = BOC()
    val boc2 = BOC()
    val abc  = ABC()

    boc1.deposit(100_000f)
    boc2.deposit(100_000f)
    abc.deposit(500_000f)

    val unionAccounts: ArrayList<ChinaUnionPay> = arrayListOf(boc1, boc2, abc)
    val bocAccounts  : ArrayList<BOC>           = arrayListOf(boc1, boc2)
    val abcAccounts  : ArrayList<ABC>           = arrayListOf(abc)

    val unionTotalBalance = ChinaUnionPay.getTotalBalance(unionAccounts)
    val bocTotalBalance   = ChinaUnionPay.getTotalBalance(bocAccounts)
    val abcTotalBalance   = ChinaUnionPay.getTotalBalance(abcAccounts)
    // 相当于
    val unionCollection : ArrayList<out ChinaUnionPay> = bocAccounts /* ArrayList<BOC> */

    println(
        """
        所有银联账户总余额: $unionTotalBalance
        所有中国银行账户总余额: $bocTotalBalance
        所有农业银行账户总余额: $abcTotalBalance
    """.trimIndent()
    )


    // ? super, 限制下界, 逆变. 只能写入, 不能读取 -> 消费者. Consumer Super
    val icbc = ICBC()
    val unionAccounts2: ArrayList<ChinaUnionPay> = arrayListOf()
    val icbcAccounts  : ArrayList<ICBC>          = arrayListOf()
    icbc.addToList(unionAccounts2)
    icbc.addToList(icbcAccounts)
    // 相当于
    val icbcCollection : ArrayList<in ChinaUnionPay> = unionAccounts2 /* ArrayList<ChinaUnionPay> */


    // * 表示任意类型，相当于 Java 的 ?
    var anyList : ArrayList<*> = arrayListOf<String>()
    anyList = arrayListOf<Int>()
}
