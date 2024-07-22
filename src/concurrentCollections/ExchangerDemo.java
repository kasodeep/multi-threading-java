package concurrentCollections;

import java.util.concurrent.Exchanger;

public class ExchangerDemo {

    public static void main(String[] args) {
        Exchanger<Integer> exchanger = new Exchanger<>();

        Thread thread1 = new Thread(new FirstThread(exchanger));
        Thread thread2 = new Thread(new SecondThread(exchanger));

        thread1.start();
        thread2.start();
    }
}

class FirstThread implements Runnable {

    private final Exchanger<Integer> exchanger;

    public FirstThread(Exchanger<Integer> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        try {
            int dataToSend = 10;
            System.out.println("First thread is sending data " + dataToSend);

            exchanger.exchange(dataToSend);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class SecondThread implements Runnable {

    private final Exchanger<Integer> exchanger;

    public SecondThread(Exchanger<Integer> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
            int dataToSend = 20;
            System.out.println("Second thread is sending data " + dataToSend);

            int receivedData = exchanger.exchange(dataToSend);
            System.out.println("Second thread received: " + receivedData);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}