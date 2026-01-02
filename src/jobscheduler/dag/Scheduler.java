package jobscheduler.dag;

import java.util.*;

public class Scheduler {

    private final Map<String, List<String>> graph;
    private final Map<String, Integer> inDegree = new HashMap<>();
    private final Map<String, Task> tasks = new HashMap<>();

    private volatile boolean stopped = false;

    public Scheduler(Map<String, List<String>> graph) {
        this.graph = graph;
        buildInDegree();
    }

    private void buildInDegree() {
        for (String node : graph.keySet()) {
            inDegree.putIfAbsent(node, 0);
            for (String dep : graph.get(node)) {
                inDegree.put(dep, inDegree.getOrDefault(dep, 0) + 1);
            }
        }
    }

    public void registerTask(String id, int workTime) {
        tasks.put(id, new Task(id, workTime, this));
    }

    public synchronized void start() {
        for (String taskId : inDegree.keySet()) {
            if (inDegree.get(taskId) == 0) {
                startTask(taskId);
            }
        }
    }

    private void startTask(String taskId) {
        if (stopped) return;
        new Thread(tasks.get(taskId)).start();
    }

    // ===== EVENTS =====
    public synchronized void onTaskSuccess(String taskId) {
        if (stopped) return;

        for (String dependent : graph.getOrDefault(taskId, List.of())) {
            int deg = inDegree.merge(dependent, -1, Integer::sum);
            if (deg == 0) {
                startTask(dependent);
            }
        }
    }

    public synchronized void onTaskFailure(String taskId, Exception e) {
        stopped = true;
        System.err.println("FAIL-FAST: stopping scheduler due to " + taskId);
    }
}

