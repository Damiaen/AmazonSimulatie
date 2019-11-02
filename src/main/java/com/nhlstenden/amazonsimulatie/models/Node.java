package com.nhlstenden.amazonsimulatie.models;

import java.util.List;

public class Node {

    private String name;

    private double x;
    private double z;

    private int distance;
    private Node previous;
    private boolean isVisited;

    private boolean canHaveCrate;
    private boolean hasCrate;
    private boolean isRecievingCrate;

    private Crate crate;

    public Node(String name,double x, double z, boolean canHaveCrate) {
        this.name = name;
        this.x = x;
        this.z = z;
        this.canHaveCrate = canHaveCrate;
    }

    public boolean getIsRecievingCrate() {
        return isRecievingCrate;
    }
    public void setIsRecievingCrate(boolean b) {
        isRecievingCrate = b;
    }

    public boolean getIsVisited() {
        return isVisited;
    }
    public void setIsVisited(boolean b) {
        isVisited = b;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setPrevious(Node node){ this.previous = node; }

    public Node getPrevious(){ return this.previous; }

    public double getX() {
        return this.x;
    }
    public double getZ() {
        return this.z;
    }

    public String getName() {
        return this.name;
    }

    public boolean getHasCrate () {
        return hasCrate;
    }
    public void setHasCrate(boolean b) {
        hasCrate = b;
    }
    public boolean getCanHaveCrate () {
        return canHaveCrate;
    }
    public Crate getCrate() {return crate;}
    public void setCrate(Crate crate) { this.crate = crate;}
}
