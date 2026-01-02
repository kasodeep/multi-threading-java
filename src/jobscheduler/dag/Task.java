package jobscheduler.dag;

public class Task implements Runnable {

    private final String id;
    private final int workTime;
    private final Scheduler scheduler;

    public Task(String id, int workTime, Scheduler scheduler) {
        this.id = id;
        this.workTime = workTime;
        this.scheduler = scheduler;
    }

    public String getId() {
        return id;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(workTime);

            // simulate failure if needed
            // if ("C".equals(id)) throw new RuntimeException("boom");

            System.out.println("Task completed: " + id);
            scheduler.onTaskSuccess(id);

        } catch (Exception e) {
            System.err.println("Task failed: " + id);
            scheduler.onTaskFailure(id, e);
        }
    }
}
