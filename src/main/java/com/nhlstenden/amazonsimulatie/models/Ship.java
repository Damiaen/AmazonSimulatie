package com.nhlstenden.amazonsimulatie.models;

import java.util.ArrayList;
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

    private double x = -5;
    private double y = 30;
    private double z = -120;

    private double rotationX = 0;
    private double rotationY = 0;
    private double rotationZ = 0;

    private Node Target;
    private List<Node> Path;

    private List<Crate> crates;
    private int maxCrates = 7;

    Ship(int numberOfCrates) {
        this.uuid = UUID.randomUUID();
        addCrates(numberOfCrates);
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
                if (x == 0)
                {
                    if (numberOfCrates() > 0) {
                        this.status = "UNLOADING";
                    }
                    else {
                        status = "LOADING";
                    }
                }
                break;
            case "UNLOADING":
                if (numberOfCrates() == 0) {
                    status = "DEPART";
                }
                break;
            case "LOADING":
                if (numberOfCrates() == maxCrates){status = "DEPART";}
                break;
            case "DEPART":
                this.x += 1;
                if (x == 280) {
                    this.x = -280;
                    this.status = "START";
                }
                break;
        }
        return true;
    }


    public int numberOfCrates() {
        return crates.size();
    }
    private List<Crate> addCrates (int numberOfCrates) {
        crates = new ArrayList<>();
        for (int i = 0; i < numberOfCrates; i++){
            crates.add(new Crate());
        }
        return new ArrayList<>(crates);
    }

    public Crate getCrate ()
    {
        Crate crate = crates.get(0);
        crates.remove(0);
        return crate;
    }

    public List<Crate> getCrates(){
        return crates;
    }

    public void addCrate (Crate crate)
    {
        crates.add(crate);
    }

    public int getMaxCrates() {
        return maxCrates;
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