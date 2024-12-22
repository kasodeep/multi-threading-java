package basicMultithreading;

public class DaemonThreadDemo {

    public static void main() {
        Thread bgThread = new Thread(new DaemonHelper());
        Thread userThread = new Thread(new UserThread());

        bgThread.setDaemon(true);
        bgThread.start();
        userThread.start();
    }
}

class DaemonHelper implements Runnable {
    @Override
    public void run() {
        int count = 0;

        while (count < 500) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;
            System.out.println("Daemon helper running...");
        }
    }
}

class UserThread implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(5000); // Causes the thread to sleep.

            boolean interrupted = Thread.interrupted();
            System.out.println("User thread interrupted: " + interrupted);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("User Thread Helper done with execution!");
    }
}
