package com.nhlstenden.amazonsimulatie.models;

import java.util.List;

public class WorldTile implements Object3D,Updatable
{
    // X,Y,Z = upper left corner of the tile
    // width, length and height are also the max values

    private double x;
    private double y;
    private double z;

    private double width;
    private double length;
    private double height;

    private Node lastVisited;
    private Node node;

    /*
    -- object gets a new target tile
    -- object moves by given set of tiles
    -- if object moves to a new tile. this new tile will be Occupied if the current XY values are in range of the values of the tile
    --

     */

    public WorldTile (double x, double y, double z, double width, double length, double height,Node node)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.length = length;
        this.height = height;
        this.node = node;
    }


    private boolean Occupied(double x, double y, double z)
    {
        if (x < this.x && x > this.x + width) {
            return false;
        }
        if (y < this.y && y > this.y + height) {
            return false;
        }
        if (z < this.z && z > this.z + length) {
            return false;
        }
        return true;
    }


    @Override
    public String getUUID() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public double getRotationX() {
        return 0;
    }

    @Override
    public double getRotationY() {
        return 0;
    }

    @Override
    public double getRotationZ() {
        return 0;
    }

    @Override
    public boolean update() {
        return false;
    }
}
