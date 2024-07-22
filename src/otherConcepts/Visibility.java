package otherConcepts;

/**
 * Visibility Problem.
 * Without volatile the threads keep a copy of the variables and modify there.
 * When we specify the volatile variable, the threads read and write directly to main memory.
 */
public class Visibility {

    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource();

        // Thread1.
        new Thread(() -> {
            System.out.println("Thread 1 initiating");
            try {
                Thread.sleep(1000);
                System.out.println("Thread 1 completed");
                sharedResource.setFlag(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // Thread2.
        new Thread(() -> {
            System.out.println("Thread 2 initiating");
            while (!sharedResource.isFlag()) ; // Wait till flag is set.
            System.out.println("Thread 2 completed");
        }).start();
    }

    static class SharedResource {

        private volatile boolean flag = false;

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }
}

