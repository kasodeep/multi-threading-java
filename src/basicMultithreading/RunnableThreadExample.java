package basicMultithreading;

public class RunnableThreadExample {

    public static void main(String[] args) {
        Thread threadOne = new Thread(new ThreadOne());
        Thread threadTwo = new Thread(new ThreadTwo());

        Thread threadThree = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                System.out.println("Thread Three: " + i);
            }
        });

        threadOne.start();
        threadTwo.start();
        threadThree.start();
    }
}

class ThreadOne implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Thread One: " + i);
        }
    }
}

class ThreadTwo implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Thread Two: " + i);
        }
    }
}
