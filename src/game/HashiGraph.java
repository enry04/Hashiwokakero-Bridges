package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HashiGraph {

    private int nIslands;
    private List<List<Integer>> adjacencyList;

    public HashiGraph(List<Island> islands) {
        this.nIslands = islands.size();
    }

    public void buildGraph(Map<String, Integer> bridgeValues) {
        initGraph();
        for (Map.Entry<String, Integer> entry : bridgeValues.entrySet()) {
            int currentBridgeValue = entry.getValue();
            if (currentBridgeValue > 0) {
                String[] parts = entry.getKey().split("_");  //["bridge", "i", "j"]
                int firstNode = Integer.parseInt(parts[1]);
                int secondNode = Integer.parseInt(parts[2]);
                adjacencyList.get(firstNode).add(secondNode);
                adjacencyList.get(secondNode).add(firstNode);
            }
        }
    }

    private void initGraph() {
        adjacencyList = new ArrayList<>();
        for (int i = 0; i < nIslands; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public boolean isGraphConnected() {

        boolean[] visited = new boolean[nIslands];
        depthFirstSearch(visited, 0);

        for (boolean isVisited : visited) {
            if (!isVisited) return false;
        }
        return true;
    }

    private void depthFirstSearch(boolean[] visited, int currentNode) {
        visited[currentNode] = true;
        for (int nextNode : adjacencyList.get(currentNode)) {
            if (!visited[nextNode]) {
                depthFirstSearch(visited, nextNode);
            }
        }
    }

    public List<List<Integer>> getComponents() {
        List<List<Integer>> components = new ArrayList<>();
        boolean[] visited = new boolean[nIslands];

        for (int i = 0; i < nIslands; i++) {
            if (!visited[i]) {
                List<Integer> component = new ArrayList<>();
                depthFirstSearchComponent(i, visited, component);
                components.add(component);
            }
        }
        return components;
    }

    private void depthFirstSearchComponent(int currentNode, boolean[] visited, List<Integer> component) {
        visited[currentNode] = true;
        component.add(currentNode);
        for (int neighbor : adjacencyList.get(currentNode)) {
            if (!visited[neighbor]) {
                depthFirstSearchComponent(neighbor, visited, component);
            }
        }
    }

}
