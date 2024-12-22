package concurrentCollections;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentCache {

    private static final Map<String, String> cache = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            final int threadNum = (i + 1);

            new Thread(() -> {
                String key = "Key @ " + threadNum;
                for (int j = 0; j < 3; j++) {
                    String value = getCachedValue(key);

                    System.out.println("Thread " + Thread.currentThread().getName() +
                            ": Key=" + key + ", Value=" + value);
                }
            }).start();
        }
    }

    public static String getCachedValue(String key) {
        String value = cache.get(key);

        if (value == null) {
            value = compute(key);
            cache.put(key, value);
        }
        return value;
    }

    private static String compute(String key) {
        System.out.println(key + " not present in the cache, so going to compute!");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Value for " + key;
    }
}
