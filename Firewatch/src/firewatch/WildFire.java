/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firewatch;

import java.util.Date;

/**
 *
 * @author aidan
 */
public class WildFire {
    //fire number
    private final String number;
    //year
    private final int year;
    //coordinates
    private final double[] coordinates;
    //size
    private final double size;//hectares
    //class
    private final char fClass;
    //radius
    private final double radius;
    //start date
    private final Date start;
    //end date
    private final Date end;
    //cause
    private final String cause;
    //weather
    private final String weather;
    
    WildFire(String number, int year, double[] coordinates, double size,
            char fClass, Date start, Date end, String cause, String weather){
        this.number = number;
        this.year = year;
        this.coordinates = coordinates;
        this.size = size;
        this.fClass = fClass;
        this.start = start;
        this.end = end;
        this.cause = cause;
        this.weather = weather;
        //calculate radius
        this.radius = Math.sqrt((this.size *0.01) / Math.PI);
    }
    
    String getNumber(){
        return number;
    }
    
    int getYear(){
        return year;
    }
    
    double[] getCoordinates(){
        return coordinates;
    }
    
    double getSize(){
        return size;
    }
    
    double getRadius(){
        return radius;
    }
    
    char getfClass(){
        return fClass;
    }
    
    Date getStart(){
        return start;
    }
    
    Date getEnd(){
        return end;
    }
    
    String getCause(){
        return cause;
    }
    
    String getWeather(){
        return weather;
    }
}
