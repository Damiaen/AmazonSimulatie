package com.nhlstenden.amazonsimulatie.models;



import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Time;
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
    private List<Object3D> robots;
    private List<Object3D> ships;
    private List<Object3D> crates;
    private List<WorldTile> worldTiles;
    private List<Node> Nodes;

    /*
     * Dit onderdeel is nodig om veranderingen in het model te kunnen doorgeven aan de controller.
     * Het systeem werkt al as-is, dus dit hoeft niet aangepast te worden.
     */
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /*
     * De wereld maakt een lege lijst voor worldObjects aan. Daarin wordt nu één robot gestopt.
     * Deze methode moet uitgebreid worden zodat alle objecten van de 3D wereld hier worden gemaakt.
     */
    public World() {
        this.robots = new ArrayList<>();
        this.robots.add(new Robot());

        this.ships = new ArrayList<>();
        this.ships.add(new Ship());

        this.crates = new ArrayList<>();

        //this.worldTiles = GenerateWorldTiles(10,1,10);
        GenerateNodes(7,5);

    }

    private List<WorldTile> GenerateWorldTiles(int worldWidth, int worldHeigth, int worldLength)
    {
        List<WorldTile> tiles = new ArrayList<>();
        for (int z = 0; z < worldLength; z++){
            for (int x = 0; x < worldWidth; x++){
                tiles.add(new WorldTile(x,0,z,1,1,1, "empty"));
            }
        }
        return tiles;
    }

    private List<Node> GenerateNodes(int worldWidth, int worldLength)
    {
        List<Node> nodes = new ArrayList<>();
        for (int z = 0; z < worldLength; z++)
        {
            for (int x = 0; x < worldWidth; x++)
            {
                if (x == 0 && z == 0) {
                    nodes.add(new Node(x, 1, z,3));
                    System.out.print(x + "-" + 1 + "-" + z + " ");
                }
                if (x == 3 && z == 0) {
                    nodes.add(new Node(x, 1, z,3));
                    System.out.print(x + "-" + 1 + "-" + z+ " ");
                }
                if (x == 6 && z == 0) {
                    nodes.add(new Node(x, 1, z,3));
                    System.out.print(x + "-" + 1 + "-" + z+ " ");
                }
                if (z > 1) {
                    nodes.add(new Node(x, 1, z,1));
                    System.out.print(x + "-" + 1 + "-" + z + " ");
                }
            }
        }
        return Nodes;
    }

    private void FillNeighbours(List<Node> nodes,int worldWidth, int worldLength)
    {
        /*
            0 --> world width
            |
            |
            \/
            world length
         */

        /*
        3 rijen aan dozen, 4 per rij
        1 hoofddok
        5 x 7
        .- - -.- - -.
        |           |
        .           .
        |           |
        .-.-.-.-.-.-.
        |     |     |
        .-.-.-.-.-.-.
        |     |     |
        .-.-.-.-.-.-.
         */


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
        for (Object3D object : this.robots) {
            if(object instanceof Updatable) {
                if (((Updatable)object).update()) {
                    pcs.firePropertyChange(Model.UPDATE_COMMAND, null, new ProxyObject3D(object));
                }
            }
        }

        for (Object3D object : this.ships) {
            if(object instanceof Updatable) {
                if (((Updatable)object).update()) {
                    pcs.firePropertyChange(Model.UPDATE_COMMAND, null, new ProxyObject3D(object));
                }
            }
        }

        for (Object3D object : this.crates) {
            if(object instanceof Updatable) {
                if (((Updatable)object).update()) {
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
}