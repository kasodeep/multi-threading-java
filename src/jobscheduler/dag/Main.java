package jobscheduler.dag;

import java.util.Map;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Map<String, java.util.List<String>> graph = Helper.buildGraph();
        Scheduler scheduler = new Scheduler(graph);

        Random r = new Random();
        for (String task : graph.keySet()) {
            scheduler.registerTask(task, r.nextInt(1000));
        }

        scheduler.start();
    }
}

