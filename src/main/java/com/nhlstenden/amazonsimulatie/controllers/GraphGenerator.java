package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.Dijkstra;
import com.nhlstenden.amazonsimulatie.models.Edge;
import com.nhlstenden.amazonsimulatie.models.Graph;
import com.nhlstenden.amazonsimulatie.models.Node;

import java.util.ArrayList;
import java.util.List;

public class GraphGenerator
{
    private Dijkstra dijkstra;

    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    public Graph Setup()
    {
        nodes = generateNodes();
        edges = generateEdges(nodes);
        Graph graph = new Graph(nodes,edges);
        return graph;
    }

    private List<Node> generateNodes()
    {
        List<Node> nodes = new ArrayList<>();

        //1st row
        nodes.add(new Node("Node0",-12,-30));
        nodes.add(new Node("Node1",10,-30));
        nodes.add(new Node("Node2",32,-30));
        //2nd row
        nodes.add(new Node("Node3",-12,-12));
        nodes.add(new Node("Node4",10,-12));
        nodes.add(new Node("Node5",32,-12));
        //3rd row
        nodes.add(new Node("Node6",-12,2));
        nodes.add(new Node("Node7",-5,2));
        nodes.add(new Node("Node8",3,2));
        nodes.add(new Node("Node9",10,2));
        nodes.add(new Node("Node10",17,2));
        nodes.add(new Node("Node11",24,2));
        nodes.add(new Node("Node12",32,2));
        //4th row
        nodes.add(new Node("Node13",-12,18));
        nodes.add(new Node("Node14",-5,18));
        nodes.add(new Node("Node15",3,18));
        nodes.add(new Node("Node16",10,18));
        nodes.add(new Node("Node17",17,18));
        nodes.add(new Node("Node18",24,18));
        nodes.add(new Node("Node19",32,18));
        //5th row
        nodes.add(new Node("Node20",-12,32));
        nodes.add(new Node("Node21",-5,32));
        nodes.add(new Node("Node22",3,32));
        nodes.add(new Node("Node23",10,32));
        nodes.add(new Node("Node24",17,32));
        nodes.add(new Node("Node25",24,32));
        nodes.add(new Node("Node26",32,32));

        return nodes;
    }

    private List<Edge> generateEdges(List<Node> nodes)
    {

        //28 edges
        List<Edge> edges = new ArrayList<>();

        //1st row
        edges.add(new Edge(nodes.get(0),nodes.get(1),2));
        edges.add(new Edge(nodes.get(1),nodes.get(2),2));

        //1st and 3rd column on 1st row
        edges.add(new Edge(nodes.get(0),nodes.get(3),1));
        edges.add(new Edge(nodes.get(2),nodes.get(5),1));

        //2nd row
        edges.add(new Edge(nodes.get(3),nodes.get(4),2));
        edges.add(new Edge(nodes.get(4),nodes.get(5),2));

        //1st, 2nd and 3rd column on 2nd row
        edges.add(new Edge(nodes.get(3),nodes.get(6),1));
        edges.add(new Edge(nodes.get(4),nodes.get(9),1));
        edges.add(new Edge(nodes.get(5),nodes.get(12),1));

        //3rd row
        edges.add(new Edge(nodes.get(6),nodes.get(7),1));
        edges.add(new Edge(nodes.get(7),nodes.get(8),1));
        edges.add(new Edge(nodes.get(8),nodes.get(9),1));
        edges.add(new Edge(nodes.get(9),nodes.get(10),1));
        edges.add(new Edge(nodes.get(10),nodes.get(11),1));
        edges.add(new Edge(nodes.get(11),nodes.get(12),1));

        //1st,2nd and 3rd columns on 3nd row
        edges.add(new Edge(nodes.get(6),nodes.get(13),1));
        edges.add(new Edge(nodes.get(9),nodes.get(16),1));
        edges.add(new Edge(nodes.get(12),nodes.get(19),1));

        //4th row
        edges.add(new Edge(nodes.get(13),nodes.get(14),1));
        edges.add(new Edge(nodes.get(14),nodes.get(15),1));
        edges.add(new Edge(nodes.get(15),nodes.get(16),1));
        edges.add(new Edge(nodes.get(16),nodes.get(17),1));
        edges.add(new Edge(nodes.get(17),nodes.get(18),1));
        edges.add(new Edge(nodes.get(18),nodes.get(19),1));

        //1st,2nd,3rd columns on 4rd row
        edges.add(new Edge(nodes.get(13),nodes.get(20),1));
        edges.add(new Edge(nodes.get(16),nodes.get(23),1));
        edges.add(new Edge(nodes.get(19),nodes.get(26),1));

        //5th row
        edges.add(new Edge(nodes.get(20),nodes.get(21),1));
        edges.add(new Edge(nodes.get(21),nodes.get(22),1));
        edges.add(new Edge(nodes.get(22),nodes.get(23),1));
        edges.add(new Edge(nodes.get(23),nodes.get(24),1));
        edges.add(new Edge(nodes.get(24),nodes.get(25),1));
        edges.add(new Edge(nodes.get(25),nodes.get(26),1));

        return edges;
    }

    private void bindEdgesToNodes (List<Node> nodes, List<Edge> edges)
    {
        for(Node n : nodes)
        {
            List<Edge> nodeEdges = new ArrayList<>();
            for (Edge e : edges)
            {
                if(e.getNode1() == n){
                    nodeEdges.add(e);
                }
            }
            nodeEdges.clear();
        }
    }

}
