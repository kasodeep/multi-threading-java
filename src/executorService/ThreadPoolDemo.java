package executorService;

import java.util.concurrent.*;

public class ThreadPoolDemo {

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                4,
                1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                // Executors.defaultThreadFactory
                new ThreadFactory() {
                    static int count = 1;

                    @Override
                    public Thread newThread(Runnable runnable) {
                        return new Thread(runnable, "thread-" + count++);
                    }
                },
                new ThreadPoolExecutor.AbortPolicy()
        );

        // Submit 7 tasks (more than core + queue to test limits).
        for (int i = 1; i <= 7; i++) {
            int taskId = i;
            try {
                executor.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + " running task " + taskId);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                });
            } catch (RejectedExecutionException e) {
                System.out.println("Task " + taskId + " rejected!");
            }
        }

        // Monitor pool stats.
        System.out.println("Active threads: " + executor.getActiveCount());
        System.out.println("Queue size: " + executor.getQueue().size());
        executor.close();
    }
}
