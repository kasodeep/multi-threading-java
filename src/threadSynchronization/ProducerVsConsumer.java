package threadSynchronization;

import java.util.ArrayList;

public class ProducerVsConsumer {

    public static void main(String[] args) {
        // Shared resource.
        ArrayList<Integer> list = new ArrayList<>();
        final int CAPACITY = 5;

        Producer producer = new Producer(list, CAPACITY);
        Consumer consumer = new Consumer(list);

        // Starting the two threads.
        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();
    }

    static class Producer implements Runnable {

        private final ArrayList<Integer> list;
        private final int capacity;

        public Producer(ArrayList<Integer> list, int capacity) {
            this.list = list;
            this.capacity = capacity;
        }

        @Override
        public void run() {
            int value = 0;
            try {
                while (value != 10) {
                    produce(value++);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void produce(int value) throws InterruptedException {
            synchronized (list) {
                while (list.size() == capacity) {
                    System.out.println("Producer waiting, list full");
                    list.wait(); // The thread will start executing when notified by consumer.
                }

                list.add(value);
                System.out.println("Produced: " + value);
                list.notifyAll();
            }
        }
    }

    static class Consumer implements Runnable {

        private final ArrayList<Integer> list;

        public Consumer(ArrayList<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            try {
                while (Thread.currentThread().isAlive()) {
                    consume();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void consume() throws InterruptedException {
            synchronized (list) {
                while (list.isEmpty()) { // Guarded Blocks.
                    System.out.println("Consumer waiting, list empty");
                    list.wait();
                }

                int value = list.removeFirst();
                System.out.println("Consumed: " + value);
                list.notifyAll();
            }
        }
    }
}
