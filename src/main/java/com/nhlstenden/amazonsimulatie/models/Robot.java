package com.nhlstenden.amazonsimulatie.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/*
 * Deze class stelt een robot voor. Hij impelementeerd de class Object3D, omdat het ook een
 * 3D object is. Ook implementeerd deze class de interface Updatable. Dit is omdat
 * een robot geupdate kan worden binnen de 3D wereld om zich zo voort te bewegen.
 */
class Robot implements Object3D, Updatable {
    private UUID uuid;
    private String status = "IDLE";

    private double x = 0;
    private double y = 10;
    private double z = 0;

    private double rotationX = 0;
    private double rotationY = 0;
    private double rotationZ = 0;

    private Node target;
    private List<Node> path;
    private Crate crate;

    private Dijkstra dijkstra;
    private Node startNode;
    private Node currentNode;

    boolean isRunning;


    public Robot(Dijkstra dijkstra, Node node) {
        this.uuid = UUID.randomUUID();
        this.dijkstra = dijkstra;
        this.path = new ArrayList<>();
        this.startNode = node;
        this.x = startNode.getX();
        this.z = startNode.getZ();
    }

    /*
     * Deze update methode wordt door de World aangeroepen wanneer de
     * World zelf geupdate wordt. Dit betekent dat elk object, ook deze
     * robot, in de 3D wereld steeds een beetje tijd krijgt om een update
     * uit te voeren. In de updatemethode hieronder schrijf je dus de code
     * die de robot steeds uitvoert (bijvoorbeeld positieveranderingen). Wanneer
     * de methode true teruggeeft (zoals in het voorbeeld), betekent dit dat
     * er inderdaad iets veranderd is en dat deze nieuwe informatie naar de views
     * moet worden gestuurd. Wordt false teruggegeven, dan betekent dit dat er niks
     * is veranderd, en de informatie hoeft dus niet naar de views te worden gestuurd.
     * (Omdat de informatie niet veranderd is, is deze dus ook nog steeds hetzelfde als
     * in de view)
     */
    @Override
    public boolean update()
    {
        switch(status) {
            case "IDLE":
                // Standings still at base location
                if (path.size() > 0) {
                    this.status = "WORKING";
                }
                break;
            case "WORKING":

                if (path.size() == 0) {
                    status = "IDLE";
                    break;
                }
                updatePathFinding();
                // Pathfinding with crate
                break;
            case "RETURNING":
                // Returning to base position code
                setTarget(startNode);
                break;
        }
        return true;
    }

    private void updatePathFinding() {
        target = path.get(0);
        currentNode = target;
        if (this.x == target.getX() && this.z == target.getZ())
        {
            currentNode = path.get(0);
            path.remove(0);
        }
        else
        {
            if (this.x != target.getX()) {
                if (this.x > target.getX()) {
                    this.x -= 1;
                } else {
                    this.x += 1;
                }
            }
            else {
                if (this.z > target.getZ()) {
                    this.z -= 1;
                } else {
                    this.z += 1;
                }
            }
        }
    }

    public void setTarget(Node target)
    {
        if (currentNode == target || startNode == target){
            return;
        }
        if (currentNode != null) {
            path = dijkstra.DijkstraAlgoritm(currentNode, target);
        }
        else {
            path = dijkstra.DijkstraAlgoritm(startNode, target);
        }
    }

    public Node getCurrentNode(){
        return currentNode;
    }

    public Node getTarget() { return target; }

    public boolean getHasCrate() {
        if (crate != null){
            return true;
        }
        else
            return false;
    }

    public Crate getCrate() { return crate; }

    public void setCrate(Crate crate) { this.crate = crate; }

    @Override
    public String getUUID() {
        return this.uuid.toString();
    }

    @Override
    public String getType() {
        /*
         * Dit onderdeel wordt gebruikt om het type van dit object als stringwaarde terug
         * te kunnen geven. Het moet een stringwaarde zijn omdat deze informatie nodig
         * is op de client, en die verstuurd moet kunnen worden naar de browser. In de
         * javascript code wordt dit dan weer verder afgehandeld.
         */
        return Robot.class.getSimpleName().toLowerCase();
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public double getZ() {
        return this.z;
    }

    @Override
    public double getRotationX() {
        return this.rotationX;
    }

    @Override
    public double getRotationY() {
        return this.rotationY;
    }

    @Override
    public double getRotationZ() {
        return this.rotationZ;
    }

}