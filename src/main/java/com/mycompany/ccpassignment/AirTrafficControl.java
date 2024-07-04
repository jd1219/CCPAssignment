/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ccpassignment;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;

/**
 *
 * @author FongJunDe
 */
public class AirTrafficControl implements Runnable{
    final Queue<Plane> QueueList = new LinkedList<>();
    final BlockingDeque<Plane> tempList = new LinkedBlockingDeque<>();
    private final Airport airport;
    String name;
    
    public AirTrafficControl(Airport airport, String name) {
        this.airport = airport;
        this.name = name;
    }
    
    void handleArrival(Plane plane){
        Logger.log(name, "Checking available gates for " + "Plane " + plane.getId());
        synchronized (QueueList) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(AirTrafficControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        synchronized (airport.gates) {
            while (airport.gates.availablePermits() == 0) {
                Logger.log(name, "No available gate, please hover in air " + "Plane " + plane.getId());
                try {
                    airport.gates.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        
        Logger.log(name, "Found available gate for Plane " + plane.getId());
        
        synchronized(airport.runway){
            while(airport.runway.isLocked()){
                Logger.log(name, "Runway is occupied, Plane " + plane.getId()+ " please wait ");
                try {
                    airport.runway.wait();
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(AirTrafficControl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        Logger.log(name, "Runway is empty, " + plane.getId() + " please proceed to landing");
        
        // to let the plane continue to landing
        synchronized(plane.ClearToLand){
            plane.ClearToLand.set(true);
            plane.ClearToLand.notifyAll();
        }
        
        //to remove the first plane in the queueList
        synchronized (QueueList) {
            QueueList.poll();
            QueueList.notifyAll();
        }
    }
    
    void handleDeparture(Plane plane){
        Logger.log(name,"Checking availability runway for Plane " + plane.getId() + "......");
        
        synchronized(airport.runway){
            while(airport.runway.isLocked()){
                Logger.log(name,"The Runway is occupied,Plane " + plane.getId() +", please wait!!");
                try {
                    airport.runway.wait();
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(AirTrafficControl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        Logger.log(name, "Plane " + plane.getId() + ", runway is available, please proceed to depart!!!");
        
        synchronized (plane.ClearToDepart) {
            plane.ClearToDepart.set(true);
            plane.ClearToDepart.notifyAll();
        }

        synchronized (QueueList) {
            QueueList.poll();
            QueueList.notifyAll();
        }
    }
    @Override
    public void run(){
        Logger.log(name,name + " is ready!!!");
        Plane plane;
        while(true){
            synchronized(QueueList){
                while(QueueList.isEmpty()){
                    try {
                        QueueList.wait();
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(AirTrafficControl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                plane = QueueList.peek();
                Logger.log(name,"Plane : " + plane.getId());
            }
            
            if(plane.isArriving.get()){
                handleArrival(plane);
            }else{
                handleDeparture(plane);
            }
        }
    }
}
