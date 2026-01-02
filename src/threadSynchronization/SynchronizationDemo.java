package threadSynchronization;

/**
 * Allowing a thread to acquire the same lock more than once enables reentrant synchronization.
 */
public class SynchronizationDemo {

    private static int counter = 0;

    private static final Object lock = new Object();

    public static void main() {
        Thread one = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                incrementSyncBlocks();
            }
        });

        Thread two = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                incrementSyncBlocks();
            }
        });

        one.start();
        two.start();

        try {
            one.join();
            two.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Counter is : " + counter);
    }

    /*
     * Synchronization behavior at method level will acquire class level block.
     * There will be concurrency problem and performance bottleneck.
     * */
    private synchronized static void increment() {
        counter++;
    }

    private static void incrementSyncBlocks() {
        synchronized (lock) {
            counter++;
        }
    }
}
