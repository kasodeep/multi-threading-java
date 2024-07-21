package threadSynchronization;

public class SynchronizationDemo {

    private static int counter = 0;

    public static void main(String[] args) {
        Thread one = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                increment();
            }
        });

        Thread two = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                increment();
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
     * Synchronization behaviour at method level will acquire class level block.
     * There will be concurrency problem and performance bottleneck.
     * */
    private synchronized static void increment() {
        counter++;
    }
}
