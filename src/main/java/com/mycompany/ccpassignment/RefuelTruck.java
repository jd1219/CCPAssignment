/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ccpassignment;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;

/**
 *
 * @author User
 */
public class RefuelTruck implements Runnable{
    final Queue<Plane> QueueList = new LinkedList<>();

    String name;

    Plane plane;

    public RefuelTruck(String name) {
        this.name = name;
    }
    
    @Override
    public void run() {
        Logger.log(name, "Refuel Truck is ready...");
        while(true){
            synchronized(QueueList){
                if(QueueList.isEmpty()){
                    try {
                        QueueList.wait();
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(RefuelTruck.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
            
            plane = QueueList.poll();
            
            synchronized(plane){
                while (plane.fuelLevel < 100) {
                    // Refuel in increments of 20
                    plane.fuelLevel += 20;

                    // Ensure fuel level does not exceed 100
                    if (plane.fuelLevel > 100) {
                        plane.fuelLevel = 100;
                    }

                    Logger.log(name, "Refueling for Plane " + plane.getId() + " ... Progress: " + plane.fuelLevel + "%");

                    try {
                        Thread.sleep(300); // Simulate time taken to refuel
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(RefuelTruck.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            Logger.log(name, "Refueling complete for Plane " + plane.getId() + "!!!");

            }
        }
    }
    void refuelPlane(Plane plane) {
        synchronized (QueueList) {
            QueueList.offer(plane);
            if (QueueList.size() == 1) {
                QueueList.notify();
            }
        }
    }
}
