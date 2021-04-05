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
public class Report {
    String month; 
    String typeCount; 
    String type;
 
    String apptCount;
    String apptTime;


    
    // Constructor for appt type count
    public Report(String month, String typeCount, String type) {
        this.month = month;
        this.typeCount = typeCount;
        this.type = type;
    }

    // Constructor for appt time count
    public Report(String count, String time) {
        this.apptCount = count;
        this.apptTime = time;
    }
    
    
    // Getter and Setter

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTypeCount() {
        return typeCount;
    }

    public void setTypeCount(String typeCount) {
        this.typeCount = typeCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApptCount() {
        return apptCount;
    }

    public void setApptCount(String apptCount) {
        this.apptCount = apptCount;
    }

    public String getApptTime() {
        return apptTime;
    }

    public void setApptTime(String apptTime) {
        this.apptTime = apptTime;
    }
    

    
    
}


