package locks;

import java.util.concurrent.locks.ReentrantLock;

/**
 * With Reentrant Lock, the tryLock method is unfair.
 * It gives the priority to the thread which calls it immediately instead of waiting threads.
 */
public class ReentrantLockDemo {

    private final ReentrantLock lock = new ReentrantLock();

    private int sharedData = 0;

    public static void main(String[] args) {
        ReentrantLockDemo demo = new ReentrantLockDemo();

        // Create and start multiple threads.
        for (int i = 0; i < 5; i++) {
            new Thread(demo::methodA).start();
        }
    }

    public void methodA() {
        lock.lock();
        try {
            // Critical section.
            sharedData++;
            System.out.println("Method A: sharedData = " + sharedData);

            methodB();
        } finally {
            lock.unlock();
        }
    }

    public void methodB() {
        // Here it looks like we are trying to acquire the lock twice.
        // But in system, the holdCount increases and decreases.
        lock.lock();

        try {
            // Critical section.
            sharedData--;
            System.out.println("Method B: sharedData = " + sharedData);
        } finally {
            lock.unlock();
        }
    }
}
