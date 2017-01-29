/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.costlowcorp.cameraundertow;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author erik
 */
public class Formats {
    private static final DateTimeFormatter EXCHANGE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    private static final DateTimeFormatter PICTURE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss.SS");
    
    private Formats(){}
    
    public static String forExchange(LocalDateTime dateTime){
        return dateTime==null ? null : EXCHANGE_FORMATTER.format(dateTime);
    }
    
    public static LocalDateTime fromExchange(String dateTime){
        return dateTime==null ? null : LocalDateTime.parse(dateTime, EXCHANGE_FORMATTER);
    }
    
    public static String toTaken(LocalDateTime dateTime){
        return dateTime==null ? null : PICTURE_FORMATTER.format(dateTime);
    }
    
    public static LocalDateTime fromTaken(String dateTime){
        return dateTime==null ? null : LocalDateTime.parse(dateTime, PICTURE_FORMATTER);
    }
}
