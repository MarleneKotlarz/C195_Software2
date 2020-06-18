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
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
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
    @FXML private Button btLogout;    
    @FXML private Button btUpdateAppt;
    @FXML private Button btResetAppt;
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
    @FXML private TableColumn<Appointment, String> colApptCusId;
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
    Boolean isWeekMonthDatePickerSelected;
    
  
    
    private static ObservableList<String> startTimes = FXCollections.observableArrayList();    
    private static ObservableList<String> endTimes = FXCollections.observableArrayList();
    private static ObservableList<String> appointment = FXCollections.observableArrayList();
    private static ObservableList<String> types = FXCollections.observableArrayList();
 
    
    private final DateTimeFormatter localDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter dateDTF = DateTimeFormatter.ofPattern("dd-MM-YYYY");
    private final DateTimeFormatter monthDTF = DateTimeFormatter.ofPattern("MM");

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        // Display all Customers in the tableview 
        DBQuery.getAllCustomers().clear();
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
        colApptCusId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        // Populate comboType with Appointment Types
        comboType.setItems(getAllTypes());  

        // Set up comboBox ObservableLists startTimes/ endTimes
        startTimes.addAll("07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00");
        endTimes.addAll("07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00");
        comboStart.setItems(startTimes);
        comboEnd.setItems(endTimes); 

        

    }    

    
    
    //-------- LOOKUP CUSTOMER --------//
    
    @FXML private void onActionSearchCustomer(ActionEvent event) {
        // Uses .clear method to avoid double entries
        DBQuery.customerList.clear();       
        // Assign textfield to variable 
        String search = txtSearchCustomer.getText();
        // Calls method to find customer by Id or Name
        DBQuery.lookUpCustomer(search, search);
        tableViewCustomer.setItems(DBQuery.customerList);         
    }
    
    
    //-------- CLEAR CUSTOMER TEXTFIELDS --------//
    
    @FXML void onActionResetCustomer(ActionEvent event) {
        DBQuery.customerList.clear();
        txtSearchCustomer.setText("");
        txtCusId.setText("");
        txtCustomer.setText("");
        tableViewCustomer.setItems(DBQuery.getAllCustomers());
    }
    
    
    //-------- SELECT CUSTOMER FOR APPOINTMENT --------//
    
    @FXML void onActionSelectCustomer(ActionEvent event) {
        Customer selectedCustomer = tableViewCustomer.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            txtCusId.setText(selectedCustomer.getCustomerId());
            txtCustomer.setText(selectedCustomer.getCustomerName()); 
        } else {
            System.out.println("Please select a customer.");
        }
    }


    //-------- ADD APPOINTMENT --------//
    
    @FXML private void onActionAddAppt(ActionEvent event) {
        
        //-- Time conversion --//
        LocalDate localDate = datePickerAppt.getValue(); // Get date from datePicker to use it for the localTime assignment
        // Start Times
        LocalTime startTimes = LocalTime.parse(comboStart.getSelectionModel().getSelectedItem(), timeDTF); // Convert String comboBox to LocalTime type by using parse method and assign it to selected item
        LocalDateTime startLDT = LocalDateTime.of(localDate, startTimes); // Assign localDate and localTime to LocalDateTime
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID()); // Get local zoneID system default - use ZoneId.of(TimeZone.getDefault().getID()) or systemDefault()
        ZonedDateTime startUTC = startLDT.atZone(localZoneId).withZoneSameInstant(ZoneId.of("UTC")); // Convert from LocalDateTime to UTC - use ZoneId.of("UTC") or (localZoneId)
        String startSQLIn = String.valueOf(startUTC.toLocalDateTime()); // Convert from UTC to LocalDateTime so it can be inserted into the MYSQL database
        // End Times
        LocalTime endTimes = LocalTime.parse(comboEnd.getSelectionModel().getSelectedItem(), timeDTF); // Convert String comboBox to LocalTime type by using parse method and assign it to selected item
        LocalDateTime endLDT = LocalDateTime.of(localDate, endTimes); // Assign localDate and localTime to LocalDateTime
        localZoneId = ZoneId.of(TimeZone.getDefault().getID()); // Get local zoneID system default - use ZoneId.of(TimeZone.getDefault().getID()) or systemDefault()
        ZonedDateTime endUTC = endLDT.atZone(localZoneId).withZoneSameInstant(ZoneId.of("UTC")); // Convert from LocalDateTime to UTC
        String endSQLIn = String.valueOf(endUTC.toLocalDateTime()); // Convert from UTC to LocalDateTime so it can be inserted into the MYSQL database

        // Appointment textfields - User Input will be displayed in the appointment tableView
        String customerId = txtCusId.getText();
        String title = txtTitle.getText();
        String description = txtDescription.getText();
        String coType = comboType.getSelectionModel().getSelectedItem().toString();
        
        // Add appointment if it is within BussinessHours and does not overlap with existing appointment
        if(DBQuery.checkOverlappingAppointments(startSQLIn, endSQLIn) && DBQuery.checkAppointmentOutsideBusinessHours(startTimes, endTimes) == true) {
            // Calls addAppointment method and passes in the required parameters
            DBQuery.addAppointment(customerId, title, description, coType, startSQLIn, endSQLIn);  
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error"); 
            alert.setContentText("Please select an appointment time that is within business hours (08:00 - 17:00) and do not overlap existing appointments!");
            alert.showAndWait();
        }
        
        // Display appointment tableview
        displayAppointments();

    }
    
   
    //-------- UPDATE APPOINTMENT --------//
    
    @FXML private void onActionUpdateAppt(ActionEvent event) {
        
        try {
            // Appointment textfields needed for updateAppointment arguments
            String customerId = txtCusId.getText();
            String customerName = txtCustomer.getText();
            String title = txtTitle.getText();
            String description = txtDescription.getText();
            String coType = comboType.getSelectionModel().getSelectedItem().toString();
            String appointmentId = txtApptID.getText();

            //-- Time Conversion --//
            LocalDate localDate = datePickerAppt.getValue();
            // Start Times        
            LocalTime startTimes = LocalTime.parse(comboStart.getSelectionModel().getSelectedItem(), timeDTF); 
            LocalDateTime startLDT = LocalDateTime.of(localDate, startTimes); 
            ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID()); 
            ZonedDateTime startUTC = startLDT.atZone(localZoneId).withZoneSameInstant(ZoneId.of("UTC"));
            String startSQLIn = String.valueOf(startUTC.toLocalDateTime()); 
            // End Times
            LocalTime endTimes = LocalTime.parse(comboEnd.getSelectionModel().getSelectedItem(), timeDTF);
            LocalDateTime endLDT = LocalDateTime.of(localDate, endTimes);
            localZoneId = ZoneId.of(TimeZone.getDefault().getID());            
            ZonedDateTime endUTC = endLDT.atZone(localZoneId).withZoneSameInstant(ZoneId.of("UTC"));
            String endSQLIn = String.valueOf(endUTC.toLocalDateTime());


            // Calls updateAppointment method and passes in required arguments
            DBQuery.updateAppointment(title, description, coType, startSQLIn, endSQLIn, appointmentId);               

            // Display appointment Tablview
            displayAppointments();        
            
        }catch(Exception e) {
          System.out.println("Error editing appointment: " + e.getMessage());
        }  
    }
    
    
    //-------- DELETE APPOINTMENT --------//
    
    @FXML private void onActionDeleteAppt(ActionEvent event) {
        
        // Selected appointment
        Appointment deleteAppt = tableViewAppt.getSelectionModel().getSelectedItem();
        String apptId = deleteAppt.getApptId();
        // Set AlertBox with Button
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to delete this appointment?");
        alert.setTitle("Delete Appointment");        
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {            
            //call delete Customer method and pass in value
            DBQuery.deleteAppointment(apptId);           
            // Load appointment tableView again with updated appointment List
            displayAppointments();
        }      
    }
    

    //-------- POPULATE APPOINTMENT TABLEVIEW --------//
    
    public void displayAppointments() {
        // Clears the ObservableList appointmentList - needed so that if a button is pressed the data doesn't show up twice again
        DBQuery.appointmentList.clear();
        // Sets tableView with getAllAppointments method to show all records
        tableViewAppt.setItems(DBQuery.getAllAppointments());
    }
    
    //-------- POPULATE TEXTFIELDS TO UPDATE APPOINTMENT --------//
    
    @FXML void onClickSetAppointment(MouseEvent event) {
        
        // Get selected appointment record
        Appointment displayAppt = tableViewAppt.getSelectionModel().getSelectedItem();
        
        // Poluate textfields with data from Appointment TableView so they can be updated
        try {
            // Set data from tableview to textfields
            txtApptID.setText(displayAppt.getApptId());
            txtCusId.setText(displayAppt.getCusId());
            txtCustomer.setText(displayAppt.getCusName());
            txtTitle.setText(displayAppt.getTitle());
            txtDescription.setText(displayAppt.getDescription());
            comboType.setValue(displayAppt.getType());
 
            //------------ Populate DatePicker & ComboBoxes ------------//
            // You have to make sure that the formatter includes the patterns that are being passed in
            
            //-- Time conversion --//
            // Start Times
            String startDateTime = displayAppt.getStart();
            LocalDateTime startLDT = LocalDateTime.parse(startDateTime, localDTF);
            LocalTime startLT = startLDT.toLocalTime();
            String start = startLT.format(timeDTF);
            // End Times
            String endDateTime = displayAppt.getEnd();
            LocalDateTime endLDT = LocalDateTime.parse(endDateTime, localDTF);
            LocalTime endLT = endLDT.toLocalTime();
            String end = endLT.format(timeDTF);
            // Time conversion DatePicker
            LocalDateTime dateLDT = LocalDateTime.parse(startDateTime, localDTF);
            LocalDate populateDatePicker = dateLDT.toLocalDate();
            // Set comboBox & DatePickervalues 
            comboStart.setValue(start);
            comboEnd.setValue(end);            
            datePickerAppt.setValue(populateDatePicker);               
                
        }catch(Exception e){
          System.out.println("Error onClickSetAppointment: " + e.getMessage());
          e.printStackTrace();
        }

    }    
        
    
    //-------- APPOINTMENT TYPES --------//
    
    public static ObservableList <String> getAllTypes() {
        types.removeAll(types);
        types.addAll("English", "German");
        return types;
    }  


    @FXML private void onActionRbtByMonth(ActionEvent event) {
        
        Appointment selectAppt = new Appointment();
        
        if (isWeekMonthDatePickerSelected == null) {
        // Set AlertBox with Button
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Selection Missing");     
        alert.setHeaderText("Information");
        alert.setContentText("Please select a month using the calendar to the right.");
        alert.showAndWait();
        } 
        else {
            DBQuery.appointmentList.clear(); // must clear the tableview before filling it again
            LocalDate date = datePickerView.getValue();
            String monthDatepicker = date.format(monthDTF);
            // call method to get appointments by month filter
            tableViewAppt.setItems(DBQuery.getAllAppointmentsByMonth(monthDatepicker)); 

        }
}

    @FXML private void onActionRbtByWeek(ActionEvent event) {
        if (isWeekMonthDatePickerSelected == null) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Selection Missing");     
        alert.setHeaderText("Information");
        alert.setContentText("Please select a week using the calendar to the right.");
        alert.showAndWait();
        } 
        else {
            // send this week number to the dbquery.getAllAppointmentsByWeek()
            DBQuery.appointmentList.clear(); // must clear the tableview before filling it again
            LocalDate date = datePickerView.getValue();
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
            String weekDate = Integer.toString(weekNumber-1);
            // call method to get appointments by week filter
            tableViewAppt.setItems(DBQuery.getAllAppointmentsByWeek(weekDate));  
        }
    }

    
    @FXML private void onActionRbtViewAll(ActionEvent event) {
        DBQuery.appointmentList.clear();
        tableViewAppt.setItems(DBQuery.getAllAppointments());
    }

    
    @FXML private void onActionDatePickerTableView(ActionEvent event) {
        isWeekMonthDatePickerSelected = true;
    }
    
    
    //-------- CLEAR APPOINTMENTS TEXTFIELDS --------//
    
    @FXML void onActionResetAppt(ActionEvent event) {
        DBQuery.appointmentList.clear();
        txtCusId.setText("");
        txtCustomer.setText("");
        txtApptID.setText("");
        txtTitle.setText("");
        txtDescription.setText("");
        comboType.setValue("");        
        datePickerAppt.setValue(null);
        comboStart.setValue(null);
        comboEnd.setValue(null);
        
        tableViewAppt.setItems(DBQuery.getAllAppointments());    
    }
    
    //-------- DISPLAY CUSTOMER SCREEN --------//
    
    @FXML private void onActionDisplayCus(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/Customer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();        
    }
        
    //-------- DISPLAY REPORTS SCREEN --------//
    
    @FXML void onActionDisplayApptTypesByMonth(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/Report_ApptTypesByMonth.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();  
    }

    @FXML void onActionDisplayApptFilteredByTime(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/Report_ApptFilteredByTime.fxml"));
        stage.setScene(new Scene(scene));
        stage.show(); 
    }


    @FXML void onActionDisplayConsultantSchedule(ActionEvent event) {

    }

    
    @FXML private void onActionLogout(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/Login.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();        
    }

    
    
    
    
    
    
    @FXML private void onActionDatePickerAppt(ActionEvent event) {          
    }
 
    
    @FXML void onActionComboStart(ActionEvent event) {
    }
   
    
    @FXML void onActionComboEnd(ActionEvent event) {
    }
           
    
    
    
}
