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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author david
 */
public class ConnectSQLite {
    public static Connection Connect() throws SQLException, ParseException{
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectSQLite.class.getName()).log(Level.SEVERE, null, ex);
        }
        con = DriverManager.getConnection("jdbc:sqlite:FireWatch.sqlite");
        st = con.createStatement();
        rs = st.executeQuery("SELECT * FROM Wildfire;");
        while (rs.next()) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
            String fireNumber = rs.getString(1);
            int year = rs.getInt(2);
            String fireName = rs.getString(3);
            double latitude = rs.getDouble(4);
            double longitude = rs.getDouble(5);
            String cause = rs.getString(6);
            String activity = rs.getString(7);
            Date start = null, end = null;
            if (rs.getString(8) != null){
                start = df.parse(rs.getString(8));
            }
            String condition = rs.getString(9);
            if (rs.getString(10) != null){
                end = df.parse(rs.getString(10));
            }
            float size = rs.getFloat(11);
            String sizeClass = rs.getString(12);
            System.out.println(fireNumber + " " + year + " " + fireName + " " + latitude + " " + longitude + " " + cause + " " + activity + " " + start + " " + condition + " " + end + " " + size + " " + sizeClass);
        }
        rs.close();
        st.close();
        con.close();
        return null;
    }
}
