package basicMultithreading;

public class VirtualThreadDemo {

    public static void main() {
        final int numberOfThreads = 10_000;

        Runnable objRunnable = () -> {
            String name = "Instagram";
            System.out.println("Fetching data from " + name);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < numberOfThreads; i++) {
            Thread thread = Thread.ofVirtual().unstarted(objRunnable);
            thread.setDaemon(true);
            thread.start();

            // Platform thread typically have large thread stack and utilizes CPU capacity and memory.
            System.out.println("Thread Number: " + i);
        }
    }
}
