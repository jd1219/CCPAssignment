/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ccpassignment;

/**
 *
 * @author FongJunDe
 */
public class SanityCheck {
    static int planesHandled = 0;
    long landingWaitingTime;
    long takeOffWaitingTime;
    int passengersBoarded;
    long dockTime;
    Plane plane;

    public SanityCheck(long landingWaitingTime, long dockTime, long takeOffWaitingTime, int passengersBoarded, Plane plane) {
        this.landingWaitingTime = landingWaitingTime;
        this.dockTime = dockTime;
        this.takeOffWaitingTime = takeOffWaitingTime;
        this.passengersBoarded = passengersBoarded;
        this.plane = plane;
    }
    
    public int getPlaneId(){
        return plane.getId();
    }
    
    public long getLandingWaitingTime(){
        return landingWaitingTime / 1000;
    }
    
    public long getDockTime(){
        return dockTime / 1000;
    }
    
    public long getTakeOffWaitingTime(){
        return takeOffWaitingTime / 1000;
    }
    
    public int getPassengerBoarded(){
        return passengersBoarded;
    }
}