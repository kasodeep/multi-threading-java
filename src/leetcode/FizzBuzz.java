package leetcode;

import java.util.function.IntConsumer;

public class FizzBuzz {

    private final int n;

    private int count;

    public FizzBuzz(int n) {
        this.n = n;
        this.count = 1;
    }

    public synchronized void fizz(Runnable printFizz) throws InterruptedException {
        while (count <= n) {
            boolean condition = divisible(count, 3) && !divisible(count, 5);
            if (condition) {
                printFizz.run();
                count++;
                notifyAll();
            } else {
                wait();
            }
        }

    }

    public synchronized void buzz(Runnable printBuzz) throws InterruptedException {
        while (count <= n) {
            boolean condition = divisible(count, 5) && !divisible(count, 3);
            if (condition) {
                printBuzz.run();
                count++;
                notifyAll();
            } else {
                wait();
            }
        }

    }

    public synchronized void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        while (count <= n) {
            boolean condition = divisible(count, 3) && divisible(count, 5);
            if (condition) {
                printFizzBuzz.run();
                notifyAll();
                count++;
            } else {
                wait();
            }
        }

    }

    public synchronized void number(IntConsumer printNumber) throws InterruptedException {
        while (count <= n) {
            boolean condition = !divisible(count, 3) && !divisible(count, 5);
            if (condition) {
                printNumber.accept(count);
                count++;
                notifyAll();
            } else {
                wait();
            }
        }

    }

    private boolean divisible(int number, int division) {
        return number % division == 0;
    }
}
