package leetcode;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintInOrder {

    static class FooMethod {
        private boolean oneDone;
        private boolean twoDone;

        public FooMethod() {
            oneDone = false;
            twoDone = false;
        }

        public synchronized void first(Runnable printFirst) {
            printFirst.run();
            oneDone = true;
            notifyAll();
        }

        public synchronized void second(Runnable printSecond) throws InterruptedException {
            while (!oneDone) {
                wait();
            }
            printSecond.run();
            twoDone = true;
            notifyAll();
        }

        public synchronized void third(Runnable printThird) throws InterruptedException {
            while (!twoDone) {
                wait();
            }
            printThird.run();
        }
    }

    class FooCondition {
        private final Lock lock;
        private boolean oneDone;
        private boolean twoDone;
        private final Condition one;
        private final Condition two;

        public FooCondition() {
            lock = new ReentrantLock();
            one = lock.newCondition();
            two = lock.newCondition();
            oneDone = false;
            twoDone = false;
        }

        public void first(Runnable printFirst) {
            lock.lock();
            try {
                printFirst.run();
                oneDone = true;
                one.signal();
            } finally {
                lock.unlock();
            }
        }

        public void second(Runnable printSecond) throws InterruptedException {
            lock.lock();
            try {
                while (!oneDone) {
                    one.await();
                }
                printSecond.run();
                twoDone = true;
                two.signal();
            } finally {
                lock.unlock();
            }
        }

        public void third(Runnable printThird) throws InterruptedException {
            lock.lock();
            try {
                while (!twoDone) {
                    two.await();
                }
                printThird.run();
            } finally {
                lock.unlock();
            }
        }
    }
}
