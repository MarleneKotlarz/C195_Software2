/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Marlene
 */
public class Appointment {
    
    String apptId;
    String cusId;
    String cusName;
    String userId;    
    String title;
    String description;
    String type;
    String date;
    String start;
    String end;    

    public Appointment() {
    }



    
    // Constructor used for appointment TableView 
    public Appointment(String id, String cusId, String name, String title, String description, String type, String start, String end) {
        this.apptId = id;
        this.cusId = cusId;
        this.cusName = name;
        this.title = title;
        this.description = description;
        this.type = type;
        this.start = start;
        this.end = end;
    }
    
    // Constructor add appointment

    public Appointment(String apptId, String cusName, String title, String description, String type, String start, String end) {
        this.apptId = apptId;
        this.cusName = cusName;
        this.title = title;
        this.description = description;
        this.type = type;
        this.start = start;
        this.end = end;
    }
    
    

    //Constructor and addAppointment  
    public Appointment(String cusId, String title, String description, String type, String start, String end) {
        this.cusId = cusId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.start = start;
        this.end = end;
    }
    
    

    // Getters and Setters
    
    public String getApptId() {
        return apptId;
    }

    public void setApptId(String apptId) {
        this.apptId = apptId;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
    
    
    
}
