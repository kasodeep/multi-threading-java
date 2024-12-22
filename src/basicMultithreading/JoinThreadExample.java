package basicMultithreading;

public class JoinThreadExample {

    public static void main() {
        Thread one = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Thread One: " + i);
            }
        });

        Thread two = new Thread(() -> {
            for (int i = 1; i <= 15; i++) {
                System.out.println("Thread Two: " + i);
            }
        });

        one.start();
        two.start();

        try {
            one.join(); // Main thread will wait for the thread one.
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Done Executing All the Threads.");
    }
}
