package com.nhlstenden.amazonsimulatie.models;

/*
 * Deze interface beschrijft wat een 3D model is. Het is een interface omdat alleen de
 * methoden worden gegeven die een object moet implementeren om een 3D model te kunnen zijn.
 * Merk op dat hier alleen getters in staan, en geen setters. Dit heeft te maken met de
 * uitvoering van het proxy pattern, en het feit dat in deze software eigenlijk bijna geen
 * setters nodig zijn.
 */
public interface Object3D {
    String getUUID();
    String getType();
    String getStatus();

    double getX();
    double getY();
    double getZ();
    
    double getRotationX();
    double getRotationY();
    double getRotationZ();
}