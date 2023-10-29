package com.bqliang.thread.sync

object SynchronizedDemo3WithBug {

    private var a = 0
    private var b = 0

    private var isPlaying = true

    @Synchronized
    fun setValues(value: Int) {
        a = value
        b = value
    }

    @Synchronized
    fun setPlaying(isPlaying: Boolean) {
        this.isPlaying = isPlaying
    }

    // TODO 执行 setValues() 时，setPlaying() 也会被阻塞，因为它们都是同一个对象锁
}

object SynchronizedDemo3Solution {

    private var a = 0
    private var b = 0

    private var isPlaying = true

    // TODO 两个锁，互不干扰
    private val lock1 = Any()
    private val lock2 = Any()

    fun setValues(value: Int) {
        synchronized(lock1) {
            a = value
            b = value
        }
    }

    fun setPlaying(isPlaying: Boolean) {
        synchronized(lock2) {
            this.isPlaying = isPlaying
        }
    }
}
