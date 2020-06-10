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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import java.util.TimeZone;
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
import javafx.scene.input.MouseEvent;
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
    @FXML private ComboBox<String> comboStart;
    @FXML private ComboBox<String> comboEnd;
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
    String selectedDate;
    String selectedStartTime;
    String selectedEndTime;
    String combinedDateTimeStart;
    String combinedDateTimeEnd;
    
    private static ObservableList<String> startTimes = FXCollections.observableArrayList();    
    private static ObservableList<String> endTimes = FXCollections.observableArrayList();
    private static ObservableList<String> appointment = FXCollections.observableArrayList();
    private static ObservableList<String> types = FXCollections.observableArrayList();
 
    private final DateTimeFormatter timeDTD = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter dateDTF = DateTimeFormatter.ofPattern("YYYY-MM-dd");
    
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
        displayAppointments();

        
        // Set up columns in Appointment tableView
        colApptId.setCellValueFactory(new PropertyValueFactory<>("apptId")); 
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("cusName")); 
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("end"));


    }    

    ////////// LOOKUP CUSTOMER //////////
    @FXML private void onActionSearchCustomer(ActionEvent event) {
        // Use .clear method to avoid double entries
        DBQuery.customerList.clear();       
        // Assign textfield to variable 
        String search = txtSearchCustomer.getText();
        // call method to find customer by Id or Name
        DBQuery.lookUpCustomer(search, search);
        tableViewCustomer.setItems(DBQuery.customerList);         
    }
    
    ////////// CLEAR CUSTOMER TEXTFIELDS //////////
    @FXML void onActionResetCustomer(ActionEvent event) {
        DBQuery.customerList.clear();
        //txtCusId.setText("");
        txtSearchCustomer.setText("");
        txtCusId.setText("");
        txtCustomer.setText("");
        tableViewCustomer.setItems(DBQuery.getAllCustomers());
    }
    
    ////////// SELECT CUSTOMER FOR APPOINTMENT //////////
    @FXML void onActionSelectCustomer(ActionEvent event) {
        //Customer selectCustomer = tableViewCustomer.getSelectionModel().getSelectedItem();
        Customer selectedCustomer = tableViewCustomer.getSelectionModel().getSelectedItem();
      //  tableViewCustomer.
        if (selectedCustomer != null) {
            txtCusId.setText(selectedCustomer.getCustomerId());
            txtCustomer.setText(selectedCustomer.getCustomerName()); 
        } else {
            System.out.println("Please select a customer.");
        }
    }


    ////////// ADD APPOINTMENT //////////
    @FXML private void onActionAddAppt(ActionEvent event) {
        
        // Time conversion
        LocalDate localDate = datePickerAppt.getValue();
        // convert String comboBox to LocalTime type by using parse method
        LocalTime startTimes = LocalTime.parse(comboStart.getSelectionModel().getSelectedItem(), timeDTD);
        LocalTime endTimes = LocalTime.parse(comboEnd.getSelectionModel().getSelectedItem(), timeDTD);
        
        LocalDateTime startLDT = LocalDateTime.of(localDate, startTimes);
        LocalDateTime endLDT = LocalDateTime.of(localDate, endTimes);
        
        // get local zoneID system default - use systemDefault()or ZoneId.of(TimeZone.getDefault().getID())
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        

        // convert from localTime to UTC
        ZonedDateTime startUTC = startLDT.atZone(localZoneId).withZoneSameInstant(ZoneId.of("UTC")); // use ZoneId.of("UTC") or (localZoneId)
        ZonedDateTime endUTC = endLDT.atZone(localZoneId).withZoneSameInstant(ZoneId.of("UTC"));
                
        
        // convert from UTC to localTime so it can be inserted into the MYSQL database
        String startSQLIn = String.valueOf(startUTC.toLocalDateTime());
        String endSQLIn = String.valueOf(endUTC.toLocalDateTime());
        
        
        
        // textfields User Input that will be displayed in the appointment tableview
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

        DBQuery.addAppointment(customerId, title, description, coType, startSQLIn, endSQLIn);
        
        // Display appointment Tablview
        displayAppointments();


        // Keep this in case I need to work with a LocalDateTime object within Java
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        LocalDateTime startDateAndTimeObject = LocalDateTime.parse(combinedDateTimeStart, formatter);
//        LocalDateTime endDateAndTimeObject = LocalDateTime.parse(combinedDateTimeEnd, formatter);
        
    }
    
    
    ////////// UPDATE APPOINTMENT //////////
    @FXML private void onActionUpdateAppt(ActionEvent event) {
        try {
            String customerId = txtCusId.getText();
            String customerName = txtCustomer.getText();
            String title = txtTitle.getText();
            String description = txtDescription.getText();
            String coType = comboType.getSelectionModel().getSelectedItem().toString();
            String coStart = comboStart.getValue().toString();
            String coEnd = comboEnd.getValue().toString();
            String appointmentId = txtApptID.getText();

            // These two variables will need to be passed into the DB query addAllAppointment method along with with textfields and customer selection
            combinedDateTimeStart = selectedDate + " " + selectedStartTime;
            combinedDateTimeEnd = selectedDate + " " + selectedEndTime;
            coStart = combinedDateTimeStart;
            coEnd = combinedDateTimeEnd;    

            DBQuery.updateAppointment(customerId, title, description, coType, coStart, coEnd, appointmentId);

            // Display appointment Tablview
            displayAppointments();        
            
        }catch(Exception e) {
          System.out.println("Error editing appointment: " + e.getMessage());
        }  

        
    }
    

    ////////// POPULATE APPOINTMENT TABLEVIEW //////////
    public void displayAppointments() {
        DBQuery.appointmentList.clear();
        tableViewAppt.setItems(DBQuery.getAllAppointments());
        
    }
    
    ////////// POPULATE TEXTFIELDS TO UPDATE APPOINTMENT //////////
    // Poluate textfields with data from Appointment TableView so they can be updated
    @FXML void onClickSetAppointment(MouseEvent event) {
        
        Appointment displayAppt = tableViewAppt.getSelectionModel().getSelectedItem();
        
        if (displayAppt != null) {
            // Set data from tableview to textfields
            txtApptID.setText(displayAppt.getApptId());
            txtCustomer.setText(displayAppt.getCusName());
            txtTitle.setText(displayAppt.getTitle());
            txtDescription.setText(displayAppt.getDescription());
            comboType.setValue(displayAppt.getType());
            
            
//            datePickerAppt.setValue(LocalDate.parse(displayAppt.toString(), timeDTD));
//            
//            comboStart.setValue(displayAppt.getStart());
//            comboEnd.setValue(displayAppt.getEnd());            
            
        }else {
            System.out.println("No appointment selection");
        }

    }    
        
    ////////// APPOINTMENT TYPES //////////
    public static ObservableList <String> getAllTypes() {
        types.removeAll(types);
        types.addAll("Beginner", "Intermediate", "Advanced");
        return types;
    }
    

    @FXML private void onActionDatePickerAppt(ActionEvent event) {
    }
 
    @FXML void onActionComboStart(ActionEvent event) {
    }
   
    
    @FXML void onActionComboEnd(ActionEvent event) {
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
    
    ////////// DISPLAY CUSTOMER SCREEN //////////
    @FXML private void onActionDisplayCus(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/Customer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();        
    }
        
    ////////// DISPLAY REPORTS SCREEN //////////
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
