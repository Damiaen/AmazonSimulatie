package com.nhlstenden.amazonsimulatie.models;

public class Edge {
    private Node node1;
    private Node node2;
    private int weight;

    public Edge(Node node1, Node node2, int weight) {
        this.node1 = node1;
        this.node2 = node2;
        this.weight = weight;
    }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }

    public int getWeight() {
        return weight;
    }

}
