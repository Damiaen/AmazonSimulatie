package com.nhlstenden.amazonsimulatie.models;

import java.util.ArrayList;

public class Node {

    private double x;
    private double y;
    private double z;

    private int distance = Integer.MAX_VALUE;
    private ArrayList<Edge> edges = new ArrayList<>();
    private Node previous;


    public Node(int i, double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }



    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public void setPrevious(Node node){ this.previous = node; }

    public Node getPrevious(){ return previous; }
}
