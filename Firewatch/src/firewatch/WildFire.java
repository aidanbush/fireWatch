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
    //name
    private final String name;
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
    private final String genCause;
    //active cause
    private final String activeCause;

    //weather
    private final String weather;
    
    WildFire(String number, int year, String name, double[] coordinates, double size, char fClass, Date start, Date end, String genCause, String activeCause, String weather){
        this.number = number;
        this.year = year;
        this.name = name;
        this.coordinates = coordinates;
        this.size = size;
        this.fClass = fClass;
        this.start = start;
        this.end = end;
        this.genCause = genCause;
        this.activeCause = activeCause;
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
    
    String getName() {
        return name;
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
    
    String getGenCause() {
        return genCause;
    }
    
    String getActiveCause() {
        return activeCause;
    }
    
    String getWeather(){
        return weather;
    }
}
