package com.bqliang.thread.sync

import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

object SynchronizedDemo2WithBug {

    private var x = 0

    private fun count() {
        x++
    }

    @JvmStatic
    fun main(args: Array<String>) {
        thread {
            repeat(1_000_000) {
                count()
            }
            println("x = $x")
        }

        thread {
            repeat(1_000_000) {
                count()
            }
            println("x = $x")
        }
        // TODO 最后打印的结果不是 2_000_000 ???
    }
}

object SynchronizedDemo2Solution1 {

    private var x = 0

    @Synchronized // TODO 使用 synchronized 关键字修饰方法，方法执行期间保证同步性(可见性)和原子性
    private fun count() {
        x++
    }

    @JvmStatic
    fun main(args: Array<String>) {
        thread {
            repeat(1_000_000) {
                count()
            }
            println("x = $x")
        }

        thread {
            repeat(1_000_000) {
                count()
            }
            println("x = $x")
        }
    }
}

object SynchronizedDemo2Solution2 {

    private var x = AtomicInteger(0) // TODO 使用原子类 AtomicInteger 保证操作的原子性

    private fun count() {
        x.incrementAndGet()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        thread {
            repeat(1_000_000) {
                count()
            }
            println("x = ${x.get()}")
        }

        thread {
            repeat(1_000_000) {
                count()
            }
            println("x = ${x.get()}")
        }
    }
}
