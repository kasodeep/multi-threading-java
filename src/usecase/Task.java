package usecase;

public record Task(Runnable task, long interval, long executionTime) implements Comparable<Task> {

    @Override
    public int compareTo(Task task) {
        return Long.compare(this.executionTime, task.executionTime);
    }
}
