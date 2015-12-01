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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author aidan
 */
public class DatabaseModel {
    private final Connection con;
    private ObservableList<Wildfire> fires;
    
    //TODO make constructor
    public DatabaseModel() throws SQLException{
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectSQLite.class.getName()).log(Level.SEVERE, null, ex);
        }
        con = DriverManager.getConnection("jdbc:sqlite:FireWatch.sqlite");
        fires = FXCollections.observableArrayList();
    }
    
    
    public List<Wildfire> getRangeInclusive(String start, String end) {
        
        //sql query
        Statement stmt = null;
        
        String query = "SELECT * FROM Wildfire " +"WHERE fire_start_date > '"
                + start + "' AND fire_start_date < '" + end + "'";
        
        try{
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            //parse resultSet
            
            try {
                fires = parseResultSet(rs);
            } catch (ParseException ex) {
                Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
            }
            return Collections.unmodifiableList(fires);
        }
        catch (SQLException e){
            System.out.println(e);
        }
        finally{
            if(stmt != null){
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
    
    private static ObservableList<Wildfire> parseResultSet(ResultSet rs) throws SQLException, ParseException {
        ObservableList<Wildfire> fires = FXCollections.observableArrayList();
        
        while (rs.next()) {
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

    public ObservableList<Wildfire> getFires() {
        return fires;
    }
}
