package executorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Task Queue can contain one task at max.
 * If all the threads are busy a new thread is created.
 * If the thread is idled for more than 60 seconds, it is killed.
 */
public class CachedThreadPoolDemo {

    public static void main(String[] args) {
        try (ExecutorService executorService = Executors.newCachedThreadPool()) {
            for (int i = 1; i <= 10000; i++) {
                executorService.execute(new Task(i));
            }
        }
    }
}
