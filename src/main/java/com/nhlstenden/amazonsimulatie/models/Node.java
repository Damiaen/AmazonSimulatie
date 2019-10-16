package com.nhlstenden.amazonsimulatie.models;

import java.util.List;

public class Node
{
    private double x;
    private double y;
    private double z;

    int weight;
    List<Node> neighbours;


    public Node (double x,double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.weight = 1;
    }

    public int GetWeight() {return this.weight;}

    public void SetWeight (int weight)
    {
        if (weight > 0) {
            this.weight = weight;
        } else {
            this.weight = 1;
        }
    }

    public List<Node> GetNeighbours() {return neighbours;}

    public void SetNeighbours(List<Node> nodes)
    {
        for (int i = 0; i < nodes.size(); i++){
            neighbours.add(nodes.get(i));
        }
    }

}
