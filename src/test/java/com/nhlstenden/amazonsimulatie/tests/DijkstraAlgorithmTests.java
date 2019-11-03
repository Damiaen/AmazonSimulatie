package com.nhlstenden.amazonsimulatie.tests;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nhlstenden.amazonsimulatie.models.Dijkstra;
import com.nhlstenden.amazonsimulatie.models.Graph;
import com.nhlstenden.amazonsimulatie.models.Node;
import com.nhlstenden.amazonsimulatie.models.World;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DijkstraAlgorithmTests {
    Graaf graaf = new Graaf();

    List<Knoop> knopen = new ArrayList<>();

    // 6 punten/knopen/vertices/nodes in de graaf
    Knoop knoop1 = new Knoop("Punt A", 0, 5);
    Knoop knoop2 = new Knoop("Punt B", 5, 5);
    Knoop knoop3 = new Knoop("Punt C", 10, 5);
    Knoop knoop4 = new Knoop("Punt D", 10, 10);
    Knoop knoop5 = new Knoop("Punt E", 15, 10);
    Knoop knoop6 = new Knoop("Punt F", 15, 15);

    @Before
    public void setUp() {
        knoop1.addBestemming(knoop2, 1);
        knoop1.addBestemming(knoop4, 1);
        knoop4.addBestemming(knoop5, 1);
        knoop5.addBestemming(knoop6, 1);
        knoop6.addBestemming(knoop3, 1);

        graaf.addNode(knoop1);
        graaf.addNode(knoop2);
        graaf.addNode(knoop3);
        graaf.addNode(knoop4);
        graaf.addNode(knoop5);
        graaf.addNode(knoop6);
    }

    @Test
    public void testDijkstraRightPath() {
        graaf = Dijkstra.berekenPadVanafBegin(graaf, knoop1);

        knopen = graaf.getKnoopByName("Punt C").getKorstePad();

        ArrayList<String> actualKnopen = new ArrayList<>();

        for(Knoop k : knopen) {
            actualKnopen.add(k.getNaam());
        }

        ArrayList<String> expectedKnopen = new ArrayList<>();


        //Verwachte pad dat wordt uitgekozen
        expectedKnopen.add("Punt A");
        expectedKnopen.add("Punt D");
        expectedKnopen.add("Punt E");
        expectedKnopen.add("Punt F");

        Assert.assertEquals(expectedKnopen, actualKnopen);
    }

    @Test
    public void getKnoopByLocationReturnsTheRightKnoop() {
        Knoop actualKnoop = graaf.getKnoopByLocation(15, 10);

        //Expected Knoop is "Punt E"
        Assert.assertEquals(knoop5.getX(), actualKnoop.getX());
        Assert.assertEquals(knoop5.getZ(), actualKnoop.getZ());
    }

    @Test
    public void doesKnoopReturnPathAccuratelyWithDeadEnds() {
        Knoop goodpath1 = new Knoop("goodpath 1", 0 ,0);
        Knoop goodpath2 = new Knoop("goodpath 2", 0 ,0);
        Knoop goodpath3 = new Knoop("goodpath 3", 0 ,0);
        Knoop goodpath4 = new Knoop("goodpath 4", 0 ,0);
        Knoop deadend1 = new Knoop("deadend 1", 0, 0);
        Knoop deadend2 = new Knoop("deadend 2", 0, 0);
        Knoop deadend3 = new Knoop("deadend 3", 0, 0);
        Knoop deadend4 = new Knoop("deadend 4", 0, 0);

        goodpath1.addBestemming(goodpath2, 1);
        goodpath2.addBestemming(goodpath3, 1);
        goodpath3.addBestemming(goodpath4, 1);
        goodpath2.addBestemming(deadend1,1);
        deadend1.addBestemming(deadend2, 1);
        deadend2.addBestemming(deadend3, 1);
        deadend3.addBestemming(deadend4,1);

        graaf.addNode(goodpath1);
        graaf.addNode(goodpath2);
        graaf.addNode(goodpath3);
        graaf.addNode(goodpath4);
        graaf.addNode(deadend1);
        graaf.addNode(deadend2);
        graaf.addNode(deadend3);
        graaf.addNode(deadend4);

        graaf = Dijkstra.berekenPadVanafBegin(graaf, goodpath1);

        knopen = graaf.getKnoopByName("goodpath 4").getKorstePad();

        ArrayList<String> actualKnopen = new ArrayList<>();

        for(Knoop k : knopen) {
            actualKnopen.add(k.getNaam());
        }

        ArrayList<String> expectedKnopen = new ArrayList<>();

        //Verwachte pad dat wordt uitgekozen
        expectedKnopen.add("goodpath 1");
        expectedKnopen.add("goodpath 2");
        expectedKnopen.add("goodpath 3");

        Assert.assertEquals(expectedKnopen, actualKnopen);
    }

    @Test
    public void twoRoutesSameDistanceDoesDijkstraHandleThisCorrectly() {
        Knoop cirlce1 = new Knoop("circle 1", 0 ,0);
        Knoop cirlce2 = new Knoop("circle 2", 0 ,0);
        Knoop cirlce3 = new Knoop("circle 3", 0 ,0);
        Knoop cirlce4 = new Knoop("circle 4", 0 ,0);
        Knoop cirlce5 = new Knoop("circle 5", 0, 0);
        Knoop cirlce6 = new Knoop("circle 6", 0, 0);

        cirlce1.addBestemming(cirlce2, 1);
        cirlce2.addBestemming(cirlce3, 1);
        cirlce3.addBestemming(cirlce6, 1);
        cirlce1.addBestemming(cirlce4,1);
        cirlce4.addBestemming(cirlce5, 1);
        cirlce5.addBestemming(cirlce6, 1);

        graaf.addNode(cirlce1);
        graaf.addNode(cirlce2);
        graaf.addNode(cirlce3);
        graaf.addNode(cirlce4);
        graaf.addNode(cirlce5);
        graaf.addNode(cirlce6);

        graaf = Dijkstra.berekenPadVanafBegin(graaf, cirlce1);

        knopen = graaf.getKnoopByName("circle 6").getKorstePad();

        ArrayList<String> actualKnopen = new ArrayList<>();

        for(Knoop k : knopen) {
            actualKnopen.add(k.getNaam());
        }

        ArrayList<String> expectedKnopen = new ArrayList<>();

        //Verwachte pad dat wordt uitgekozen
        expectedKnopen.add("circle 1");
        expectedKnopen.add("circle 2");
        expectedKnopen.add("circle 3");

        Assert.assertEquals(expectedKnopen, actualKnopen);    }
}