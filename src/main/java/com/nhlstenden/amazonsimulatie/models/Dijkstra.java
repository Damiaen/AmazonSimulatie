package com.nhlstenden.amazonsimulatie.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Dijkstra {
        /*
         * This implementation of Dijkstra's algorithm updates all nodes in a graph with their shortest distance/path to a given node
         */

        private Graph graph;
        private ArrayList<Edge> edges;
        private Set<Node> unsearched;
        private Set<Node> searched;

        public Dijkstra(Graph graph) {
            this.graph = graph;
            this.edges = new ArrayList(graph.getEdges());
        }

        /*
         * Updates all nodes with shortest distance from start. printPath(end) must be called separately.
         */
        public void run(Node start) {

            unsearched = new HashSet<>();
            searched = new HashSet<>();

            Node current = start;		//Set the current node to start
            start.setDistance(0);		//
            unsearched.add(start);

            while(!unsearched.isEmpty()) {
                //Set current to the node with the least distance to start
                current = getClosest();
                searched.add(current);
                unsearched.remove(current);
                updateNeighborDistances(current);
            }
        }
        /*
         * @param destination the node whose shortest path to the start node we want to print
         */
        public void printPath(Node destination) {
            System.out.println("Total distance traveled: " + destination.getDistance());
            Node current = destination;
            Stack<Node> path = new Stack<>();
            path.push(destination);

            //Enqueue all path nodes to a stack (so we can easily print in reverse order)
            while(current.getPrevious()!= null) {
                current = current.getPrevious();
                path.push(current);
            }

            //Print out the path in the correct order
            do {
                System.out.println(path.pop());
            }
            while(!path.isEmpty());
        }

        /*
         * @param curr the node whose neighbors we will update
         */
        private void updateNeighborDistances(Node curr) {
            List<Node> neighbors = getNeighbors(curr);
            int distance = curr.getDistance();
            for(Node neighbor : neighbors) {
                //Updates distances for nodes that neighbor @param curr
                int temp = getDistanceFrom(curr, neighbor);
                if(distance + temp < neighbor.getDistance()) {
                    neighbor.setPrevious(curr);
                    neighbor.setDistance(distance + temp);
                    unsearched.add(neighbor);
                }
            }
        }

        /*
         * @return the unsearched node with the shortest path to the start
         */
        private Node getClosest() {
            Node closest = null;
            for(Object node : unsearched) {
                if(closest == null) {
                    closest = (Node)node;
                }
                else {
                    if(((Node) node).getDistance() < closest.getDistance()) {
                        closest = (Node)node;
                    }
                }
            }
            return closest;
        }

        private int getDistanceFrom(Node start, Node end) {
            for(Edge e : edges) {
                if((e.getNode1().equals(start) && e.getNode2().equals(end))) {
                    return e.getWeight();
                }
            }
            return 0;
        }

        private ArrayList<Node> getNeighbors(Node n) {
            ArrayList<Node> neighbors = new ArrayList<Node>();
            for(Edge edge : edges) {
                if(edge.getNode1().equals(n)) {
                    neighbors.add(edge.getNode2());
                }
            }
            return neighbors;
        }

    }
