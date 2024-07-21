package executorService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolDemo {

    public static void main(String[] args) {
        try (ScheduledExecutorService service = Executors.newScheduledThreadPool(1)) {
            service.scheduleAtFixedRate(new ProbeTask(), 1000, 2000, TimeUnit.MILLISECONDS);

            try {
                if (!service.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                    service.shutdown();
                }
            } catch (InterruptedException e) {
                service.shutdown();
            }
        }
    }
}

class ProbeTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Probing end point for updates...");
    }
}
