package basicMultithreading;

public class ThreadPriorityExample {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + " says hello...");

        Thread thread = new Thread(() -> System.out.println(Thread.currentThread().getName() + " says hello..."));
        thread.setName("Thread");

        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }
}
