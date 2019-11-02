package com.nhlstenden.amazonsimulatie.models;



import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/*
 * Deze class is een versie van het model van de simulatie. In dit geval is het
 * de 3D wereld die we willen modelleren (magazijn). De zogenaamde domain-logic,
 * de logica die van toepassing is op het domein dat de applicatie modelleerd, staat
 * in het model. Dit betekent dus de logica die het magazijn simuleert.
 */
public class World implements Model {
    /*
     * De wereld bestaat uit objecten, vandaar de naam worldObjects. Dit is een lijst
     * van alle objecten in de 3D wereld. Deze objecten zijn in deze voorbeeldcode alleen
     * nog robots. Er zijn ook nog meer andere objecten die ook in de wereld voor kunnen komen.
     * Deze objecten moeten uiteindelijk ook in de lijst passen (overerving). Daarom is dit
     * een lijst van Object3D onderdelen. Deze kunnen in principe alles zijn. (Robots, vrachrtwagens, etc)
     */
    private List<Robot> robots;
    private List<Ship> ships;
    private List<Crate> crates;
    private Graph graph;
    private List<Node> nodes;
    Node dockNode;
    boolean worldLoaded;

    /*
     * Dit onderdeel is nodig om veranderingen in het model te kunnen doorgeven aan de controller.
     * Het systeem werkt al as-is, dus dit hoeft niet aangepast te worden.
     */
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /*
     * De wereld maakt een lege lijst voor worldObjects aan. Daarin wordt nu één robot gestopt.
     * Deze methode moet uitgebreid worden zodat alle objecten van de 3D wereld hier worden gemaakt.
     */
    public World(Graph graph)
    {
        this.graph = graph;
        nodes = graph.getNodes();
        dockNode = nodes.get(1);
        this.robots = new ArrayList<>();
        Dijkstra dijkstra = new Dijkstra(this.graph);
        this.robots.add(new Robot(dijkstra,graph.getNodes().get(1)));
        this.robots.add(new Robot(dijkstra,graph.getNodes().get(1)));

        this.ships = new ArrayList<>();
        this.ships.add(new Ship(2));

        this.crates = new ArrayList<>();
        worldLoaded = true;
    }

    /*
     * Deze methode wordt gebruikt om de wereld te updaten. Wanneer deze methode wordt aangeroepen,
     * wordt op elk object in de wereld de methode update aangeroepen. Wanneer deze true teruggeeft
     * betekent dit dat het onderdeel daadwerkelijk geupdate is (er is iets veranderd, zoals een positie).
     * Als dit zo is moet dit worden geupdate, en wordt er via het pcs systeem een notificatie gestuurd
     * naar de controller die luisterd. Wanneer de updatemethode van het onderdeel false teruggeeft,
     * is het onderdeel niet veranderd en hoeft er dus ook geen signaal naar de controller verstuurd
     * te worden.
     */
    @Override
    public void update() {

        /*
        1. check if ship is unloading
        2. go to node 1
        3. check if ship has crates
        4. pick up crate
        5. bring crate to storage node
        6. check if ship is unloading etc...
        7. if ship is empty, ship leaves
        8. robots go idle

        1. check if ship is loading
        2. check how many crates are needed
        3. send robots to get amount of crates by
        4. select amount of robots equal  or less to amount of crates needed if possible
        5. select the nodes to go to that have a crate
        6. go back to node 1 (the ship) and drop the crate off
        7. check if the needed amount of crates is delivered
        8. if true go idle
        9. else go back to step 3
         */
        Ship ship = ships.get(0);

        if (ship.getStatus().equals("UNLOADING")) {
            for (Robot robot : robots) {
                Node currentNode = robot.getCurrentNode();
                boolean hasCrate = robot.getHasCrate();
                if (robot.getStatus().equals("IDLE")) {
                    if (!hasCrate) {
                        if (ship.numberOfCrates() > 0) {
                            if (currentNode == dockNode) {
                                robot.setCrate(ship.getCrate());
                            } else {
                                robot.setTarget(dockNode);
                            }
                        }
                    }
                    else {
                        if (currentNode == robot.getTarget() && currentNode.getCanHaveCrate()) {
                            currentNode.setHasCrate(true);
                            currentNode.setCrate(robot.getCrate());
                            robot.setCrate(null);
                        }
                        else {
                            for (Node node : nodes) {
                                if (node.getCanHaveCrate() && !node.getHasCrate()) {
                                    robot.setTarget(node);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (ship.getStatus().equals("LOADING")) {
            for (Robot robot : robots) {
                Node currentNode = robot.getCurrentNode();
                boolean hasCrate = robot.getHasCrate();
                if (robot.getStatus().equals("IDLE")) {
                    if (hasCrate) {
                        if (ship.numberOfCrates() != ship.getMaxCrates()) {
                            if (currentNode == dockNode) {
                                ship.addCrate(robot.getCrate());
                                robot.setCrate(null);
                            } else {
                                robot.setTarget(dockNode);
                            }
                        }
                    }
                    else {
                        if (currentNode == robot.getTarget() && currentNode.getCanHaveCrate()){
                            currentNode.setHasCrate(true);
                            currentNode.setCrate(robot.getCrate());
                            robot.setCrate(null);
                        }
                        else {
                            for (Node node : nodes) {
                                if (node.getCanHaveCrate() && node.getHasCrate()) {
                                    robot.setTarget(node);
                                    break;
                                }
                            }
                        }
                    }

                }
            }
        }






        if (!worldLoaded) return;
        for (Object3D object : this.robots)
        {
            if (object instanceof Updatable) {
                if (((Updatable) object).update()) {
                    pcs.firePropertyChange(Model.UPDATE_COMMAND, null, new ProxyObject3D(object));
                }
            }
        }

        for (Object3D object : this.ships) {
            if (object instanceof Updatable) {
                if (((Updatable) object).update()) {
                    pcs.firePropertyChange(Model.UPDATE_COMMAND, null, new ProxyObject3D(object));
                }
            }
        }

        for (Object3D object : this.crates) {
            if (object instanceof Updatable) {
                if (((Updatable) object).update()) {
                    pcs.firePropertyChange(Model.UPDATE_COMMAND, null, new ProxyObject3D(object));
                }
            }
        }
    }

    /*
     * Standaardfunctionaliteit. Hoeft niet gewijzigd te worden.
     */
    @Override
    public void addObserver(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    /*
     * Deze methode geeft een lijst terug van alle objecten in de wereld. De lijst is echter wel
     * van ProxyObject3D objecten, voor de veiligheid. Zo kan de informatie wel worden gedeeld, maar
     * kan er niks aangepast worden.
     */
    @Override
    public List<Object3D> getWorldObjectsAsList() {
        ArrayList<Object3D> returnList = new ArrayList<>();

        for(Object3D object : this.robots) {
            returnList.add(new ProxyObject3D(object));
        }

        for(Object3D object : this.ships) {
            returnList.add(new ProxyObject3D(object));
        }

        for(Object3D object : this.crates) {
            returnList.add(new ProxyObject3D(object));
        }

        return returnList;
    }

    public List<Robot> getRobots() {
        return robots;
    }
}