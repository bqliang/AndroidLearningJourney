package com.bqliang.thread.interaction;

public class StopDemo {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) {
                System.out.println(i);
            }
        });
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        thread.stop(); // 不推荐使用
    }
}
