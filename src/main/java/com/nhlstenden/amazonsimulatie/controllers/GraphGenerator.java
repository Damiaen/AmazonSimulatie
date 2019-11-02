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

    public Graph Setup()
    {
        List<Node> nodes = generateNodes();
        List<Edge> edges = generateEdges(nodes);
        return new Graph(nodes, edges);
    }

    private List<Node> generateNodes()
    {
        List<Node> nodes = new ArrayList<>();

        //1st row
        nodes.add(new Node("Node0",-42,-72, false));
        nodes.add(new Node("Node1",8,-72, false));
        nodes.add(new Node("Node2",55,-72,false));
        //2nd row
        nodes.add(new Node("Node3",-42,-40,false));
        nodes.add(new Node("Node4",8,-40,false));
        nodes.add(new Node("Node5",55,-40,false));
        //3rd row
        nodes.add(new Node("Node6",-42,-8,false));
        nodes.add(new Node("Node7",-26,-8, true));
        nodes.add(new Node("Node8",-9,-8, true));
        nodes.add(new Node("Node9",8,-8,false));
        nodes.add(new Node("Node10",17,-8, true));
        nodes.add(new Node("Node11",24,-8, true));
        nodes.add(new Node("Node12",55,-8,false));
        //4th row
        nodes.add(new Node("Node13",-42,22,false));
        nodes.add(new Node("Node14",-26,22, true));
        nodes.add(new Node("Node15",-9,22, true));
        nodes.add(new Node("Node16",8,22,false));
        nodes.add(new Node("Node17",17,22, true));
        nodes.add(new Node("Node18",24,22, true));
        nodes.add(new Node("Node19",55,22,false));
        //5th row
        nodes.add(new Node("Node20",-42,55,false));
        nodes.add(new Node("Node21",-26,55, true));
        nodes.add(new Node("Node22",-9,55, true));
        nodes.add(new Node("Node23",8,55,false));
        nodes.add(new Node("Node24",25,55, true));
        nodes.add(new Node("Node25",24,55, true));
        nodes.add(new Node("Node26",55,55,false));

        return nodes;
    }

    private List<Edge> generateEdges(List<Node> nodes)
    {

        //28 edges
        List<Edge> edges = new ArrayList<>();

        //1st row
        edges.add(new Edge(nodes.get(0),nodes.get(1),2));
        edges.add(new Edge(nodes.get(1),nodes.get(0),2));
        edges.add(new Edge(nodes.get(1),nodes.get(2),2));
        edges.add(new Edge(nodes.get(2),nodes.get(1),2));

        //1st and 3rd column on 1st row
        edges.add(new Edge(nodes.get(0),nodes.get(3),1));
        edges.add(new Edge(nodes.get(3),nodes.get(0),1));
        edges.add(new Edge(nodes.get(2),nodes.get(5),1));
        edges.add(new Edge(nodes.get(5),nodes.get(2),1));

        //2nd row
        edges.add(new Edge(nodes.get(3),nodes.get(4),2));
        edges.add(new Edge(nodes.get(4),nodes.get(3),2));
        edges.add(new Edge(nodes.get(4),nodes.get(5),2));
        edges.add(new Edge(nodes.get(5),nodes.get(4),2));

        //1st, 2nd and 3rd column on 2nd row
        edges.add(new Edge(nodes.get(3),nodes.get(6),1));
        edges.add(new Edge(nodes.get(6),nodes.get(3),1));
        edges.add(new Edge(nodes.get(4),nodes.get(9),1));
        edges.add(new Edge(nodes.get(9),nodes.get(4),1));
        edges.add(new Edge(nodes.get(5),nodes.get(12),1));
        edges.add(new Edge(nodes.get(12),nodes.get(5),1));

        //3rd row
        edges.add(new Edge(nodes.get(6),nodes.get(7),1));
        edges.add(new Edge(nodes.get(7),nodes.get(6),1));
        edges.add(new Edge(nodes.get(7),nodes.get(8),1));
        edges.add(new Edge(nodes.get(8),nodes.get(7),1));
        edges.add(new Edge(nodes.get(8),nodes.get(9),1));
        edges.add(new Edge(nodes.get(9),nodes.get(8),1));
        edges.add(new Edge(nodes.get(9),nodes.get(10),1));
        edges.add(new Edge(nodes.get(10),nodes.get(9),1));
        edges.add(new Edge(nodes.get(10),nodes.get(11),1));
        edges.add(new Edge(nodes.get(11),nodes.get(10),1));
        edges.add(new Edge(nodes.get(11),nodes.get(12),1));
        edges.add(new Edge(nodes.get(12),nodes.get(11),1));

        //1st,2nd and 3rd columns on 3nd row
        edges.add(new Edge(nodes.get(6),nodes.get(13),1));
        edges.add(new Edge(nodes.get(13),nodes.get(6),1));
        edges.add(new Edge(nodes.get(9),nodes.get(16),1));
        edges.add(new Edge(nodes.get(16),nodes.get(9),1));
        edges.add(new Edge(nodes.get(12),nodes.get(19),1));
        edges.add(new Edge(nodes.get(19),nodes.get(12),1));

        //4th row
        edges.add(new Edge(nodes.get(13),nodes.get(14),1));
        edges.add(new Edge(nodes.get(14),nodes.get(13),1));
        edges.add(new Edge(nodes.get(14),nodes.get(15),1));
        edges.add(new Edge(nodes.get(15),nodes.get(14),1));
        edges.add(new Edge(nodes.get(15),nodes.get(16),1));
        edges.add(new Edge(nodes.get(16),nodes.get(15),1));
        edges.add(new Edge(nodes.get(16),nodes.get(17),1));
        edges.add(new Edge(nodes.get(17),nodes.get(16),1));
        edges.add(new Edge(nodes.get(17),nodes.get(18),1));
        edges.add(new Edge(nodes.get(18),nodes.get(17),1));
        edges.add(new Edge(nodes.get(18),nodes.get(19),1));
        edges.add(new Edge(nodes.get(19),nodes.get(18),1));

        //1st,2nd,3rd columns on 4rd row
        edges.add(new Edge(nodes.get(13),nodes.get(20),1));
        edges.add(new Edge(nodes.get(20),nodes.get(13),1));
        edges.add(new Edge(nodes.get(16),nodes.get(23),1));
        edges.add(new Edge(nodes.get(23),nodes.get(16),1));
        edges.add(new Edge(nodes.get(19),nodes.get(26),1));
        edges.add(new Edge(nodes.get(26),nodes.get(19),1));

        //5th row
        edges.add(new Edge(nodes.get(20),nodes.get(21),1));
        edges.add(new Edge(nodes.get(21),nodes.get(20),1));
        edges.add(new Edge(nodes.get(21),nodes.get(22),1));
        edges.add(new Edge(nodes.get(22),nodes.get(21),1));
        edges.add(new Edge(nodes.get(22),nodes.get(23),1));
        edges.add(new Edge(nodes.get(23),nodes.get(22),1));
        edges.add(new Edge(nodes.get(23),nodes.get(24),1));
        edges.add(new Edge(nodes.get(24),nodes.get(23),1));
        edges.add(new Edge(nodes.get(24),nodes.get(25),1));
        edges.add(new Edge(nodes.get(25),nodes.get(24),1));
        edges.add(new Edge(nodes.get(25),nodes.get(26),1));
        edges.add(new Edge(nodes.get(26),nodes.get(25),1));

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
