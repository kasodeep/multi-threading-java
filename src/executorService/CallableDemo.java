package executorService;

import java.util.concurrent.*;

public class CallableDemo {

    public static void main(String[] args) {
        int number = 10;

        // Calculating the sum of N natural numbers.
        Callable<Integer> sumTask = () -> {
            int sum = 0;
            for (int i = 1; i <= number; i++) sum += i;
            return sum;
        };

        // Submit the task to the thread.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(sumTask);

        try {
            // get() is a blocking operation, and it blocks the threads until result is returned.
            int result = future.get();

            System.out.println("The sum is: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
        executor.close();
    }
}
