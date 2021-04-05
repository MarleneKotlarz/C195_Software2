/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Marlene
 */
public class DBConnection {
    // JDBC parts    
    private static final String databaseName = "U04YYf";
    private static final String DB_URL = "jdbc:mysql://3.227.166.251/"+databaseName;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String username = "U04YYf";
    private static String password = "53688381077";     
    
    private static Connection conn;

    // Database Connection
    public static void startConnection() {
        try {
            Class.forName(driver); // looks through libraries to find MySQL library
            conn = (Connection)DriverManager.getConnection(DB_URL,username,password);
            System.out.println("Connection successful."); 
        }
        catch(ClassNotFoundException e) {
            System.out.println("Error:" + e.getMessage());
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }      
    }
    
    public static Connection getConnection() throws SQLException {
        conn = DriverManager.getConnection(DB_URL,username,password);
        return conn;
    }
    
    public static void closeConnection()throws ClassNotFoundException, SQLException, Exception {
        conn.close();
            System.out.println("Connection closed!");
    }
}
