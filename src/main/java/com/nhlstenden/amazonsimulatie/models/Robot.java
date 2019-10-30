package com.nhlstenden.amazonsimulatie.models;

import java.util.List;
import java.util.UUID;

/*
 * Deze class stelt een robot voor. Hij impelementeerd de class Object3D, omdat het ook een
 * 3D object is. Ook implementeerd deze class de interface Updatable. Dit is omdat
 * een robot geupdate kan worden binnen de 3D wereld om zich zo voort te bewegen.
 */
class Robot implements Object3D, Updatable {
    private UUID uuid;
    private String status = "IDLE";

    private double x = -12;
    private double y = 10;
    private double z = -28;

    private double rotationX = 0;
    private double rotationY = 0;
    private double rotationZ = 0;

    private Node Target;
    private List<Node> Path;
    private Crate Crate;

    public Robot() {
        this.uuid = UUID.randomUUID();
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
                break;
            case "WORKING":
                UpdatePathFinding();
                // Pathfinding with crate
                break;
            case "RETURNING":
                // Returning to base position code
                break;
        }
        return true;
    }

    private void UpdatePathFinding() {
            if (this.Path.size() > 1)
            {

//                double sane1X = this.Path.get(0).getX();
//                double sane1Z = this.Path.get(0).getZ();
//                double sane2X = this.Path.get(1).getX();
//                double sane2Z = this.Path.get(1).getZ();
//
//                if (this.x < sane2X)
//                {
                    this.x =+ 1;
                    this.y =+ 1;
                    this.z =+ this.z;
                    this.Path.remove(0);
//                }
            }
            else
            {
                this.status = "IDLE";
            }
        }

    void SetTarget(List<Node> list)
    {
        this.Path = list;
        this.status = "WORKING";
    }

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