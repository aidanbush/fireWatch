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
    //unique id
    private String id;
    //coordinates
    private float[] coordinates;
    //size
    private float size;//km2
    //class
    private char fClass;
    //radius
    private float radius;
    //start date
    private Date start;
    //end date
    private Date end;
    //cause
    private String cause;
    
    WildFire(String id){
        this.id = id;
        //this needs to be rewritten to work with the database
    }
    
    float[] getCoordinates(){
        return coordinates;
    }
    
    float getSize(){
        return size;
    }
    
    float getRadius(){
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
}
