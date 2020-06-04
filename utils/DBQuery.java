/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.Address;
import Model.Customer;
import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

//--------------------------------------------------------    
//////////////////// User login Screen ////////////////////
//--------------------------------------------------------    
    
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
//--------------------------------------------------------      
//////////////////// CUSTOMER SCREEN ////////////////////
//--------------------------------------------------------      
    
    
////////// POPULATE CITY COMBOBOX //////////
    
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
    

////////// GET CITY ID  //////////
    
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
        } catch (SQLException e) {
            System.out.println("Check your SQL");        
        }
        return null;
    }
    

////////// GET ADDRESSID FROM LAST ADDED CUSTOMER //////////    
    
    public static String getAddressIdFromLastCustomerAdded() {
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
        } catch (SQLException e) {
            System.out.println("Check your SQL");        
        }
        return null;
    }
    
    
////////// GET CUSTOMER INFO //////////
    
public static ObservableList<Customer> getAllCustomers() {
    String sqlStmt = "SELECT customerId, customerName, address, address2, city, postalCode, country, phone\n" +            
        "FROM customer cu\n" + 
        "INNER JOIN address ad ON cu.addressId = ad.addressId\n" +
        "INNER JOIN city ci ON ad.cityId = ci.cityId\n" +
        "INNER JOIN country co ON ci.countryId = co.countryId ORDER BY customerId";
    try {
        conn = DBConnection.getConnection();
        ps = conn.prepareStatement(sqlStmt);
        rs = ps.executeQuery(); // submits entire SQL statement
            
        while(rs.next()) {
            String id = rs.getString("customerId");
            String name = rs.getString("customerName");
            String add = rs.getString("address");
            String add2 = rs.getString("address2");
            String city = rs.getString("city");
            String zip = rs.getString("postalCode");
            String country = rs.getString("country");
            String phone = rs.getString("phone");
            // add variables to the customerList for the Customer tableview in Customer controller                
            customerList.add(new Customer(id, name, add, add2, city, zip, country, phone));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;        
    }
       
    
    
////////// ADD ADDRESS //////////

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

        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        return null;
    }
    
////////// ADD CUSTOMER //////////
    
    public static String addCustomer(String customerName, String addressId) { // passing in customerDB table input fields
        String sqlStmt = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, '1', now(), 'test', now(), 'test')";
        
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt);
            ps.setString(1, customerName); // replaces first ? in SQL statement
            ps.setString(2, addressId);             
            ps.execute(); // submits entire SQL statement
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        return null;
    }    
   
    
////////// UPDATE CUSTOMER //////////
    
public static void updateCustomer(String customerId, String name, String address, String address2, String cityId, String postalCode, String phone) throws SQLException {
    
    try{            
        String sqlStmt1 = "UPDATE customer SET customerName = ? WHERE customerId = ?;";    
        conn = DBConnection.getConnection();
        ps = conn.prepareStatement(sqlStmt1);
        ps.setString(1, name);    
        ps.setString(2, customerId); 
        ps.execute();  
    
    
        String sqlStmt2 = "SELECT addressId FROM customer WHERE customerId = ?";
        conn = DBConnection.getConnection();
        ps = conn.prepareStatement(sqlStmt2);
        ps.setString(1, customerId); 
        rs = ps.executeQuery(); // submits entire SQL statement
        String addressId = null;
            while(rs.next()) {
                addressId = rs.getString(1);
            }      
    
    
        String sqlStmt3 = "UPDATE address SET address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ? WHERE addressId = ?";
        conn = DBConnection.getConnection();
        ps = conn.prepareStatement(sqlStmt3);

        ps.setString(1, address);    
        ps.setString(2, address2);
        ps.setString(3, cityId);
        ps.setString(4, postalCode);
        ps.setString(5, phone);   
        ps.setString(6, addressId);
        ps.execute();
        
    } catch(SQLException e) {
            System.out.println("SQL ERROR:" + e.getMessage());
    }       
}
    
    
////////// DELETE CUSTOMER AND ASSOCIATED ADDRESS //////////

    public static String deleteCustomer(String customerId) {
        try {
            // Uses CustomerID to lookup Address ID from CustomerDB table
            String addressId = null;
            String sqlStmt1 = "SELECT addressId FROM customer WHERE customerId = ?";

            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt1);
            ps.setString(1, customerId); 
            rs = ps.executeQuery(); // submits entire SQL statement
                
                while(rs.next()) { // assign rs to addressID for sqlStmt3 ? variable
                    addressId = rs.getString(1);
                }            

            // Delete CustomerID first as it's the parent to the AddressDB table
            String sqlStmt2 = "DELETE FROM customer WHERE customerId = ?";
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt2);
            ps.setString(1, customerId);
            ps.executeUpdate(); // submits update 
                
            // Since parent key refrence has been deleted, we can now delete the associated addressID from the AddressDB table
            String sqlStmt3 = "DELETE FROM address WHERE addressId = ?";                 
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt3);
            ps.setString(1, addressId);
            ps.executeUpdate(); // submits delete 

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }    
   
    
}
    
    
    
