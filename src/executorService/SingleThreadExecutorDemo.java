package executorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * It consists of a <b>Task Queue</b> and the thread executes tasks one by one.
 * Fetch task & Execute
 */
public class SingleThreadExecutorDemo {

    public static void main(String[] args) {
        try (ExecutorService executorService = Executors.newSingleThreadExecutor()) {
            for (int i = 1; i <= 5; i++) {
                executorService.submit(new Task(i));
            }
        }
    }
}
