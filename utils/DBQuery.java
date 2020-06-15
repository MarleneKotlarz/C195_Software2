/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.Address;
import Model.Appointment;
import Model.Customer;
import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    public static ObservableList<String> types = FXCollections.observableArrayList();
    public static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    public static DateTimeFormatter localDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
    public static DateTimeFormatter timeDTF = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static DateTimeFormatter dateDTF = DateTimeFormatter.ofPattern("dd-MM-YYYY");
    
    
//------------------------------------//    
//-------- User login Screen --------//
//----------------------------------//    
    
    public static boolean checkLogin(String userNameInput, String passwordInput) {
        String sqlStmt = "SELECT userName, password FROM user WHERE userName = ? && password = ?";  // ? is a placeholder value
        try {
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
            System.out.println(e.getMessage()); 
        }
        return false;
    }
    
    
//----------------------------------//      
//-------- CUSTOMER SCREEN --------//
//--------------------------------//     
    
    
//-------- POPULATE CITY COMBOBOX --------//
    
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
    

//-------- GET CITY ID  --------//
    
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
    

//-------- GET ADDRESSID FROM LAST ADDED CUSTOMER --------//    
    
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
    
    
//-------- GET CUSTOMER INFO --------//
    
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
                // add variables to the customerList                
                customerList.add(new Customer(id, name, add, add2, city, zip, country, phone));

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return customerList;        
    }
       
    
    
//-------- ADD ADDRESS --------//

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
    
//-------- ADD CUSTOMER --------//
    
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
   
    
//-------- UPDATE CUSTOMER --------//
    
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
    
    
//-------- DELETE CUSTOMER AND ASSOCIATED ADDRESS --------//

    public static String deleteCustomer(String customerId) {
        try {
            // Uses CustomerID to lookup Address ID from CustomerDB table
            String addressId = null;
            String sqlStmt1 = "SELECT addressId FROM customer WHERE customerId = ?";

            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt1);
            ps.setString(1, customerId); 
            rs = ps.executeQuery(); // submits entire SQL statement
                
                while(rs.next()) { // assign rs to addressID for sqlStmt3 "?" placeholder variable
                    addressId = rs.getString(1);
                }     

            // Delete first the appointmentId where customerId is being referenced as it is the parent to the customerId
            String sqlStmt2 = "DELETE FROM appointment WHERE customerId = ?";
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt2);
            ps.setString(1, customerId);
            ps.executeUpdate(); // submits update     
            
            // Delete CustomerID second as it's the parent to the AddressDB table
            String sqlStmt3 = "DELETE FROM customer WHERE customerId = ?";
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt3);
            ps.setString(1, customerId);
            ps.executeUpdate(); // submits update 
                
            // Since parent key refrence has been deleted, we can now delete the associated addressID from the AddressDB table
            String sqlStmt4 = "DELETE FROM address WHERE addressId = ?";                 
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt4);
            ps.setString(1, addressId);
            ps.executeUpdate(); // submits delete 

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }    
   
    
//--------------------------------------------//     
//-------- APPOINTMENT / MAIN SCREEN --------//
//------------------------------------------//    
    
    
//-------- SEARCH FOR CUSTOMER --------//
    
    public static String lookUpCustomer(String customerId, String customerName) {
        try {
            String sqlStmt = "SELECT customerId, customerName FROM customer WHERE customerId = ? || customerName = ?";
            
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt);
            ps.setString(1, customerId);
            ps.setString(2, customerName);
            rs = ps.executeQuery();
            while(rs.next()) {
                String id = rs.getString("customerId");
                String name = rs.getString("customerName");
                customerList.add(new Customer(id, name));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    
    //Converts user's localdatetime to UTC 
    public static LocalDateTime stringToLDT_UTC(String time, String date) {
        LocalDateTime ldt =  LocalDateTime.parse(date + " " + time, localDTF).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        String replace = ldt.toString().replace("T", " ");
        return ldt;
    }    
    
//-------- GET APPOINTMENT INFO --------//    
    
    // Information for Appointment Tableview which displays customerName for each appointment 
    public static ObservableList<Appointment> getAllAppointments() {
        String sqlStmt = "SELECT appointmentId, ap.customerId, customerName, title, description, type, start, end \n" +
        "FROM appointment ap \n" +
        "INNER JOIN customer cu ON ap.customerId = cu.customerId \n" +
        "ORDER BY appointmentId";
        
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt);
            rs = ps.executeQuery(); // submits entire SQL statement


            while(rs.next()) {
                String apptId = rs.getString("appointmentId");
                String cusId = rs.getString("customerId");
                String cusName = rs.getString("customerName");
                String title = rs.getString("title");
                String descr = rs.getString("description");
                String type = rs.getString("type");            
                
                //Convert timestamp appointment table "start" & "end" column time from UTC to LocalDateTime that the user selected
                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                

                //-- Time conversion --//
                // START TIME //
                // Combines dateTime with a time-zone to create a ZonedDateTime . Here it is 'UTC'(ex: 2020-06-10T13:00Z[UTC])
                ZonedDateTime zdtStartOutput = startUTC.atZone(ZoneId.of("UTC"));
                // Returns a copy of dateTime with a different time-zone, retaining the instant. Here the zone is selected by systemDefault(ex: 2020-06-10T09:00-04:00[America/New_York])
                ZonedDateTime zdtStartOutToLocalTimeZone = zdtStartOutput.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
                // Convert local zone to localDateTime
                LocalDateTime ldtStartOutput = zdtStartOutToLocalTimeZone.toLocalDateTime();
                // Convert LocalDateTime to a String using a formatter so it can be passed in the .add() method
                String formattedStartTime = ldtStartOutput.format(localDTF);
                
                // END TIME //                
                ZonedDateTime zdtEndOutput = endUTC.atZone(ZoneId.of("UTC"));
                ZonedDateTime zdtEndOutToLocalTimeZone = zdtEndOutput.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
                LocalDateTime ldtEndOutput = zdtEndOutToLocalTimeZone.toLocalDateTime();
                String formattedEndTime = ldtEndOutput.format(localDTF);

                
                // Assign parameters to .add method
                appointmentList.add(new Appointment(apptId, cusId, cusName, title, descr, type, formattedStartTime, formattedEndTime));   

            }            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentList;          
    } 
    
       // Information for Appointment Tableview which displays customerName for each appointment 
    public static ObservableList<Appointment> getAllAppointmentsByMonth(String datepickerSelection) {
        String sqlStmt = "SELECT appointmentId, customerName, title, description, type, start, end \n" +
        "FROM appointment ap \n" +
        "INNER JOIN customer cu ON ap.customerId = cu.customerId \n" +
        "WHERE month(start) = ? \n" + // Insert the datepickerSelection into this variable
        "ORDER BY appointmentId";

        try {
            //String datepickerSelection = null;
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt);
            ps.setString(1, datepickerSelection);
            rs = ps.executeQuery(); // submits entire SQL statement

            
            while(rs.next()) {
                String apptId = rs.getString("appointmentId");
                String cusName = rs.getString("customerName");
                String title = rs.getString("title");
                String descr = rs.getString("description");
                String type = rs.getString("type");                            
                //Convert timestamp appointment table "start" & "end" column time from UTC to LocalDateTime that the user selected
                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                

                //-------- Time conversion --------//
                
                // START TIME //
                // Combines dateTime with a time-zone to create a ZonedDateTime . Here it is 'UTC'(ex: 2020-06-10T13:00Z[UTC])
                ZonedDateTime zdtStartOutput = startUTC.atZone(ZoneId.of("UTC"));
                // Returns a copy of dateTime with a different time-zone, retaining the instant. Here the zone is selected by systemDefault(ex: 2020-06-10T09:00-04:00[America/New_York])
                ZonedDateTime zdtStartOutToLocalTimeZone = zdtStartOutput.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
                // Convert local zone to localDateTime
                LocalDateTime ldtStartOutput = zdtStartOutToLocalTimeZone.toLocalDateTime();
                // Convert LocalDateTime to a String using a formatter so it can be passed in the .add() method
                String formattedStartTime = ldtStartOutput.format(localDTF);
                
                // END TIME //                
                ZonedDateTime zdtEndOutput = endUTC.atZone(ZoneId.of("UTC"));
                ZonedDateTime zdtEndOutToLocalTimeZone = zdtEndOutput.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
                LocalDateTime ldtEndOutput = zdtEndOutToLocalTimeZone.toLocalDateTime();
                
                String formattedEndTime = ldtEndOutput.format(localDTF);
                
                // Assign parameters to .add method
                appointmentList.add(new Appointment(apptId, cusName, title, descr, type, formattedStartTime, formattedEndTime));   

            }            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentList;          
    } 
    
        // Information for Appointment Tableview which displays customerName for each appointment 
    public static ObservableList<Appointment> getAllAppointmentsByWeek(String datepickerSelection) {
        String sqlStmt = "SELECT appointmentId, customerName, title, description, type, start, end \n" +
        "FROM appointment ap \n" +
        "INNER JOIN customer cu ON ap.customerId = cu.customerId \n" +
        "WHERE week(start) = ? \n" + // Insert the datepickerSelection into this variable minus -1 (so if it's the 24th week of the year, subtract 1 from that number)
        "ORDER BY appointmentId";
        
        // you need to include the "datepickerSelection" in your SQL statement
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt);
            ps.setString(1, datepickerSelection);            
            rs = ps.executeQuery(); // submits entire SQL statement


            while(rs.next()) {
                String apptId = rs.getString("appointmentId");
                String cusName = rs.getString("customerName");
                String title = rs.getString("title");
                String descr = rs.getString("description");
                String type = rs.getString("type");            
                
                //Convert timestamp appointment table "start" & "end" column time from UTC to LocalDateTime that the user selected
                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                

                //-------- Time conversion --------//
                
                // START TIME //
                // Combines dateTime with a time-zone to create a ZonedDateTime . Here it is 'UTC'(ex: 2020-06-10T13:00Z[UTC])
                ZonedDateTime zdtStartOutput = startUTC.atZone(ZoneId.of("UTC"));
                // Returns a copy of dateTime with a different time-zone, retaining the instant. Here the zone is selected by systemDefault(ex: 2020-06-10T09:00-04:00[America/New_York])
                ZonedDateTime zdtStartOutToLocalTimeZone = zdtStartOutput.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
                // Convert local zone to localDateTime
                LocalDateTime ldtStartOutput = zdtStartOutToLocalTimeZone.toLocalDateTime();
                // Convert LocalDateTime to a String using a formatter so it can be passed in the .add() method
                String formattedStartTime = ldtStartOutput.format(localDTF);
                
                // END TIME //                
                ZonedDateTime zdtEndOutput = endUTC.atZone(ZoneId.of("UTC"));
                ZonedDateTime zdtEndOutToLocalTimeZone = zdtEndOutput.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
                LocalDateTime ldtEndOutput = zdtEndOutToLocalTimeZone.toLocalDateTime();
                String formattedEndTime = ldtEndOutput.format(localDTF);

                
                // Assign parameters to .add method
                appointmentList.add(new Appointment(apptId, cusName, title, descr, type, formattedStartTime, formattedEndTime));   

            }            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentList;          
    } 
  
  
public static Boolean checkAppointmentOutsideBusinessHours(LocalTime comboStartSelection, LocalTime comboEndSelection) {

        LocalTime openingTime = LocalTime.of(8, 0); // 08:00 AM
        LocalTime closingTime = LocalTime.of(17, 0); // 17:00 PM
        Boolean isApptValid = true;
     
        // Checks if appt is within Business Hours
        if(comboStartSelection.isBefore(openingTime) || comboEndSelection.isAfter(closingTime))
            isApptValid = false;
           
        
        return isApptValid;    
    } 
    
    
public static Boolean checkOverlappingAppointments(String comboStartSelection, String comboEndSelection) {

        String sqlStmt = "SELECT start, end FROM appointment";
        Boolean isApptValid = true;
        
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt);
            rs = ps.executeQuery(); // submits entire SQL statement


            while(rs.next()) {
                LocalDateTime dbStartUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime dbEndUTC = rs.getTimestamp("end").toLocalDateTime();  
                
                LocalDateTime comboStart = LocalDateTime.parse(comboStartSelection);
                LocalDateTime comboEnd = LocalDateTime.parse(comboEndSelection);
                
                // For overlapping appointment
                // Checks if the appointment is within the timeframe of an existing appointment
                if(comboStart.isAfter(dbStartUTC) && comboEnd.isBefore(dbEndUTC) || 
                     comboEnd.isAfter(dbStartUTC) && comboEnd.isBefore(dbEndUTC) )
                    isApptValid = false;
           }
   
            
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        return isApptValid;    
    }
    

    

//-------- ADD APPOINTMENT --------//

    public static String addAppointment(String customerId, String title, String description, String type, String start, String end) {

        String sqlStmt = "INSERT INTO appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) \n" + 
            "VALUES (?, '1', ?, ?, 'location-sample-text', 'contact-sample-text', ?, 'url-sample-text', ?, ?, now(), 'test', now(), 'test')";    

        try {
            
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt);
            ps.setString(1, customerId); // replaces first ? placeholder in SQL statement
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setString(4, type);
            ps.setString(5, start);
            ps.setString(6, end);

            ps.execute(); // submits entire SQL statement
            
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        return null;    
    }
    

    
//-------- UPDATE APPOINTMENT --------//
    
    public static String updateAppointment(String title, String description, String type, String start, String end, String appointmentId) {

        try {
            String sqlStmt = "UPDATE appointment SET title = ?, description = ?, type = ?, start = ?, end = ? WHERE appointmentId = ?";
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt);

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, type);
            ps.setString(4, start);
            ps.setString(5, end);
            ps.setString(6, appointmentId);
            ps.execute();
            
            
      
        }catch(SQLException e) {
            System.out.println("Update Appointment SQL ERROR:" + e.getMessage());
    }
    return null;
    }
    
    
//-------- DELETE APPOINTMENT --------//
    public static String deleteAppointment(String appointmentId) {
        try {
            String sqlStmt = "DELETE FROM appointment WHERE appointmentId = ? ";
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sqlStmt);
            ps.setString(1, appointmentId);
            ps.executeUpdate();
        }catch(SQLException e) {
            System.out.println("Delete Appointment SQL ERROR:" + e.getMessage());
        }
        return null;
    }    
    
    
    
    
}
    
    
    
