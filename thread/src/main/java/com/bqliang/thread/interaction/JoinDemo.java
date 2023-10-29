package com.bqliang.thread.interaction;

public class JoinDemo {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            System.out.println("t1");
        });

        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {

        }
        System.out.println("main end");
    }
}
