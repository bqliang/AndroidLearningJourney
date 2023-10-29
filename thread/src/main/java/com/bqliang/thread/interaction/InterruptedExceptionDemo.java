package com.bqliang.thread.interaction;

import java.io.File;

public class InterruptedExceptionDemo {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            // create some new files
            File[] files = new File[100];
            // ...

            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                // some cleanup work like delete the files that were created at the beginning
                for (File file : files) {
                    if (file != null && file.exists()) {
                        file.delete();
                    }
                }
                return; // end the thread
            }

            // process the files that were created at the beginning
            for (File file : files) {
                // ...
            }
        });

        thread.start();

        try {
            Thread.sleep(3_000);
        } catch (InterruptedException e) {
        }
        thread.interrupt();
    }
}

