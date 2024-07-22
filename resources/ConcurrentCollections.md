## CONCURRENT COLLECTIONS

* Most of the Java collections are not thread safe.
* Ways to make collections thread safe.
* Use `Collections.synchronize()` method.
* Use the concurrent collections which are synchronized.

#### Downsides of using the Collections.synchronized() approach

* Coarse grained locking
* Limited functionality
* No Fail Fast Iterators
* Performance Overhead

### Countdown Latch

* Introduction to countdown latch.
* When to use countdown latch.
* Code demonstration.
* Is it functionally similar to join?
    * Purpose
    * Usage
* Can we reset the count?

### Cyclic Barrier

* Introduction to Cyclic Barrier.
* Code Demonstration.
* How does Cyclic Barrier work under the hood?

### Blocking Queue

* Introduction to Blocking Queue
* Blocking Queue Interfaces
    * BlockingQueue
    * BlockingDeque
    * TransferQueue

* Major implementations
    * ArrayBlockingQueue
    * LinkedBlockingQueue
    * PriorityBlockingQueue
    * DelayQueue
    * SynchronousQueue

* Code Demonstration

### Concurrent Map

* Introduction to Concurrent Map
* What is ConcurrentMap?
* Why do we need ConcurrentMap?
* Concurrent Map Implementations
    * ConcurrentHashMap
    * ConcurrentSkipListMap
    * ConcurrentLinkedHashMap
    * ConcurrentNavigableMap

* Code Demonstration
* Internal implementation and working of Concurrent Map

* Adding an element to concurrent hash map.

1. Hashing and Determining Segment
2. Acquiring Lock
3. Insertion in Segment
4. Releasing Lock

* Fetching an element from concurrent hash map.

1. Hashing and Determining Segment
2. Acquiring Lock
3. Searching in Segment
4. Releasing Lock

### Exchanger

* Introduction to Exchanger

* What is Exchanger in Java?
* Why and when to use Exchangers?

* Exchanger implementation in Java
* Code Demonstration
* Comparing Queue with Exchanger

* Exchanger
    * Point to Point communication.
    * Simplicity for two threads.
    * Synchronous.
    * Symmetrical exchange.

* Queue
    * One to many communication.
    * Asynchronous.
    * Buffering.
    * Non-symmetrical exchange.
* Similarity to Synchronous Queue

### Copy On Write Array

* Introduction to COWA

* What is COWA?
* Why and When to use COWA

* Code Demonstration
* How it works?
* Comparing the approach to Git branching!
* Let's visualise!








