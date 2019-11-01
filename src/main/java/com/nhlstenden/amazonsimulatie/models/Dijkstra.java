package com.nhlstenden.amazonsimulatie.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Dijkstra {
    /*
     * This implementation of Dijkstra's algorithm updates all nodes in a graph with their shortest distance/path to a given node
     */

    private List<Edge> edges;
    private List<Node> nodes;

    public Dijkstra(Graph graph) {
        this.edges = graph.getEdges();
        this.nodes = graph.getNodes();
    }

    public List<Node> DijkstraAlgoritm(Node startNode, Node eindNode) {

        List<Node> nodeList = nodes;

        for (Node node : nodeList) {
            node.setDistance(1000000);
            node.setIsVisited(false);
        }
        startNode.setDistance(0);

        while (nodeList.size() > 0) {
            Node nextNode = startNode;
            double min = 10000000;
            for (Node node : nodeList) {
                if (node.getDistance() < min && !node.getIsVisited()) {
                    min = node.getDistance();
                    nextNode = node;
                }
            }

            List<Edge> nodeEdges = new ArrayList<>();
            for (Edge e : this.edges) {
                if (e.getNode1() == nextNode || e.getNode2() == nextNode) {
                    nodeEdges.add(e);
                }
            }


            for (Edge edge : nodeEdges) {
                int a = edge.getWeight() + nextNode.getDistance();
                Node n;
                if (nextNode != edge.getNode1()) {
                    n = edge.getNode1();
                } else {
                    n = edge.getNode2();
                }
                if (a < n.getDistance()) {
                    n.setDistance(a);
                    n.setPrevious(nextNode);
                }
            }

            nextNode.setIsVisited(true);
            nodeList.remove(nextNode);
        }

        List<Node> routeList = new ArrayList<>();
        Node no = eindNode;

        routeList.add(eindNode);
        while (no.getPrevious() != startNode) {
            routeList.add(no.getPrevious());
            no = no.getPrevious();
        }
        routeList.add(startNode);
        Collections.reverse(routeList);
        return routeList;

    }

}
