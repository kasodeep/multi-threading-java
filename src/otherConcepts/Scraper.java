package otherConcepts;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

enum ScrapeService {

    INSTANCE;

    private final Semaphore semaphore = new Semaphore(3);

    public void scrape() {
        try {
            semaphore.acquire();
            System.out.println("Available Permits: " + semaphore.availablePermits());

            invokeScrapeBot();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
    }

    private void invokeScrapeBot() {
        try {
            System.out.println("Scraping data...");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class Scraper {

    public static void main(String[] args) {
        try (ExecutorService service = Executors.newCachedThreadPool()) {
            for (int i = 0; i < 15; i++) {
                service.execute(ScrapeService.INSTANCE::scrape);
            }
        }
    }
}
