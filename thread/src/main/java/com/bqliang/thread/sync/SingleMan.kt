package com.bqliang.thread.sync

class SingleManWithBug1 private constructor() {

    companion object {
        private var instance: SingleManWithBug1? = null

        fun getInstance(): SingleManWithBug1 {
            if (instance == null) {
                instance = SingleManWithBug1()
            }
            return instance!!
        }

        /**
         * 1. 若两个线程同时调用 [getInstance] 方法，那么就可能会各自创建 SingleManWithBug 对象并返回.
         *   解决办法: 使用 synchronized 关键字修饰 [getInstance] 方法，使得同一时刻只有一个线程可以进入该方法.
         */
    }
}


class SingleManWithLowPerf private constructor() {

    companion object {
        private var instance: SingleManWithLowPerf? = null

        @Synchronized // TODO 使用 [Synchronized] 关键字修饰了 [getInstance] 方法，使得同一时刻只有一个线程可以进入该方法.
        fun getInstance(): SingleManWithLowPerf {
            if (instance == null) {
                instance = SingleManWithLowPerf()
            }
            return instance!!
        }

        /**
         * 虽然解决了 [SingleManWithBug1] 中的问题，但是每次调用 [getInstance] 方法时都需要获取锁，这样会影响性能.
         *  解决办法: 先检查 instance 是否为 null，若为 null 再获取锁创建对象.
         */
    }
}


class SingleManWithBug2 private constructor() {

    companion object {
        private var instance: SingleManWithBug2? = null

        fun getInstance(): SingleManWithBug2 {
            if (instance == null) { // TODO 先检查 instance 是否为 null，若为 null 再获取锁创建对象.
                synchronized(SingleManWithBug2::class) {
                    instance = SingleManWithBug2()
                }
            }
            return instance!!
        }

        /**
         * [synchronized] 关键字保护的只是 instance = SingleManWithBug2() 这一行代码，也就是说，两个线程同时调用 [getInstance] 方法时，
         * 还是有可能同时判断 instance == null 为 true，然后其中一个线程进拿着锁创建对象，另一个线程等待锁释放后再次拿着锁创建对象.
         * 解决办法: 双重检查锁定.
         */
    }
}


class SingleManWithBug3 private constructor() {

    companion object {
        private var instance: SingleManWithBug3? = null

        fun getInstance(): SingleManWithBug3 {
            if (instance == null) {
                synchronized(SingleManWithBug3::class) {
                    if (instance == null) { // TODO 在 synchronized 关键字保护的代码块中再次检查 instance 是否为 null, 确保不会创建多个对象.
                        instance = SingleManWithBug3()
                    }
                }
            }
            return instance!!
        }

        /**
         * Java 创建对象是个比较复杂的过程，instance = SingleManWithBug3() 这一行代码可能会被编译成多条指令
         * 我们期待的执行顺序是:
         * 1. 分配内存空间
         * 2. 初始化对象
         * 3. 将 instance 指向分配的内存空间
         *
         * 但是由于指令重排序的存在，可能会被编译成如下顺序:
         * 1. 分配内存空间
         * 2. 将 instance 指向分配的内存空间
         * 3. 初始化对象
         *
         * 这样就会导致一个线程在执行完 1 和 2 后，instance 指向分配的内存空间（instance != null），但是对象还没有初始化
         * 此时另一个线程就进来了, instance != null 为 true，就会直接返回 instance，但是 instance 还没有初始化，
         * 就会导致空指针异常.
         *
         * 解决办法: 使用 volatile 关键字修饰 instance.
         */
    }
}


class SingleMan private constructor() {

    companion object {
        @Volatile
        private var instance: SingleMan? = null

        fun getInstance(): SingleMan {
            if (instance == null) {
                synchronized(SingleMan::class) {
                    if (instance == null) { // TODO 在 synchronized 关键字保护的代码块中再次检查 instance 是否为 null, 确保不会创建多个对象.
                        instance = SingleMan()
                    }
                }
            }
            return instance!!
        }

        /**
         * Java 创建对象是个比较复杂的过程，instance = SingleManWithBug3() 这一行代码可能会被编译成多条指令
         * 我们期待的执行顺序是:
         * 1. 分配内存空间
         * 2. 初始化对象
         * 3. 将 instance 指向分配的内存空间
         *
         * 但是由于指令重排序的存在，可能会被编译成如下顺序:
         * 1. 分配内存空间
         * 2. 将 instance 指向分配的内存空间
         * 3. 初始化对象
         *
         * 这样就会导致一个线程在执行完 1 和 2 后，instance 指向分配的内存空间（instance != null），但是对象还没有初始化
         * 此时另一个线程就进来了, instance != null 为 true，就会直接返回 instance，但是 instance 还没有初始化，
         * 就会导致空指针异常.
         *
         * 解决办法: 使用 volatile 关键字修饰 instance.
         */
    }
}
