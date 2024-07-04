/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ccpassignment;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author FongJunDe
 */
public class AirTrafficControlManager implements Runnable{
    final Airport airport;
    AirTrafficControl ATC_Arrival;
    AirTrafficControl ATC_Departure;
    ArrayList<SanityCheck> statistics = new ArrayList<>();
    
    public AirTrafficControlManager(Airport airport){
        this.airport = airport;
        this.ATC_Arrival = new AirTrafficControl(airport, "ATC Arrival");
        this.ATC_Departure = new AirTrafficControl(airport, "ATC Departure");

    }
    
    public void startServices(){
        new Thread(ATC_Arrival).start();
        new Thread(ATC_Departure).start();
    }
    
    void RequestLanding(Plane plane){
        synchronized (ATC_Arrival.QueueList) {
            //adding a plane into the queue
                ATC_Arrival.QueueList.offer(plane);
            ATC_Arrival.QueueList.notifyAll();
        }
    }
    
    void planeDeparture(Plane plane) {
        synchronized (ATC_Departure.QueueList) {
            //adding a plane into the queue
            ATC_Departure.QueueList.offer(plane);
            ATC_Departure.QueueList.notifyAll();
        }
    }
    
    @Override
    public void run() {
        startServices();
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        
        while(!ATC_Arrival.QueueList.isEmpty() ||!ATC_Departure.QueueList.isEmpty() || airport.runway.isLocked() || airport.gates.availablePermits() < 3){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(AirTrafficControlManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        Logger.log("ATC Manager","All planes has already settled, generating SanityCheck Report");
        logStatistics();
        
    }
    
    public void logStatistics(){
        if(statistics.isEmpty()){
            Logger.log("Statistics","No data available.");
            return;
        }
        
        //table to show the available gates, number of planes served and availability of runway
        StringBuilder table3 = new StringBuilder();
        String format3 = "| %-15s | %-23s | %-7s |\n";
        
        table3.append(String.format(format3,
                "---------------",
                "-----------------------",
                "------"
        ));
        
        table3.append(String.format(format3,
                "Available Gates",
                "Number of Planes Served",
                "Runway"
        ));
        
        table3.append(String.format(format3,
                "---------------",
                "-----------------------",
                "------"
        ));
        
        table3.append(String.format(format3,
                airport.gates.availablePermits(),
                statistics.size(),
                airport.runway.isLocked() ? "Locked" : "Clear"
        ));
        
        // to show the stats of each plane
        StringBuilder table = new StringBuilder();
        String format = "| %-8s | %-20s | %-10s | %-21s | %-18s |\n";
        
        table.append(String.format(format, 
                "--------", 
                "--------------------", 
                "----------", 
                "---------------------", 
                "------------------"
        ));
        
        table.append(String.format(format,
                "Plane ID",
                "Landing Waiting Time",
                "Dock Time",
                "Take Off Waiting Time",
                "Passengers Boarded"
        ));
        
        table.append(String.format(format, 
                "--------", 
                "--------------------", 
                "----------", 
                "---------------------", 
                "------------------"
        ));
        
        long minLandingWaitingTime = Long.MAX_VALUE;
        long maxLandingWaitingTime = Long.MIN_VALUE;
        long totalLandingWaitingTime = 0;
        
        long minTakeOffWaitingTime = Long.MAX_VALUE;
        long maxTakeOffWaitingTime = Long.MIN_VALUE;
        long totalTakeOffWaitingTime = 0;
        
        for (SanityCheck check : statistics) {
            long landingTimeSec = check.getLandingWaitingTime();
            long takeOffTimeSec = check.getTakeOffWaitingTime();
            
            minLandingWaitingTime = Math.min(minLandingWaitingTime, landingTimeSec);
            maxLandingWaitingTime = Math.max(maxLandingWaitingTime, landingTimeSec);
            totalLandingWaitingTime += landingTimeSec;
            
            minTakeOffWaitingTime = Math.min(minTakeOffWaitingTime, takeOffTimeSec);
            maxTakeOffWaitingTime = Math.max(maxTakeOffWaitingTime, takeOffTimeSec);
            totalTakeOffWaitingTime += takeOffTimeSec;
            
            table.append(String.format(format,
                    check.getPlaneId(),
                    landingTimeSec + " sec",
                    check.getDockTime() + " sec",
                    takeOffTimeSec + " sec",
                    check.getPassengerBoarded()
            ));
        }
        
        long avrgLandingWaitingTime = totalLandingWaitingTime / statistics.size();
        long avrgTakeOffWaitingTime = totalTakeOffWaitingTime / statistics.size();
        
        StringBuilder table2 = new StringBuilder();
        String format2 = "| %-24s | %-24s | %-28s | %-25s | %-25s | %-29s  |\n";
        
        table2.append(String.format(format2,
                "------------------------",
                "------------------------",
                "----------------------------", 
                "-------------------------", 
                "-------------------------", 
                "-----------------------------"
        ));
        
        // to show the min, max and avrage waiting time of landing and take off
        table2.append(String.format(format2,
                "Min Landing Waiting Time",
                "Max Landing Waiting Time",
                "Average Landing Waiting Time",
                "Min Take Off Waiting Time",
                "Max Take Off Waiting Time",
                "Average Take Off Waiting Time"
        ));
        
        table2.append(String.format(format2,
                "------------------------",
                "------------------------",
                "----------------------------", 
                "-------------------------", 
                "-------------------------", 
                "-----------------------------"
        ));
        
        table2.append(String.format(format2,
                minLandingWaitingTime + " sec",
                maxLandingWaitingTime + " sec",
                avrgLandingWaitingTime + " sec",
                minTakeOffWaitingTime + " sec",
                maxTakeOffWaitingTime + " sec",
                avrgTakeOffWaitingTime + " sec"
        ));
        
        Logger.log("Statistics","\n" + table3.toString());
        
        Logger.log("Plane Statistics", "\n" + table.toString());
        
        Logger.log("Waiting Time Table", "\n" + table2.toString());
        
        long systemEndTime = System.currentTimeMillis() - airport.startTime;
        Logger.log("System",String.format("System running time: %d sec", systemEndTime/1000));
    }
}