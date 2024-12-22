## LOCKS

* Introduction to Locks
    - Lock objects work very much like the implicit locks used by synchronized code.

* Synchronized Blocks VS Locks
* Comparison of Synchronised Blocks & Locks

### Lock Conditions

* What are Conditions?
* Let's visualise these Conditions
* Code Demonstration

### Reentrant Lock

* What is Reentrant Lock?
* How does this work?
* When Reentrant locks are needed?
* Code Demonstration
* Lock Fairness
* Important methods of Reentrant Locks
  > getHoldCount()

  > tryLock()

  > tryLock(timeout, timeUnit)

  > tryLock() has a problem

  > isHeldByCurrentThread()

  > getQueueLength()

  > newCondition()

### Read Write Lock

* Introduction.
* Code Demonstration.
* Wait Queue in ReadWrite Lock.
