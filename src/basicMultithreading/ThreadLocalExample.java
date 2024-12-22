package basicMultithreading;

public class ThreadLocalExample {

    public static void main() {
        ThreadLocal<Long> userThreadLocal = new ThreadLocal<>();
        Long userId = 1234L;

        Thread requestThread = new Thread(() -> {
            userThreadLocal.set(userId);
            System.out.println("RequestThread - Adding userId to LocalThread: " + userId);

            Long value = userThreadLocal.get();
            System.out.println("RequestThread - Retrieved userId from LocalThread: " + value);

            // Good Practice.
            userThreadLocal.remove();
        });
        requestThread.start();

        // Parent and Child thread storage.
        InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
        Thread outerThread = new Thread(() -> {
            inheritableThreadLocal.set("Instagram");

            // Child thread.
            Thread innerThread = new Thread(() -> System.out.println(inheritableThreadLocal.get()));
            innerThread.start();
        });
        outerThread.start();
    }
}
