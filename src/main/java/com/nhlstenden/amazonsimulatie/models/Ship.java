package com.nhlstenden.amazonsimulatie.models;

import java.util.List;
import java.util.UUID;

/*
 * Deze class stelt een robot voor. Hij impelementeerd de class Object3D, omdat het ook een
 * 3D object is. Ook implementeerd deze class de interface Updatable. Dit is omdat
 * een robot geupdate kan worden binnen de 3D wereld om zich zo voort te bewegen.
 */
class Ship implements Object3D, Updatable {
    private UUID uuid;
    private String status = "START";

    private double x = -70;
    private double y = 16;
    private double z = -50;

    private double rotationX = 0;
    private double rotationY = 0;
    private double rotationZ = 0;

    private Node Target;
    private List<Node> Path;



    /*
     * How long should the ship offload the items
     */
    private Integer timer = 0;

    public Ship() {
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
            case "START":
                this.x += 1;
                if (x == 90) {
                    this.status = "IDLE";
                }
                break;
            case "IDLE":
                this.timer++;
                if (this.timer == 20) {
                    spawnCrates();
                }
                if (this.timer > 40) {
                    this.status = "END";
                }
                break;
            case "END":
                this.x += 1;
                if (x == 250) {
                    this.x = -80;
                    this.timer = 0;
                    this.status = "START";
                }
                break;
        }
        return true;
    }

    private void spawnCrates() {
        System.out.println("Supposed to be spawning crates now");
    }

    private void SetTarget()
    {
        // Set crates that needs to get picked up
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
        return Ship.class.getSimpleName().toLowerCase();
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