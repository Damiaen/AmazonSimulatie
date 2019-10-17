package com.nhlstenden.amazonsimulatie.models;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List nodes;
    private List edges;

    public Graph(List nodes, List edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public List getEdges() {
        return edges;
    }

    public List getNodes() {
        return nodes;
    }
}
