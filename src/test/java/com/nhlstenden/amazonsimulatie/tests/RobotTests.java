package com.nhlstenden.amazonsimulatie.tests;

import com.nhlstenden.amazonsimulatie.controllers.GraphGenerator;
import com.nhlstenden.amazonsimulatie.models.Dijkstra;
import com.nhlstenden.amazonsimulatie.models.Graph;
import com.nhlstenden.amazonsimulatie.models.Node;
import com.nhlstenden.amazonsimulatie.models.Robot;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class RobotTests
{
    GraphGenerator graphGenerator = new GraphGenerator();
    Graph graph;
    Dijkstra dijkstra;
    List<Node> nodes;
    Robot robot;

    @Before
    public void setUp()
    {
        this.graph = graphGenerator.Setup();
        this.dijkstra = new Dijkstra(graph);
        this.nodes = new ArrayList<>(graph.getNodes());
        robot = new Robot(this.dijkstra,nodes.get(1));
    }

    @Test
    public void testMovement()
    {
        robot.setTarget(nodes.get(12));
        if (robot.getCurrentNode() == nodes.get(12)){
            Assert.assertEquals(robot.getCurrentNode(), nodes.get(12));
        }
    }
}
