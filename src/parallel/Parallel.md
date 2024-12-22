## Parallelism In Java

### Task Parallelism:

* Task are the most basic unit of parallel programming.
* Java's ForkJoinPool performs the task asynchronously.

### Computation Graph:

    CGs can be used to define Data Races. 
    It occurs when there is no path between S1 & S2 and both read or write to one variable.

* A set of vertices or nodes which represents the tasks.
* Continue edges that capture the sequencing of steps within a task.
* Fork edges that connect a fork operation to the first step of child tasks.
* Join edges that connect the last step of a task to wait.

### Ideal Parallelism:

    WORK(G) = Sum of execution time of all nodes.
    SPAN(G) = The length of longest path in G, also known as Critical Path Length (CPL).

    Hence, Ideal = WORK / SPAN.
    It gives the upper limit of the speedup factor.

### Amdahl's Law:

    1. q < 1 is the fraction of WORK that must be executed in sequential.
    Then, the best speedup that can be obtained is: SpeedUp(P) <= 1/q

### Future:

    For tasks with return values, we can use Future objects.
    They compute the value asynchronously and when we need to get the value,
    we do f.get() which is blocking operation.

### Memoization:

    We can store the future objects in-stead of the value.
    This allows us to have caching in parallel environment.

### Determinism:

#### Functional:

* It gives the same output on the same input.

#### Structural:

* It gives the same computation graph each time.

### Loop Parallelism:

* All the iterations can run in parallel, if they are independent.
* Many languages provide for-all concept to make the iterations parallel.
* Java also provides IntStream...

### Matrix Multiplication:

    The outer to for loops of i and j can run in parallel.
    The inner k loop must run sequentially as there might be data races.

### Barriers:

    We want the iterations to run parallel.
    However, we may need all the iterations to say Hello first and then Bye.
    We use the concept of barriers.

### Jacobi's Iterative Averaging:

    An element is defined by the recurrence a[i] = (a[i - 1] + a[i + 1]) / 2
    The idea is to use two arrays oldX and newX.
    The iteration or calculation can be performed n times to solidify the results.

* If we run the n loop outside and perform for-all on the inside.
* We will have n * (n - 1) tasks, which will increase the overhead.
* Hence, we switch the loops, which will allow us to create only one (n - 1) tasks.

### Chunking:

    Many a times if we run the iterations in parallel, we will have lots of tasks.
    Hence, we divide the loop into chunks which run parallel and the inside run sequential.

### Phaser's:

    The barriers are somewhat time consuming and we can't do other tasks that are useful.
    Hence we use the concept of phasers.
    We have ph.awaitAdvance(), ph.arrive()

### Point to Point Synchronization:

    Let's say we have three tasks in medicine image pipeline.
    Then we can do await on previous task and perform arrive after completion of current task.
    Everytime the ideal parallelism will be P.

```
    Task A: X = A(), ph0.arrive(), ph1.awaitAdvance(0), D(X, Y)
    Task B: Y = B(), ph1.arrive(), ph1, ph2.awaitAdvance(), E(X, Y, Z)
```
