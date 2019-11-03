package com.nhlstenden.amazonsimulatie.tests;

import com.nhlstenden.amazonsimulatie.controllers.GraphGenerator;
import com.nhlstenden.amazonsimulatie.models.Dijkstra;
import com.nhlstenden.amazonsimulatie.models.Graph;
import com.nhlstenden.amazonsimulatie.models.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class DijkstraAlgorithmTests {
    GraphGenerator graphGenerator = new GraphGenerator();
    Graph graph;
    Dijkstra dijkstra;
    List<Node> nodes;

    @Before
    public void setUp() {
        this.graph = graphGenerator.Setup();
        this.dijkstra = new Dijkstra(graph);
        this.nodes = new ArrayList<>(graph.getNodes());
    }

    @Test
    public void testDijkstraForward() {

        Node beginNode = nodes.get(5);
        Node endNode = nodes.get(23);

        List<Node> route = dijkstra.DijkstraAlgoritm(beginNode,endNode);

        List<Node> expectedNodes = new ArrayList<>();

        //Verwachte pad dat wordt uitgekozen
        expectedNodes.add(beginNode);
        expectedNodes.add(nodes.get(4));
        expectedNodes.add(nodes.get(9));
        expectedNodes.add(nodes.get(16));
        expectedNodes.add(endNode);

        System.out.println("expected nodes: ");
        for (Node n : expectedNodes) {
            System.out.println(n.getName());
        }
        System.out.println("Route nodes: ");
        for (Node n : route) {
            System.out.println(n.getName());
        }

        Assert.assertEquals(expectedNodes, route);
    }


    @Test
    public void testDijkstraBackward() {
        Node beginNode = nodes.get(23);
        Node endNode = nodes.get(5);

        List<Node> route = dijkstra.DijkstraAlgoritm(beginNode,endNode);

        List<Node> expectedNodes = new ArrayList<>();

        //Verwachte pad dat wordt uitgekozen
        expectedNodes.add(beginNode);
        expectedNodes.add(nodes.get(16));
        expectedNodes.add(nodes.get(9));
        expectedNodes.add(nodes.get(4));
        expectedNodes.add(endNode);

        System.out.println("expected nodes: ");
        for (Node n : expectedNodes) {
            System.out.println(n.getName());
        }
        System.out.println("Route nodes: ");
        for (Node n : route) {
            System.out.println(n.getName());
        }

        Assert.assertEquals(expectedNodes, route);
    }
}