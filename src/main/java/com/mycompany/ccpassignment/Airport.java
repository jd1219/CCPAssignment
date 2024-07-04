/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ccpassignment;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author FongJunDe
 */
public class Airport {
    final Semaphore gates = new Semaphore(3);
    final ReentrantLock runway = new ReentrantLock();
    final long startTime = System.currentTimeMillis();
    String name;
    RefuelTruck RTruck;
    AirTrafficControlManager ATC_Manager;
    CleaningTeam CTeam;
    
    public Airport(String name){
        this.name = name;
        this.RTruck = new RefuelTruck("Refuel Truck");
        this.ATC_Manager = new AirTrafficControlManager(this);
        this.CTeam = new CleaningTeam();
    }
    
    public void startServices() {
        new Thread(RTruck).start();
        new Thread(this.ATC_Manager).start();
        new Thread(CTeam).start();
    }
    
    public static void main(String[] args) {
        // start to count the time
        long systemStart = System.currentTimeMillis();
        
        Airport TawauAirport = new Airport("Tawau Airport");
        TawauAirport.startServices();
        
        GeneratePlane gp = new GeneratePlane(TawauAirport, 6);
        Thread pgThread = new Thread(gp);
        pgThread.start();
        
    }
}
