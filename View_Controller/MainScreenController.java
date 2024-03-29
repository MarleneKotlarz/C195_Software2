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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    ////////// DATEPICKER //////////
    @FXML private DatePicker datePickerAppt;
    @FXML private DatePicker datePickerView;    
    ////////// RADIOBUTTONS //////////
    @FXML private RadioButton rbByMonth;
    @FXML private RadioButton rbByWeek;
    @FXML private RadioButton rbViewAll;
    ////////// COMBOBOXES //////////
    @FXML private ComboBox comboType;
    @FXML private ComboBox<String> comboStart;
    @FXML private ComboBox<String> comboEnd;
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
    
    @FXML private void onActionAddAppt(ActionEvent event) { // REQUIREMENT C, E
        
        try {
            if (datePickerAppt.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");     
            alert.setHeaderText("Appointment Time Error");
            alert.setContentText("Please select a date.");
            alert.showAndWait();             
            } 
            if (comboStart.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");     
            alert.setHeaderText("Appointment Time Error");
            alert.setContentText("Please select a start time.");
            alert.showAndWait();                 
            }
            if (comboEnd.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");     
            alert.setHeaderText("Appointment Time Error");
            alert.setContentText("Please select a end time.");
            alert.showAndWait();                 
            }                         
          
        if (datePickerAppt != null && comboStart != null && comboEnd != null) {
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
        int checkCoTypeSelection = comboType.getSelectionModel().getSelectedIndex();    
       
        // Add appointment if it is within BussinessHours and does not overlap with existing appointment
        if(DBQuery.checkOverlappingAppointments(startSQLIn, endSQLIn) == true && // REQUIREMENT F
           DBQuery.checkAppointmentOutsideBusinessHours(startTimes, endTimes) == true && // REQUIREMENT F
           checkForBlankTextfields(title, description, checkCoTypeSelection) == true) { // REQUIREMENT F
            // Calls addAppointment method and passes in the required parameters
            String coType = comboType.getValue().toString();
            DBQuery.addAppointment(customerId, title, description, coType, startSQLIn, endSQLIn);  
        }        

        // Display appointment tableview
        displayAppointments();   
        }
      
    }catch (NullPointerException e) {
            System.out.println("Check onActionAddAppt code: " + e.getMessage());
    }
    }
   
    //-------- UPDATE APPOINTMENT --------//
    
    @FXML private void onActionUpdateAppt(ActionEvent event) { // REQUIREMENT C, E
        
        try {        
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

            // Appointment textfields needed for updateAppointment arguments
            String customerId = txtCusId.getText();
            String customerName = txtCustomer.getText();
            String title = txtTitle.getText();
            String description = txtDescription.getText();
            int checkCoTypeSelection = comboType.getSelectionModel().getSelectedIndex();                
            String appointmentId = txtApptID.getText();

            if(DBQuery.checkOverlappingAppointments(startSQLIn, endSQLIn) == true && // REQUIREMENT F
            DBQuery.checkAppointmentOutsideBusinessHours(startTimes, endTimes) == true && // REQUIREMENT F
            checkForBlankTextfields(title, description, checkCoTypeSelection) == true) { // REQUIREMENT F
            // Calls addAppointment method and passes in the required parameters
            String coType = comboType.getValue().toString();
            DBQuery.updateAppointment(title, description, coType, startSQLIn, endSQLIn, appointmentId);               
            
            // Display appointment Tablview
            displayAppointments();                                
        }
        }catch(Exception e) {
            System.out.println("Check onActionUpdateAppt code: " + e.getMessage());
        }
    }
    
    
    //-------- DELETE APPOINTMENT --------//
    
    @FXML private void onActionDeleteAppt(ActionEvent event) { // REQUIREMENT C
        
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
    
    @FXML void onClickSetAppointment(MouseEvent event) { // REQUIREMENT E
        
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
            //-- Time conversion --//
            // Make sure that the formatter includes the patterns that are being passed in            
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
        }
    }    
        
    
    //-------- APPOINTMENT TYPES --------//
    
    public static ObservableList <String> getAllTypes() {
        types.removeAll(types);
        types.addAll("English", "German");
        return types;
    }  

    
    //-------- RADIOBUTTON VIEW BY MONTH --------//

    @FXML private void onActionRbtByMonth(ActionEvent event) { // REQUIREMENT D
        
        Appointment selectAppt = new Appointment();        
        if (isWeekMonthDatePickerSelected == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Selection Missing");     
            alert.setHeaderText("Information");
            alert.setContentText("Please select a month using the calendar to the right.");
            alert.showAndWait();
            rbByMonth.setSelected(false);
        } 
        else {
            DBQuery.appointmentList.clear();
            LocalDate date = datePickerView.getValue();
            String monthDatepicker = date.format(monthDTF);
            // call method to get appointments by month method
            tableViewAppt.setItems(DBQuery.getAllAppointmentsByMonth(monthDatepicker)); 
        }
    }
    
    
    //-------- RADIOBUTTON VIEW BY WEEK  --------//

    @FXML private void onActionRbtByWeek(ActionEvent event) { // REQUIREMENT D
        if (isWeekMonthDatePickerSelected == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Selection Missing");     
            alert.setHeaderText("Information");
            alert.setContentText("Please select a week using the calendar to the right.");
            alert.showAndWait();
            rbByWeek.setSelected(false);
        } 
        else {
            DBQuery.appointmentList.clear(); // must clear the tableview before filling it again
            LocalDate date = datePickerView.getValue();
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
            String weekDate = Integer.toString(weekNumber-1);
            tableViewAppt.setItems(DBQuery.getAllAppointmentsByWeek(weekDate));  
        }
    }
    

    //-------- RADIOBUTTON ALL  --------//
    
    @FXML private void onActionRbtViewAll(ActionEvent event) { // REQUIREMENT D
        DBQuery.appointmentList.clear();
        tableViewAppt.setItems(DBQuery.getAllAppointments());
    }

    
    //-------- VERIFY DATE SELECTION --------//
    
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
    
        
    //-------- DISPLAY NUMBER OF APPOINTMENT TYPES BY MONTH --------//
    
    @FXML void onActionDisplayApptTypesByMonth(ActionEvent event) throws IOException {
        DBQuery.getNumberOfApptTypesByMonth().clear(); // REQUIREMENT I
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/Report_ApptTypesByMonth.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();  
    }

    
    //-------- DISPLAY APPOINTMENT TIMES BY MORNING AND AFTERNOON --------//
    
    @FXML void onActionDisplayApptFilteredByTime(ActionEvent event) throws IOException {
        DBQuery.reportApptFilteredByTime().clear(); // REQUIREMENT I
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/Report_ApptFilteredByTime.fxml"));
        stage.setScene(new Scene(scene));
        stage.show(); 
    }

    
    //-------- DISPLAY SCHEDULE FOR CONSULTANTS --------//

    @FXML void onActionDisplayConsultantSchedule(ActionEvent event) throws IOException {
        DBQuery.reportGetAllAppointmentsByUser().clear(); // REQUIREMENT I
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/Report_ConsultantSchedule.fxml"));
        stage.setScene(new Scene(scene));
        stage.show(); 
    }

    
    //-------- LOGOUT BUTTON --------//    
    
    @FXML private void onActionLogout(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/Login.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();        
    }
    

//-------- CHECK TEXTFIELDS --------//
    
    public Boolean checkForBlankTextfields(String title, String description, int coType) { // REQUIREMENT F
        
        if (title.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Invalid title field.");
            alert.setContentText("Please enter a title.");
            alert.showAndWait();
            return false;
        }
        else if (description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Invalid description field.");
            alert.setContentText("Please enter a description.");
            alert.showAndWait();
            return false;            
        }
        else if (coType<0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Invalid type selection.");
            alert.setContentText("Please select a type.");
            alert.showAndWait();
            return false;
        }       
        else{
        return true;    
        }
    }

    
}
