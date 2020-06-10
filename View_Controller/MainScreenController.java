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
    ////////// CUSTOMER TABLEVIEW //////////
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
 
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter dateDTF = DateTimeFormatter.ofPattern("dd-MM-YYYY");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        // Display all Customers in the tableview 
        tableViewCustomer.setItems(DBQuery.getAllCustomers());
        // Set up columns in Customer tableView 
        colCusId.setCellValueFactory(new PropertyValueFactory<>("customerId")); 
        colCusName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        
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

        // Populate comboType with Appointment Types
        comboType.setItems(getAllTypes());
        // Set up comboBox ObservableLists startTimes/ endTimes
        startTimes.addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00");
        endTimes.addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00");
        comboStart.setItems(startTimes);
        comboEnd.setItems(endTimes); 

    }    

    
    ////////// LOOKUP CUSTOMER //////////
    @FXML private void onActionSearchCustomer(ActionEvent event) {
        // Uses .clear method to avoid double entries
        DBQuery.customerList.clear();       
        // Assign textfield to variable 
        String search = txtSearchCustomer.getText();
        // Calls method to find customer by Id or Name
        DBQuery.lookUpCustomer(search, search);
        tableViewCustomer.setItems(DBQuery.customerList);         
    }
    
    
    ////////// CLEAR CUSTOMER TEXTFIELDS //////////
    @FXML void onActionResetCustomer(ActionEvent event) {
        DBQuery.customerList.clear();
        txtSearchCustomer.setText("");
        txtCusId.setText("");
        txtCustomer.setText("");
        tableViewCustomer.setItems(DBQuery.getAllCustomers());
    }
    
    
    ////////// SELECT CUSTOMER FOR APPOINTMENT //////////
    @FXML void onActionSelectCustomer(ActionEvent event) {
        Customer selectedCustomer = tableViewCustomer.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            txtCusId.setText(selectedCustomer.getCustomerId());
            txtCustomer.setText(selectedCustomer.getCustomerName()); 
        } else {
            System.out.println("Please select a customer.");
        }
    }


    ////////// ADD APPOINTMENT //////////
    @FXML private void onActionAddAppt(ActionEvent event) {
        
        //------------ Time conversion ------------//
        // Get date from datePicker to use it for the localTime assignment
        LocalDate localDate = datePickerAppt.getValue();
        
        // Convert String comboBox to LocalTime type by using parse method and assign it to selected item
        LocalTime startTimes = LocalTime.parse(comboStart.getSelectionModel().getSelectedItem(), timeDTF);
        LocalTime endTimes = LocalTime.parse(comboEnd.getSelectionModel().getSelectedItem(), timeDTF);
        // Assign localDate and localTime to LocalDateTime
        LocalDateTime startLDT = LocalDateTime.of(localDate, startTimes);
        LocalDateTime endLDT = LocalDateTime.of(localDate, endTimes);
        // Get local zoneID system default - use ZoneId.of(TimeZone.getDefault().getID()) or systemDefault()
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        // Convert from LocalDateTime to UTC 
        ZonedDateTime startUTC = startLDT.atZone(localZoneId).withZoneSameInstant(ZoneId.of("UTC")); // use ZoneId.of("UTC") or (localZoneId)
        ZonedDateTime endUTC = endLDT.atZone(localZoneId).withZoneSameInstant(ZoneId.of("UTC"));
        // Convert from UTC to LocalDateTime so it can be inserted into the MYSQL database
        String startSQLIn = String.valueOf(startUTC.toLocalDateTime());
        String endSQLIn = String.valueOf(endUTC.toLocalDateTime());
        
        
        
        // Appointment textfields - User Input will be displayed in the appointment tableView
        String customerId = txtCusId.getText();
        String customerName = txtCustomer.getText();
        String title = txtTitle.getText();
        String description = txtDescription.getText();
        String coType = comboType.getSelectionModel().getSelectedItem().toString();
        // Calls addAppointment method and passes in the required parameters
        DBQuery.addAppointment(customerId, title, description, coType, startSQLIn, endSQLIn);
        // Display appointment tableview
        displayAppointments();

    }
    
    
    ////////// UPDATE APPOINTMENT //////////
    @FXML private void onActionUpdateAppt(ActionEvent event) {
        
        try {
            
            //------------ Time conversion ------------//
            LocalDate localDate = datePickerAppt.getValue();
            
            LocalTime startTimes = LocalTime.parse(comboStart.getSelectionModel().getSelectedItem(), timeDTF);
            LocalTime endTimes = LocalTime.parse(comboEnd.getSelectionModel().getSelectedItem(), timeDTF);
            LocalDateTime startLDT = LocalDateTime.of(localDate, startTimes);
            LocalDateTime endLDT = LocalDateTime.of(localDate, endTimes);		
            ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
            ZonedDateTime startUTC = startLDT.atZone(localZoneId).withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime endUTC = endLDT.atZone(localZoneId).withZoneSameInstant(ZoneId.of("UTC"));
            String startSQLIn = String.valueOf(startUTC.toLocalDateTime());
            String endSQLIn = String.valueOf(endUTC.toLocalDateTime());
        
            // Appointment textfields needed for updateAppointment arguments
            String customerId = txtCusId.getText();
            String customerName = txtCustomer.getText();
            String title = txtTitle.getText();
            String description = txtDescription.getText();
            String coType = comboType.getSelectionModel().getSelectedItem().toString();
            
        // String coStart = comboStart.getValue().toString();
        // String coEnd = comboEnd.getValue().toString();
            String appointmentId = txtApptID.getText();

            // Calls updateAppointment method and passes in required arguments
            DBQuery.updateAppointment(customerId, title, description, coType, startSQLIn, endSQLIn, appointmentId);
            // Display appointment Tablview
            displayAppointments();        
            
        }catch(Exception e) {
          System.out.println("Error editing appointment: " + e.getMessage());
        }  
    }
    

    ////////// POPULATE APPOINTMENT TABLEVIEW //////////
    public void displayAppointments() {
        // Clears the ObservableList appointmentList 
        // Needed so that if a button is pressed the data doesn't show up twice again
        DBQuery.appointmentList.clear();
        // Sets tableView with getAllAppointments method to show all records
        tableViewAppt.setItems(DBQuery.getAllAppointments());
    }
    
    ////////// POPULATE TEXTFIELDS TO UPDATE APPOINTMENT //////////
    @FXML void onClickSetAppointment(MouseEvent event) {
        // Get selected appointment record

        Appointment displayAppt = tableViewAppt.getSelectionModel().getSelectedItem();
        // Poluate textfields with data from Appointment TableView so they can be updated
        try {

                // Set data from tableview to textfields
                txtApptID.setText(displayAppt.getApptId());
                txtCustomer.setText(displayAppt.getCusName());
                txtTitle.setText(displayAppt.getTitle());
                txtDescription.setText(displayAppt.getDescription());
                comboType.setValue(displayAppt.getType());
 
                //------------ Populate DatePicker & ComboBoxes ------------//
                // Gets the startDate from the tableView, splits it in half and assigns variables to each piece
                String startDateTimeStamp = displayAppt.getStart();
                // Use a blank "space" as the delimiter to seperate the string
                String[] startDateTimeStampArray = startDateTimeStamp.split(" "); 
                // Assign element [0] of startDateTimeStampArray to the date string
                String date = startDateTimeStampArray[0]; 
                // Assign element [1] of startDateTimeStampArray to the startTime string
                String startTime = startDateTimeStampArray[1]; 
                // Convert the "date" string variable from above to datatype LocalDate
                LocalDate populateDatePicker = LocalDate.parse(date); 
                // Set DatePicker to date provided by populateDatePicker object value
                datePickerAppt.setValue(populateDatePicker); 
                // Gets the endDate from the Table view, splits it in half and assigns variables to each piece                
                String endDateTimeStamp = displayAppt.getEnd();
                String[] endDateTimeStampArray = endDateTimeStamp.split(" ");
                    // String enddate = endDateTimeStampArray[0]; // Not used since endDate is not needed for ComboBox endTime
                // Assign element [1] of endDateTimeStampArray to the end string
                String endTime = endDateTimeStampArray[1]; 
                // Set comboBox values 
                comboStart.setValue(startTime);
                comboEnd.setValue(endTime);            

        }catch(Exception e){
          System.out.println("Error editing appointment: " + e.getMessage());
          e.printStackTrace();
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
