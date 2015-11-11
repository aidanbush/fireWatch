/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firewatch;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author aidan
 */
public class DatabaseModel {
    
    
    List<WildFire> getRangeInclusive(Connection con, Date start, Date end){
        List<WildFire> fires = null;
        
        //sql query
        Statement stmt = null;
        String query = "SELECT * FROM filename "
                + "WHERE fire_start_date => start AND ex_fs_date =< end";
        
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

    private List<WildFire> parseResultSet(ResultSet rs) throws SQLException {
        List<WildFire> fires = new ArrayList<>();
        WildFire fire;
        
        while(!rs.isLast()){
            //create fire
        }
    }
    
}
