/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firewatch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aidan
 */
public class DatabaseModel {
    private Connection con;
    
    //TODO make constructor
    public DatabaseModel() throws SQLException{
        con = null;
        
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectSQLite.class.getName()).log(Level.SEVERE, null, ex);
        }
        con = DriverManager.getConnection("jdbc:sqlite:FireWatch.sqlite");
        System.out.println("Connection established");
    }
    
    
    public List<Wildfire> getRangeInclusive(String start, String end) throws SQLException, ParseException{
        System.out.println("Attempting to get data");
        List<Wildfire> fires = null;
        
        //sql query
        Statement stmt = null;
        String query = "SELECT * FROM Wildfire " +"WHERE fire_start_date > '"
                +start +"' AND ex_fs_date < '" +end +"'";
        
        System.out.println(query);
        try{
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            //parse resultSet
            fires = parseResultSet(rs);
        }
        catch (SQLException e){
            //do something???
        }
        finally{
            if(stmt != null){
                stmt.close();
            }
        }
        
        return fires;
    }
    
    private static List<Wildfire> parseResultSet(ResultSet rs) throws SQLException, ParseException {
        List<Wildfire> fires = new ArrayList<>();
        
        while (rs.next()) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
            String fireNumber = rs.getString(1);
            int year = rs.getInt(2);
            String fireName = rs.getString(3);
            double latitude = rs.getDouble(4);
            double longitude = rs.getDouble(5);
            double[] coordinates = {latitude, longitude};
            
            String cause = rs.getString(6);
            String activity = rs.getString(7);
            String start = null, end = null;
            
            if (rs.getString(8) != null){
                start = rs.getString(8);
            }
            String condition = rs.getString(9);
            
            if (rs.getString(10) != null){
                end = rs.getString(10);
            }
            float size = rs.getFloat(11);
            String sizeClass = rs.getString(12);
            
            Wildfire fire;
            fire = new Wildfire(fireNumber, year, fireName,
                    coordinates, size, sizeClass.charAt(0), start, end, cause,
                    activity, condition);
            
            fires.add(fire);
        }
        rs.close();
        return fires;
    }
    
}