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
    private final String start;
    //end date
    private final String end;
    //cause
    private final String genCause;
    //active cause
    private final String activeCause;

    //weather
    private final String weather;
    
    public WildFire(String number, int year, String name, double[] coordinates, double size, char fClass, String start, String end, String genCause, String activeCause, String weather){
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
    
    public String getNumber(){
        return number;
    }
    
    public int getYear(){
        return year;
    }
    
    public String getName() {
        return name;
    }
    
    public double[] getCoordinates(){
        return coordinates;
    }
    
    public double getSize(){
        return size;
    }
    
    public double getRadius(){
        return radius;
    }
    
    public char getfClass(){
        return fClass;
    }
    
    public String getStart(){
        return start;
    }
    
    public String getEnd(){
        return end;
    }
    
    public String getGenCause() {
        return genCause;
    }
    
    public String getActiveCause() {
        return activeCause;
    }
    
    public String getWeather(){
        return weather;
    }
}
