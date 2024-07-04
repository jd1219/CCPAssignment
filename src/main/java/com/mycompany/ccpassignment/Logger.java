/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ccpassignment;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author FongJunDe
 */
public class Logger {
    static void log(String unit, String messages) {
        System.out.printf("[%s][%s] %s : %s%n", getTime(), Thread.currentThread().getName(), unit, messages);
    }

    static String getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return formatter.format(LocalTime.now());
    }
}
