package com.bqliang.thread.sync

import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock

object ReentrantLockDemo {

    private var count = 0

    private val reentrantLock = ReentrantLock()

    fun count() {
        reentrantLock.lock()       // TODO 上锁
        try {
            count++
            // some other operations
        } finally {
            reentrantLock.unlock() // TODO 解锁
        }
    }
    // 这种写法相当于 synchronized 代码块
}


object ReentrantReadWriteLockDemo {

    private var count = 0

    private val readWriteLock = ReentrantReadWriteLock()
    private val writeLock = readWriteLock.writeLock()
    private val readLock = readWriteLock.readLock()

    fun count() {
        writeLock.lock()
        try {
            count++
            // some other operations
        } finally {
            writeLock.unlock()
        }
    }

    fun print() {
        readLock.lock() // TODO 操作只涉及读操作，所以使用读锁，所以该方法可以被多个线程同时执行
        try {
            println("count = $count")
        } finally {
            readLock.unlock()
        }
    }
}