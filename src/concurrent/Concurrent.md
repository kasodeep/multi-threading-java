## Concurrency In Java

### Critical Section:

    When we say that two threads are modifying a shared variable.
    They must do it in mutual exclusion.
    Hence, the term critical section, higher level abstract of Locks.

### Object Based Isolations(Monitors):

    When we make the sections isolated we might have threads that can run in parallel.
    Consider a linked list: A - B - C - D - E - F;
    We can delete the nodes B and E in parallel.
    Hence we perform object based isolation, like isolate(A, C, () -> {})

### Actors:

    They are higher level construct than critical sections.
    Sometimes, we might forget to add isolation in a method where the curr object is being modified.
    Hence we separate the shared variable in Actors.

* Actors have : mailbox, methods (to communicate), local variables.
* They store the messages and perform the actions accordingly.

### Optimistic Concurrency:

    Let's say we want to implement the AtomicInteger class.
    The main question is how do we implement the getAndSet().
    We use infinite for loops for this purpose.

```java
public class Atomic {
    int currVal;

    public boolean compareAndSet(int curr, int next) {
        if (currVal != curr) return false;
        currVal = next;
        return true;
    }

    public int getAndSet(int delta) {
        while (true) {
            int temp = currVal;
            if (compareAndSet(temp, temp + delta)) return temp;
        }
    }
}
```

### Linearizability:

    We say a multithreaded model is legal (linear) as long as some sequential model gives the same output.
    It helps us in identifying the correct implementations and remove bugs.
