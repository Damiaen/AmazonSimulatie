package com.nhlstenden.amazonsimulatie.models;

import java.util.List;

public class Node
{
    int weight;
    List<Node> neighbours;

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

    public void SetNeighbour (Node node)
    {
        if (!neighbours.contains(node)) {
            neighbours.add(node);
        } else {
            return;
        }
    }
    public void SetNeighbours(List<Node> nodes)
    {
        for (int i = 0; i < nodes.size(); i++){
            neighbours.add(nodes.get(i));
        }
    }

}
