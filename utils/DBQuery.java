/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.Customer;
import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Marlene
 */
public class DBQuery {
    
    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;
    
    public static ObservableList<String> cityList = FXCollections.observableArrayList();
    public static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    
// User login Screen
    public static boolean checkLogin(String userNameInput, String passwordInput) {
        String sqlStmt = "SELECT userName, password FROM user WHERE userName = ? && password = ?";  // ? is a placeholder value
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt);
            // criteria for the statement
            ps.setString(1, userNameInput); // replaces first ? in SQL statement
            ps.setString(2, passwordInput); // replaces second ? in SQL statement
            // process results
            rs = ps.executeQuery(); // submits entire SQL statement
            
            if (rs.next()) {
                User user = new User();
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                return true;
            }
            else {
                return false;
            }
        } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
                return false;
        }
    }
    
//////////////////// CUSTOMER SCREEN ////////////////////
    
////////// populate city comboBox //////////
    public static ObservableList<String> getAllCities()  {
        String sqlStmt = "SELECT city FROM city";
        try {      
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt);
            rs = ps.executeQuery(); // submits entire SQL statement
            
            while(rs.next()) {
                String city = rs.getString("city");
                Customer cusCity = new Customer(city);                
                cityList.add(cusCity.getCity());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cityList;
    }
    

////////// get cityId //////////
    public static String getCityId(String city) {
    String sqlStmt = "SELECT cityId FROM city WHERE city = ?"; // ? is a placeholder value
        rs = null;
        String cityId;
        
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt);
            ps.setString(1, city); // replaces first ? in SQL statement
            rs  = ps.executeQuery(); // submits entire SQL statement
            
            while (rs.next()) {
                cityId=rs.getString("cityId"); // getting the SQL results which is the cityId column
                return cityId;
            }
        }catch (SQLException e) {
            System.out.println("Check your SQL");        
        }
        return null;
    }
    

////////// get addressId //////////    
    public static String getAddressIdFromLastCostumerAdded() {
    String sqlStmt = "SELECT addressId FROM address Order By addressId DESC Limit 1"; // ? is a placeholder value
        rs = null;
        String addressId;
        
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt);
            rs  = ps.executeQuery(); // submits entire SQL statement
            while (rs.next()) {
                addressId=rs.getString("addressId"); // getting the SQL results which is the addressId column
                return addressId;
            }
        }catch (SQLException e) {
            System.out.println("Check your SQL");        
        }
        return null;
    }

    
////////// Add Address //////////
    public static String addAddress(String address, String address2, String cityId, String postalCode, String phone) { // passing in addressDB table input fields
        String sqlStmt = "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, ?, ?, ?, now(), 'test', now(), 'test')";
        
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt);
            ps.setString(1, address); // replaces first ? in SQL statement
            ps.setString(2, address2); 
            ps.setString(3, cityId);
            ps.setString(4, postalCode); 
            ps.setString(5, phone); 
  
            ps.execute(); // submits entire SQL statement

        }catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        return null;
    }
    
////////// Add Customer //////////
    public static String addCustomer(String customerName, String addressId) { // passing in customerDB table input fields
        String sqlStmt = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, '1', now(), 'test', now(), 'test')";
        
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt);
            ps.setString(1, customerName); // replaces first ? in SQL statement
            ps.setString(2, addressId);             
            ps.execute(); // submits entire SQL statement
        }catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        return null;
    }    
    
    
//    INSERT INTO `U04YYf`.`customer`
//(`customerName`,
//`addressId`,
//`active`,
//`createDate`,
//`createdBy`,
//`lastUpdate`,
//`lastUpdateBy`)
    
////////// get allCustomers //////////
//public static ObservableList<Customer> getAllCostumers() {
//    String sqlStmt = "Select customerId, customerName, address, address2, city, postalCode, country, phone"
//}       
     
}   
    
        

    
    
    
    
    




   
    
    
    
    
    
    
    
    

