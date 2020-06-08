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
    @FXML private TextField txtCusId;
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
    @FXML private TableColumn<Appointment, String> colCustomerName;
    @FXML private TableColumn<Appointment, String> colTitle;
    @FXML private TableColumn<Appointment, String> colDescription;
    @FXML private TableColumn<Appointment, String> colType;
    @FXML private TableColumn<Appointment, String> colStart;
    @FXML private TableColumn<Appointment, String> colEnd;

    
    Stage stage;
    Parent scene;
    Customer customer;
    String selectedDate;
    String selectedStartTime;
    String selectedEndTime;
    String combinedDateTimeStart;
    String combinedDateTimeEnd;
    
    private static ObservableList<String> startTimes = FXCollections.observableArrayList();    
    private static ObservableList<String> endTimes = FXCollections.observableArrayList();
    private static ObservableList<String> appointment = FXCollections.observableArrayList();
    private static ObservableList<String> types = FXCollections.observableArrayList();
 

    
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
        comboType.setItems(getAllTypes());
        // Set up start times
        startTimes.addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00");
        endTimes.addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00");
        comboStart.setItems(startTimes);
        comboEnd.setItems(endTimes);       
        
        //Display appointment tableView
       tableViewAppt.setItems(DBQuery.getAllAppointments());

        
        // Set up columns in Appointment tableView
        colApptId.setCellValueFactory(new PropertyValueFactory<>("apptId")); 
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("cusName")); 
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("end"));


        

        
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
        //txtCusId.setText("");
        txtSearchCustomer.setText("");
        tableViewCustomer.setItems(DBQuery.getAllCustomers());
    }
    
    @FXML void onActionSelectCustomer(ActionEvent event) {
        //Customer selectCustomer = tableViewCustomer.getSelectionModel().getSelectedItem();
        customer = tableViewCustomer.getSelectionModel().getSelectedItem();
      //  tableViewCustomer.
        if (customer != null) {
            txtCusId.setText(customer.getCustomerId());
            txtCustomer.setText(customer.getCustomerName()); 
        } else {
            System.out.println("Please select a customer.");
        }
    }



    @FXML private void onActionAddAppt(ActionEvent event) {
        
        //String apptId = txtApptID.getText();
        String customerId = txtCusId.getText();
        String customerName = txtCustomer.getText();
        String title = txtTitle.getText();
        String description = txtDescription.getText();
        
        String coType = comboType.getSelectionModel().getSelectedItem().toString();
        String coStart = comboStart.getValue().toString();
        String coEnd = comboEnd.getValue().toString();

        // These two variables will need to be passed into the DB query addAllAppointment method along with with textfields and customer selection
        combinedDateTimeStart = selectedDate + " " + selectedStartTime;
        combinedDateTimeEnd = selectedDate + " " + selectedEndTime;
        
        coStart = combinedDateTimeStart;
        coEnd = combinedDateTimeEnd;
        
        //DBQuery.getCustomerId(selectedDate)
        getApptTxtFields();
        DBQuery.addAppointment(customerId, title, description, coType, coStart, coEnd);


    


        // Keep this in case I need to work with a LocalDateTime object within Java
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        LocalDateTime startDateAndTimeObject = LocalDateTime.parse(combinedDateTimeStart, formatter);
//        LocalDateTime endDateAndTimeObject = LocalDateTime.parse(combinedDateTimeEnd, formatter);
        
    }
    
    @FXML private void onActionUpdateAppt(ActionEvent event) {
    }
    
    
    public void getApptTxtFields() {
        try {
            String apptId = txtApptID.getText();
            String customerId = txtCusId.getText();
            String customerName = txtCustomer.getText();
            String title = txtTitle.getText();
            String description = txtDescription.getText();
            String coType = comboType.getValue().toString();
            String coStart = comboStart.getValue().toString();
            String coEnd = comboEnd.getValue().toString();
            
        } catch(Exception e) {
            System.out.println("Error getting appointment txt fields: " +e.getMessage());
        }
    }
        
    
    public static ObservableList <String> getAllTypes() {
        types.removeAll(types);
        types.addAll("Beginner", "Intermediate", "Advanced");
        return types;
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
            String dateSelection = datePickerAppt.getValue().toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate formatedDateSelection = LocalDate.parse(dateSelection, formatter);
            selectedDate = formatedDateSelection.format(formatter);
            
        }
    }
    
    
    @FXML void onActionComboStart(ActionEvent event) {
        if (comboStart.getValue() == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Start Time is  Missing");
            alert.setContentText("Please select a time.");
            alert.showAndWait();
        } else {
            String startSelection = comboStart.getValue().toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime formattedStartTime = LocalTime.parse(startSelection, formatter);
            selectedStartTime = formattedStartTime.format(formatter);
            } 
    }
    

    @FXML void onActionComboEnd(ActionEvent event) {
        if (comboEnd.getValue() == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Start Time is  Missing");
            alert.setContentText("Please select a time.");
            alert.showAndWait();
        } else {
            String endSelection = comboEnd.getValue().toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime formattedEndTime = LocalTime.parse(endSelection, formatter);
            selectedEndTime = formattedEndTime.format(formatter);
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

    
}
