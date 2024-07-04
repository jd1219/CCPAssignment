/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ccpassignment;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

/**
 *
 * @author User
 */
public class Plane implements Runnable{
    final AtomicBoolean ClearToLand = new AtomicBoolean(false);
    final AtomicBoolean ClearToDepart = new AtomicBoolean(false);
    final AtomicBoolean isArriving = new AtomicBoolean(true);
    Airport airport;
    int numPassenger;
    int maxPassenger;
    int fuelLevel;
    int cleanlinessLevel;
    private int planeid;
    
    public Plane(Airport airport, int planeid) {
        this.planeid = planeid;
        this.airport = airport;
        this.numPassenger = ThreadLocalRandom.current().nextInt(30, 50);
        this.maxPassenger = ThreadLocalRandom.current().nextInt(30, 50);
        this.fuelLevel = ThreadLocalRandom.current().nextInt(25, 75);
        this.cleanlinessLevel = ThreadLocalRandom.current().nextInt(25, 75);

    }
    
    public int getId(){
        return this.planeid;
    }
    
    void disembark(Plane plane){
        String[] messages = {
            "I am leaving Plane %d",
            "Nice flight on Plane %d",
            "Goodbye, Plane %d",
            "Exiting Plane %d now",
            "What an experience Plane %d"
        };
        for(int i = 1; i <= numPassenger; i++){
            String message = messages[ThreadLocalRandom.current().nextInt(messages.length)];
            Logger.log("Passenger " + i, String.format(message,getId()));
            try {
                Thread.sleep(150);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            Thread.sleep(400);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void embark(Plane plane){
        String[] messages = {
            "I am boarding Plane %d",
            "Hope its a nice flight on Plane %d",
            "Hello, Plane %d",
            "Boarding Plane %d now",
            "Finally you here, Plane %d"
        };
        for(int i = 1; i <= maxPassenger; i++){
            String message = messages[ThreadLocalRandom.current().nextInt(messages.length)];
            Logger.log("Passenger " + i, String.format(message,getId()));
                    try {
                Thread.sleep(150);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            Thread.sleep(400);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        Logger.log("Plane " + this.planeid, "Request to Land");
        
        airport.ATC_Manager.RequestLanding(this);
        
        // start to record the waiting time of plane to landing
        long landingWaitTimeStart = System.currentTimeMillis();
        
        synchronized(ClearToLand){
            if(!ClearToLand.get()){
                Logger.log("Plane " + this.planeid, "Awaiting permission to land.");
                try{
                    ClearToLand.wait();
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        try {
            //let the runway to be accupied and adding 1 permits to the semaphore gates
            airport.runway.lock();
            airport.gates.acquire(1);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Logger.log("Plane " + this.planeid, "Permission get, ready to landing!!!");
        try {
            Thread.sleep(800);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Logger.log("Plane " + this.planeid, "Landed successfully!!!");
        try {
            Thread.sleep(800);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // record the time that plane alr landed
        long landingWaitTime = System.currentTimeMillis() - landingWaitTimeStart;
        
        // record the time of plane after landed
        long dockTimeStart = System.currentTimeMillis();
        
        // unlock the runway and let other plane to access it
        synchronized(airport.runway){
            airport.runway.unlock();
            airport.runway.notifyAll();
        }
        
        Logger.log("Plane " + this.planeid, "Heading to gates......");
        try {
            Thread.sleep(800);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Logger.log("Plane " + this.planeid, "Docking now.....");
        try {
            Thread.sleep(800);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Logger.log("Plane " + this.planeid, "Docked successfully.....");
        try {
            Thread.sleep(800);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //refuel, cleaning, disembark and embark the passengers
        airport.RTruck.refuelPlane(this);
        airport.CTeam.planeCleaning(this);
        disembark(this);
        embark(this);
        
        while (cleanlinessLevel < 100) {
            Logger.log("Plane " + this.planeid, "Plane not cleaned yet, waiting on CLEANING......");
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        while (fuelLevel < 100) {
            Logger.log("Plane " + this.planeid, "Plane not refueled yet, waiting on REFUELLING......");
            try {
                Thread.sleep(800);
            }  catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        
        Logger.log("Plane " + this.planeid, "Refueled and Cleaned, ready to undock......");
        
        // record the time that plane on land
        long dockTime = System.currentTimeMillis() - dockTimeStart;
        
        // record the time that plane ready to take off
        long takeOffWaitingTimeStart = System.currentTimeMillis();
        
        this.isArriving.set(false);
        
        airport.ATC_Manager.planeDeparture(this);
        
        // check if the runway is clear
        synchronized(ClearToDepart){
            if(!ClearToDepart.get()){
                try {
                    ClearToDepart.wait();
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        //to let the runway be lock and other plane cannot access
        airport.runway.lock();
        
        Logger.log("Plane " + this.planeid, "Depart confirmed, ready to undock now......");
        try {
            Thread.sleep(800);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Logger.log("Plane " + this.planeid, "Heading towards runway......");
        try {
            Thread.sleep(800);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Logger.log("Plane " + this.planeid, "Ready to take off......");
        try {
            Thread.sleep(800);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Logger.log("Plane " + this.planeid, "Take off successfully, goodbye Tawau Airport");
        try {
            Thread.sleep(800);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // record the time that plane ready to take off
        long takeOffWaitingTime = System.currentTimeMillis() - takeOffWaitingTimeStart;
        
        synchronized(airport.runway){
            // unlock the runway after plane took off and let other plane access to it
            airport.runway.unlock();
            airport.runway.notifyAll();
        }
        
        synchronized(airport.gates){
            // to release a gate and let other plane to dock
            airport.gates.release(1);
            airport.gates.notifyAll();
        }
        
        // create sanityCheck object and add it into the statistics array
        SanityCheck stats = new SanityCheck(landingWaitTime, dockTime, takeOffWaitingTime, maxPassenger, this);
        airport.ATC_Manager.statistics.add(stats);
        SanityCheck.planesHandled++;
        
    }
}
