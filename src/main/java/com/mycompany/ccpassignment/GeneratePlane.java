/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ccpassignment;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FongJunDe
 */
public class GeneratePlane implements Runnable{
    private final Airport TAirport;
    private int planeid;

    GeneratePlane(Airport airport, int planeid) {
        this.TAirport = airport;
        this.planeid = planeid;
    }
    
    @Override
    public synchronized void run(){
        int i = 1;
        int temp = 6;
        while(i <= temp){
            // try to assign the sixth plane request to emergency landing
            if(i == 4){
                Thread plane = new Thread(new Plane(TAirport, i,true));
                plane.start();
            }else{
                Thread plane = new Thread(new Plane(TAirport, i,false)); 
                plane.start();
            }                
            i++;
            try {
                Thread.sleep(new Random().nextInt(3000));
            } catch (InterruptedException ex) {
                Logger.getLogger(GeneratePlane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}