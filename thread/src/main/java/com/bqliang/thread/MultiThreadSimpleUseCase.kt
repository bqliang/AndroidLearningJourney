package com.bqliang.thread

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

fun main() {
    thread()
    // runnable()
    // threadFactory()
    // executor()
    // callable()
}

fun thread() {
    val thread = object : Thread() {
        override fun run() {
            println("直接重写 Thread 类的 run 方法")
        }
    }
    thread.start()
}

fun runnable() {
    val runnable = object : Runnable {
        override fun run() {
            println("重写 Runnable 接口的 run 方法")
        }
    }
    val thread = Thread(runnable)
    thread.start()
}

fun threadFactory() {
    val factory = object : ThreadFactory {
        val count = AtomicInteger(0) // int

        override fun newThread(runnable: Runnable): Thread =
            Thread(
                /* target = */ runnable,
                /* name = */ "Thread-${count.incrementAndGet()}" // ++count
            )
    }

    val runnable = Runnable {
        println("Thread ${Thread.currentThread().name} start!")
    }

    val threadA = factory.newThread(runnable)
    val threadB = factory.newThread(runnable)
    threadA.start()
    threadB.start()
}

fun executor() {
    val executor = Executors.newCachedThreadPool()
    val runnable = Runnable { println("Thread with Runnable started!") }
    executor.execute(runnable)
    executor.execute(runnable)
    executor.execute(runnable)
    executor.shutdown()

    val threadPoolExecutor: ThreadPoolExecutor = ThreadPoolExecutor(
        /* corePoolSize = */ 2,
        /* maximumPoolSize = */ 4,
        /* keepAliveTime = */ 3, /* unit = */ TimeUnit.SECONDS,
        /* workQueue = */ SynchronousQueue<Runnable>()
    )
    threadPoolExecutor.execute(runnable)
    threadPoolExecutor.shutdown()
}

fun callable() {
    val callable = object : Callable<String> {
        override fun call(): String {
            Thread.sleep(3000)
            return "Hello from Callable!"
        }
    }

    val executor = Executors.newSingleThreadExecutor()
    val future: Future<String> = executor.submit(callable)
    executor.shutdown()
    val result = future.get() // 阻塞
    println(result)
}
