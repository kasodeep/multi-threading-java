package otherConcepts;

import java.util.concurrent.atomic.AtomicInteger;

public class Atomicity {

    public static void main(String[] args) throws InterruptedException {
        AtomicCounter atomicCounter = new AtomicCounter();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                atomicCounter.increment();
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);
        Thread thread3 = new Thread(task);

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        System.out.println("Final count (Expected 3000): " + atomicCounter.getCount());
    }
}

class AtomicCounter {

    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.getAndIncrement();
    }

    public int getCount() {
        return count.get();
    }
}

class NonAtomicCounter {

    private int count = 0;

    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}
