package leetcode;

import java.util.concurrent.Semaphore;

public class FooBarAlternate {

    private final int n;
    private final Semaphore fooSemaphore = new Semaphore(1);

    private final Semaphore barSemaphore = new Semaphore(0);

    public FooBarAlternate(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            fooSemaphore.acquire();
            printFoo.run();
            barSemaphore.release();
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            barSemaphore.acquire();
            printBar.run();
            fooSemaphore.release();
        }
    }
}