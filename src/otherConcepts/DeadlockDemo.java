package otherConcepts;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * There are three different types of issues:
 * 1. Deadlocks
 * 2. Live-locks.
 * 3. Starvation.
 */
public class DeadlockDemo {

    private final Lock lockA = new ReentrantLock(true);

    private final Lock lockB = new ReentrantLock(true);

    public static void main(String[] args) {
        DeadlockDemo deadlock = new DeadlockDemo();

        new Thread(deadlock::workerOne, "Worker One").start();
        new Thread(deadlock::workerTwo, "Worker Two").start();

        new Thread(() -> {
            // Getting the managed threads by JVM.
            ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();

            while (true) {
                long[] threadIds = mxBean.findDeadlockedThreads();

                if (threadIds != null) {
                    ThreadInfo[] threadInfo = mxBean.getThreadInfo(threadIds);

                    System.out.println("Dead Lock detected!");
                    for (long threadId : threadIds) {
                        System.out.println("Thread with ID " + threadId + " in Dead Lock");
                    }
                    break;
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void workerOne() {
        lockA.lock();
        System.out.println("Worker One acquired lockA");

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        lockB.lock();
        System.out.println("Worker One acquired LockB");
        lockA.lock();
        lockB.unlock();
    }

    public void workerTwo() {
        lockB.lock();
        System.out.println("Worker Two acquired lockB");

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        lockA.lock();
        System.out.println("Worker Two acquired LockA");
        lockB.lock();
        lockA.unlock();
    }
}
