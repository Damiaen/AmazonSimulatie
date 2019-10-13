package com.nhlstenden.amazonsimulatie.models;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.List;

public class WorldTile implements Model
{
    private double X;
    private double Y;
    private double Z;

    private List<WorldTile> NeighbourTiles;

    public WorldTile (double X, double Y, double Z) {

    }

    @Override
    public void update() {

    }

    @Override
    public void addObserver(PropertyChangeListener pcl) {

    }

    @Override
    public java.util.List<Object3D> getWorldObjectsAsList() {
        return null;
    }
}
