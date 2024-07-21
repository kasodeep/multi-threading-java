### Problem:

* In a single threaded program these instructions will be executed one by one.
* The time-consuming sections of the code can freeze the entire application

### Solution:

* Figure out the time-consuming tasks and decide if they can be run separately.
* If yes, run such task(s) in separate threads.

*Multi Threading is the ability of CPU to perform different tasks concurrently.*

### Process:

* Process is an instance of program execution.
* When you enter an application, it's a process.
* The OS assigns its own Stack & Heap Memory area.

### Thread:

* Thread is a lightweight process.
* It is a unit of execution within a given program.
* A single process may contain multiple threads.
* Each thread in the process shares the memory and resources.

### States of Thread:

* New State: When we call the start() method.
* Active State: We have two types: Runnable & Running.
* Blocked State: Waiting for some I/O execution to occur.
* Terminated State: The thread completed its execution.

### Concurrency Vs Parallelism:

* Not Concurrent, Not Parallel
    * CPU Core: ---Task1-----Task2-----

* Concurrent, Not Parallel
    * CPU Core: ---Task1---Task2---Task1----Task1----

* Not Concurrent, Parallel
    * CPU Core1: ----Task1----
    * CPU Core2: ----Task2----

* Concurrent, Parallel
    * CPU Core1: ---Task1---Task2---Task1----Task1----
    * CPU Core2: ---Task3---Task4---Task3----Task4----