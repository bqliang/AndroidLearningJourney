package com.bqliang.thread.sync

import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

object SynchronizedDemo1WithBug {
    private var running = true

    private fun stop() {
        running = false
    }

    @JvmStatic
    fun main(args: Array<String>) {
        thread {
            while (running) {
            }
        }

        Thread.sleep(1000)
        stop() // TODO stop 无法停止线程???
    }
}


object SynchronizedDemo1Solution1 {
    @Volatile // TODO 使用 volatile 关键字保证同步性(可见性)
    private var running = true

    private fun stop() {
        running = false
    }

    @JvmStatic
    fun main(args: Array<String>) {
        thread {
            while (running) {
            }
        }

        Thread.sleep(1000)
        stop()
    }
}

object SynchronizedDemo1Solution2 {
    // TODO 使用 Atomic 类保证同步性(可见性)和原子性
    private var running = AtomicBoolean(true)

    private fun stop() {
        running.set(false)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        thread {
            while (running.get()) {
            }
        }

        Thread.sleep(1000)
        stop()
    }
}
