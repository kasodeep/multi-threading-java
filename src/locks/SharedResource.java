package locks;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SharedResource {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private int counter = 0;

    public void increment() {
        lock.writeLock().lock();
        try {
            counter++;
            System.out.println(Thread.currentThread().getName() + " writes: " + counter);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void getValue() {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " reads: " + counter);
        } finally {
            lock.readLock().unlock();
        }
    }
}

class ReadWriteLockDemo {

    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource();

        // Create multiple reader threads.
        for (int i = 0; i < 2; i++) {
            Thread readerThread = new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    sharedResource.getValue();
                }
            }, "Reader Thread " + (i + 1));
            readerThread.start();
        }

        // Create a writer thread.
        Thread writerThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                sharedResource.increment();
            }
        }, "Writer Thread");
        writerThread.start();
    }
}
