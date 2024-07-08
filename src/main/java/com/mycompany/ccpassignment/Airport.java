/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ccpassignment;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
//
///**
// *
// * @author FongJunDe
// */
public class Airport {
    final ReentrantLock runway = new ReentrantLock();
    final Semaphore gates = new Semaphore(3);
    final long startTime = System.currentTimeMillis();
    String name;
    RefuelTruck RefuelTruck = new RefuelTruck("Refuel Truck");
    ATCManager ATC_Mgr;
    CleaningTeam CTeam;
    
    public Airport(String name){
        this.name = name;
        this.ATC_Mgr = new ATCManager(this);
        this.CTeam = new CleaningTeam();
    }
    
    public void startServices() {
        new Thread(RefuelTruck).start();
        new Thread(this.ATC_Mgr).start();
        new Thread(CTeam).start();
    }
    
    public static void main(String[] args) {
        // start to count the time
        long systemStart = System.currentTimeMillis();
        
        Airport APAirport = new Airport("Asia Pacific Airport");
        APAirport.startServices();
        
        GeneratePlane gp = new GeneratePlane(APAirport,6);
        Thread pgThread = new Thread(gp);
        pgThread.start();
        
    }
}
