package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HashiGraph {

    private int nIslands;
    private List<List<Integer>> adjacencyList;

    public HashiGraph() {
        adjacencyList = new ArrayList<>();
    }

    public void initGraph(List<Island> islands) {
        nIslands = islands.size();
    }

    public void buildGraph(Map<String, Integer> bridgeValues) {
        for (Map.Entry<String, Integer> entry : bridgeValues.entrySet()) {
            int currentBridgeValue = entry.getValue();
            if (currentBridgeValue > 0) {
            
            }
        }
    }

}
