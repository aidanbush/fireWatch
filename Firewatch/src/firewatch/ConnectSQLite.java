/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firewatch;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author david
 */
public class ConnectSQLite {
    public static Connection Connect(){
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:FireWatch.sqlite");
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM Wildfire;");
            while (rs.next()){
                String fireName = rs.getString(0);
                int year = rs.getInt(1);
                System.out.println(fireName + " " + year);
            }
            rs.close();
            st.close();
            con.close();
        } catch(ClassNotFoundException | SQLException | HeadlessException e){
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }
}
