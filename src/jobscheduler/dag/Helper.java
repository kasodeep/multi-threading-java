package jobscheduler.dag;

import java.util.*;

public class Helper {

    public static Map<String, List<String>> buildGraph() {
        Map<String, List<String>> g = new HashMap<>();

        g.put("A", List.of("B"));
        g.put("B", List.of("C", "D"));
        g.put("C", List.of("E"));
        g.put("D", List.of("F"));
        g.put("E", List.of("F"));
        g.put("F", List.of());

        return g;
    }
}

