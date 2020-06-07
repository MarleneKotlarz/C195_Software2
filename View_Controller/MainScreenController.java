/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import Model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author Marlene
 */
public class MainScreenController implements Initializable {

    ////////// BUTTONS //////////
    @FXML private Button btAddAppt;
    @FXML private Button btDisplayCus;
    @FXML private Button btDisplayReports;
    @FXML private Button btLogout;    
    @FXML private Button btUpdateAppt;
    @FXML private Button btSearchCustomer;
    @FXML private Button btResetCustomer;
    @FXML private Button btSelectCustomer;
    @FXML private Button btDeleteAppt;
    @FXML private Button btSearchAppt;
    ////////// DATEPICKER //////////
    @FXML private DatePicker datePickerView;
    @FXML private DatePicker datePickerAppt;
    ////////// COMBOBOXES //////////
    @FXML private ComboBox comboType;
    @FXML private ComboBox comboStart;
    @FXML private ComboBox comboEnd;
    ////////// RADIO BUTTONS //////////
    @FXML private ToggleGroup TG;
    @FXML private RadioButton rbtByMonth;
    @FXML private RadioButton rbtByWeek;
    @FXML private RadioButton rbtAll;
    ////////// TEXTFIELDS //////////    
    @FXML private TextField txtApptID;
    @FXML private TextField txtCustomer;
    @FXML private TextField txtTitle;
    @FXML private TextField txtDescription;
    @FXML private TextField txtSearchCustomer;
    @FXML private TextField txtSearchAppointment;    
    @FXML private Label labelTitle;
    ////////// CUSTOMER TABLVIEW //////////
    @FXML private TableView<Customer> tableViewCustomer;
    @FXML private TableColumn<Customer, String> colCusId;
    @FXML private TableColumn<Customer, String> colCusName;
    ////////// APPOINTMENT TABLEVIEW //////////
    @FXML private TableView<Appointment> tableViewAppt;
    @FXML private TableColumn<Appointment, String> colApptId;
    @FXML private TableColumn<Appointment, String> colCusAppt;
    @FXML private TableColumn<Appointment, String> colTitle;
    @FXML private TableColumn<Appointment, String> colType;
    @FXML private TableColumn<Appointment, String> colDate;
    @FXML private TableColumn<Appointment, String> colStart;
    @FXML private TableColumn<Appointment, String> colEnd;

    
    Stage stage;
    Parent scene;
    Customer customer;
    
    ObservableList<String> startTimes = FXCollections.observableArrayList();    
    ObservableList<String> endTimes = FXCollections.observableArrayList();
    
 

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Display all Customers in the Tableview 
        tableViewCustomer.setItems(DBQuery.getAllCustomers());
        // Set up columns in Customer tableView 
        colCusId.setCellValueFactory(new PropertyValueFactory<>("customerId")); 
        colCusName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        // Populate comboType with types
        comboType.setItems(DBQuery.getTypes());
        // Set up start times
        startTimes.addAll("08:00", "09:00", "10:00");
        comboStart.setItems(startTimes);

        
        
    }    

    
    @FXML private void onActionSearchCustomer(ActionEvent event) {
        // Use .clear method to avoid double entries
        DBQuery.customerList.clear();       
        // Assign textfield to variable 
        String search = txtSearchCustomer.getText();
        // call method to find customer by Id or Name
        DBQuery.lookUpCustomer(search, search);
        tableViewCustomer.setItems(DBQuery.customerList);         
    }
    
    @FXML void onActionResetCustomer(ActionEvent event) {
        DBQuery.customerList.clear();
        txtSearchCustomer.setText("");
        tableViewCustomer.setItems(DBQuery.getAllCustomers());
    }
    
    @FXML void onActionSelectCustomer(ActionEvent event) {
        Customer selectCustomer = tableViewCustomer.getSelectionModel().getSelectedItem();
        if (selectCustomer != null) {
            txtCustomer.setText(selectCustomer.getCustomerName());
        } else {
            System.out.println("Please select a customer.");
        }
    }

    @FXML private void onActionUpdateAppt(ActionEvent event) {
    }

    @FXML private void onActionAddAppt(ActionEvent event) {
    }

    @FXML private void onActionDatePickerAppt(ActionEvent event) {
        if (datePickerAppt.getValue() == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Date Missing");
            alert.setContentText("Please select a date.");
            alert.showAndWait();
        } else {
            // LocalDate (JavaClass) is without a time-zone
            LocalDate date = datePickerAppt.getValue();
            String timeTest = "02:51";
            System.out.println(date +" "+ timeTest);
            String ldt = (date +" "+ timeTest);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime localtDateAndTime = LocalDateTime.parse(ldt, formatter);
            System.out.println(".... " + localtDateAndTime);
        }
    }
    
    
    @FXML
    void onActionComboStart(ActionEvent event) {
        if (comboStart.getValue() == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Start Time is  Missing");
            alert.setContentText("Please select a time.");
            alert.showAndWait();
        } else {
            String date = comboStart.getValue().toString();
            String ldt = (date); 
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            //LocalDateTime localtDateAndTime = LocalDateTime.parse(ldt, formatter);
            //System.out.println(".... ComboStart " + localtDateAndTime);
            } 
    }


    @FXML private void onActionSearchAppt(ActionEvent event) {
    }

    @FXML private void onActionDeleteAppt(ActionEvent event) {
    }

    @FXML private void onActionRbtByMonth(ActionEvent event) {
    }

    @FXML private void onActionRbtByWeek(ActionEvent event) {
    }

    @FXML private void onActionRbtViewAll(ActionEvent event) {
    }

    @FXML private void onActionDatePickerTableView(ActionEvent event) {
    }

    @FXML private void onActionDisplayCus(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/Customer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();        
    }

    @FXML private void onActionDisplayReports(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/Reports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();        
    }

    @FXML private void onActionLogout(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/Login.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();        
    }
    
    public void getApptTxtFields() {
        try {
            String apptId = txtApptID.getText();
            String name = txtCustomer.getText();
            String title = txtTitle.getText();
            String description = txtDescription.getText();
            String coType = comboType.getValue().toString();
            String coStart = comboStart.getValue().toString();
            String coEnd = comboEnd.getValue().toString();
            
        } catch(Exception e) {
            System.out.println("Error getting appointment txt fields: " +e.getMessage());
        }
    }
    


    
}
