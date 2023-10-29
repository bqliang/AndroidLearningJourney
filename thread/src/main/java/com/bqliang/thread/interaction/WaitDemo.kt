package com.bqliang.thread.interaction

import kotlin.concurrent.thread

object WaitDemoWithBug1 {
    private var name: String? = null

    @Synchronized
    private fun initName() {
        name = "ABC"
    }

    @Synchronized
    private fun printName() {
        println("name is $name")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        thread {
            Thread.sleep(2000) // 模拟耗时操作, 2s 后初始化 name
            initName()
        }

        thread {
            Thread.sleep(1000) // 模拟耗时操作, 1s 后打印 name
            printName()
        }
    }

    // TODO 1s 后打印 name, 但是 name 在 2s 后才初始化
}


object WaitDemoWithBug2 {
    private var name: String? = null

    @Synchronized
    private fun initName() {
        name = "ABC"
    }

    @Synchronized
    private fun printName() {
        while (name == null) { // TODO 在 printName 里面不断轮询 name 是否初始化

        }
        println("name is $name")
    }

    /**
     * 因为 [initName] 和 [printName] 都被 [Synchronized] 关键字修饰,
     * [printName] 比 [initName] 先执行，然后想等待 [initName] 执行完再打印,
     * 殊不知它已经拿了锁, [printName] 在等 [initName] 执行完毕, 而 [initName] 又在等 [printName] 执行完毕,
     * 两个线程互相等待, 造成死锁.
     *
     * 解决办法: 让 [printString] 方法能在不满足继续往下运行条件时能够先把锁先释放，等到满足条件了再重新拿锁继续执行
     */

    @JvmStatic
    fun main(args: Array<String>) {
        thread {
            Thread.sleep(2000) // 模拟耗时操作, 2s 后初始化 name
            initName()
        }

        thread {
            Thread.sleep(1000) // 模拟耗时操作, 1s 后打印 name
            printName()
        }
    }
}


object WaitDemoSolution : Object() {
    private var name: String? = null

    @Synchronized
    private fun initName() {
        name = "ABC"
        // notify() 只能唤醒一个等待的线程, 如果有多个线程在等待, 只有一个线程能被唤醒
        notifyAll() // TODO 使用 notifyAll() 唤醒所有等待的线程, 注意不是直接把锁给它们, 被唤醒的线程还是要去竞争锁
    }

    @Synchronized
    private fun printName() {
        while (name == null) {
            try {
                wait() // TODO 不满足条件, 使用 wait() 释放锁, 等待被唤醒
                // 注意 wait() 是 Object 的方法, 而不是 Thread 的方法
                // 因为 wait() 是释放锁, 而锁是 Object 的属性
            } catch (e: InterruptedException) {

            }
        }
        println("name is $name")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        thread {
            Thread.sleep(2000) // 模拟耗时操作, 2s 后初始化 name
            initName()
        }

        thread {
            Thread.sleep(1000) // 模拟耗时操作, 1s 后打印 name
            printName()
        }
    }
}
