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
 * @author FongJunDe
 */
public class CleaningTeam implements Runnable{
    final Queue<Plane> QueueList = new LinkedList<>();

    @Override
    public void run(){
        Logger.log("Cleaning Team","Cleaning Team is ready!!");
        while(true){
            synchronized(QueueList){
                while(QueueList.isEmpty()){
                    try {
                        QueueList.wait();
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(CleaningTeam.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }    
            Plane plane = QueueList.poll();
            while(plane.cleanlinessLevel < 100){
                //Cleaning in increments of 20
                plane.cleanlinessLevel += 20;
                
                // to ensure the cleanlinessLevel do not exceed 100
                if(plane.cleanlinessLevel > 100){
                    plane.cleanlinessLevel = 100;
                }
                
                Logger.log("Cleaning Team (Plane " + plane.getId() + ")", "Cleaning Progress : " + plane.cleanlinessLevel + "%");

                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(CleaningTeam.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            Logger.log("Cleaning Team (Plane " + plane.getId() + ")", "Cleaning Complete for Plane " + plane.getId() + " !!!");
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(CleaningTeam.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    void planeCleaning(Plane plane){
        synchronized(QueueList){
            QueueList.offer(plane);
            QueueList.notifyAll();
        }
    }
}
